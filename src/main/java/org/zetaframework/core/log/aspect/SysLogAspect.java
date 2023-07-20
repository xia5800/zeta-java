package org.zetaframework.core.log.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.zetaframework.core.log.annotation.SysLog;
import org.zetaframework.core.log.enums.LogTypeEnum;
import org.zetaframework.core.log.event.SysLogEvent;
import org.zetaframework.core.log.model.SysLogDTO;
import org.zetaframework.core.utils.IpAddressUtil;
import org.zetaframework.core.utils.JSONUtil;
import org.zetaframework.core.utils.ZetaFunction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 系统日志 切面
 *
 * @author gcc
 */
// @Component  // 为了可以控制开启、关闭全局日志记录。改为Bean配置的方式
@Aspect
public class SysLogAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();
    private static final Integer MAX_LENGTH = 65535;

    private final ApplicationContext context;
    public SysLogAspect(ApplicationContext context) {
        this.context = context;
    }

    @Pointcut("@annotation(org.zetaframework.core.log.annotation.SysLog)")
    public void SysLogAspect() {}

    /**
     * 执行方法之前
     *
     * @param joinPoint JoinPoint
     */
    @Before(value = "SysLogAspect()")
    public void doBefore(JoinPoint joinPoint) {
        // 记录操作开始时间
        START_TIME.set(System.currentTimeMillis());
    }

    /**
     * 执行方法之后
     *
     * @param joinPoint JoinPoint
     * @param result Object
     */
    @AfterReturning(pointcut = "SysLogAspect()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        publishEvent(joinPoint, result);
    }

    /**
     * 发生异常之后
     *
     * @param joinPoint JoinPoint
     * @param e Throwable
     */
    @AfterThrowing(pointcut = "SysLogAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        publishEvent(joinPoint, e);
    }

    /**
     * 发布日志存储事件
     *
     * @param joinPoint JoinPoint
     * @param result Object
     */
    private void publishEvent(JoinPoint joinPoint, Object result) {
        publishEvent(joinPoint, result, null);
    }

    /**
     * 发布日志存储事件
     *
     * @param joinPoint JoinPoint
     * @param exception Throwable
     */
    private void publishEvent(JoinPoint joinPoint, Throwable exception) {
        publishEvent(joinPoint, null, exception);
    }

    /**
     * 发布日志存储事件
     *
     * @param joinPoint JoinPoint
     * @param result Object
     * @param exception Throwable
     */
    private void publishEvent(JoinPoint joinPoint, Object result, Throwable exception) {
        // 方法耗时
        Long spendTime = getSpendTime();
        // 获取注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog sysLog = null;
        // 方法上的@SysLog注解
        if (method.isAnnotationPresent(SysLog.class)) {
            sysLog = method.getAnnotation(SysLog.class);
        }
        if (sysLog == null || !sysLog.enabled()) {
            return;
        }

        // 构造系统日志
        SysLogDTO sysLogDTO = buildSysLogDTO(joinPoint, sysLog);
        sysLogDTO.setType(LogTypeEnum.OPERATION.name());
        sysLogDTO.setSpendTime(spendTime);
        sysLogDTO.setResult(getResponse(result, sysLog));
        sysLogDTO.setException(getException(exception, () -> {
            sysLogDTO.setType(LogTypeEnum.EXCEPTION.name());
        }));
        // 发布保存系统日志事件
        context.publishEvent(new SysLogEvent(sysLogDTO));
    }

    /**
     * 获取方法耗时
     */
    private Long getSpendTime() {
        // 记录结束时间
        long spendTime = 0L;
        if (START_TIME.get() != null) {
            spendTime = System.currentTimeMillis() - START_TIME.get();
        }
        START_TIME.remove();
        logger.debug("操作耗时：{}", spendTime);
        return spendTime;
    }

    /**
     * 构造系统日志
     * @param joinPoint JoinPoint
     * @param sysLog SysLog
     * @return SysLogDTO
     */
    private SysLogDTO buildSysLogDTO(JoinPoint joinPoint, SysLog sysLog) {
        SysLogDTO sysLogDTO = new SysLogDTO();

        // 类路径
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String name = joinPoint.getSignature().getName();
        String classPath = StrUtil.join(".", declaringTypeName, name);
        sysLogDTO.setClassPath(classPath);

        // 操作描述
        sysLogDTO.setDescription(getDescription(joinPoint, sysLog));

        // 获取请求地址、请求方式、ip等
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) { return sysLogDTO; }
        HttpServletRequest request = attributes.getRequest();
        UserAgent ua = UserAgentUtil.parse(ServletUtil.getHeaderIgnoreCase(request, "User-Agent"));
        String ip = ServletUtil.getClientIP(request);
        String ipRegion = StrUtil.EMPTY;
        if (StrUtil.isNotEmpty(ip)) {
            ipRegion = IpAddressUtil.search(ip);
        }

        // 记录请求地址、请求方式、ip 等
        sysLogDTO.setUrl(request.getRequestURI());
        sysLogDTO.setHttpMethod(request.getMethod());
        sysLogDTO.setOs(ua.getPlatform().getName());
        sysLogDTO.setDevice(ua.getOs().getName());
        sysLogDTO.setBrowser(ua.getBrowser().getName());
        sysLogDTO.setIp(ip);
        sysLogDTO.setIpRegion(ipRegion);
        // 获取请求参数
        if (sysLog.request()) {
            sysLogDTO.setParams(getRequestParam(joinPoint, request));
        }

        return sysLogDTO;
    }


    /**
     * 获取请求参数
     * @param joinPoint JoinPoint
     * @param request HttpServletRequest
     * @return String
     */
    private String getRequestParam(JoinPoint joinPoint, HttpServletRequest request) {
        String params = null;

        Map<String, String> paramMap = ServletUtil.getParamMap(request);
        if (MapUtil.isNotEmpty(paramMap)) {
            params = JSONUtil.toJsonStr(paramMap);
        } else {
            if (ArrayUtil.isNotEmpty(joinPoint.getArgs())) {
                List<Object> paramList = new ArrayList<>();
                Arrays.stream(joinPoint.getArgs()).forEach(it -> {
                    if (!(it instanceof HttpServletRequest)
                        && !(it instanceof HttpServletResponse)
                        && !(it instanceof MultipartFile)
                    ) {
                        paramList.add(it);
                    }
                });

                if (CollUtil.isNotEmpty(paramList)) {
                    params = JSONUtil.toJsonStr(paramList);
                }
            }
        }
        return params;
    }

    /**
     * 获取操作描述
     *
     * 格式：xxx-xxxx
     * "Api注解的tags值"-"SysLog注解的value值 或 ApiOperation注解的value值"
     * @return String
     */
    private String getDescription(JoinPoint joinPoint, SysLog sysLog) {
        StringBuilder sb = new StringBuilder();

        // 获取@Api的value值
        Api api = joinPoint.getTarget().getClass().getAnnotation(Api.class);
        if (api != null) {
            if (ArrayUtil.isNotEmpty(api.tags())) {
                sb.append(api.tags()[0]).append("-");
            }
        }

        // 获取@SysLog的value值
        if (StrUtil.isNotBlank(sysLog.value())) {
            sb.append(sysLog.value());
        } else {
            // 获取@ApiOperation的value值
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            if (method.isAnnotationPresent(ApiOperation.class)) {
                ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                if (StrUtil.isNotBlank(apiOperation.value())) {
                    sb.append(apiOperation.value());
                } else {
                    // @SysLog没有value值、@ApiOperation没有value值的情况下。显示方法名
                    sb.append(method.getName());
                }
            } else {
                // @SysLog没有value值、没有@ApiOperation注解的情况下。显示方法名
                sb.append(method.getName());
            }
        }
        return sb.toString();
    }

    /**
     * 获取返回值
     * @param result Object
     * @param sysLog 注解
     * @return String
     */
    private String getResponse(Object result, SysLog sysLog) {
        return sysLog.response() ? JSONUtil.toJsonStr(result) : StrUtil.EMPTY;
    }

    /**
     * 获取异常
     * @param exception Throwable
     * @param block ZetaFunction
     * @return String
     */
    private String getException(Throwable exception, ZetaFunction block) {
        if (exception != null) {
            block.run();
            return ExceptionUtil.stacktraceToString(exception, MAX_LENGTH);
        }

        return StrUtil.EMPTY;
    }

}
