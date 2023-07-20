package org.zetaframework.core.redis.aspect;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zetaframework.core.redis.annotation.Limit;
import org.zetaframework.core.redis.exception.LimitException;
import org.zetaframework.core.redis.helper.RedisHelper;
import org.zetaframework.core.utils.ContextUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 限流注解 切面
 * @author gcc
 */
@Aspect
@Component
public class LimitAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RedisHelper redisHelper;
    public LimitAspect(RedisHelper redisHelper) {
        this.redisHelper = redisHelper;
    }

    @Around("@annotation(org.zetaframework.core.redis.annotation.Limit)")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Limit limitAnnotation = method.getAnnotation(Limit.class);

        if (!limitAnnotation.enabled()) {
            // 不启动限流
            return joinPoint.proceed();
        }

        // 获取注解里设置的key值
        String methodName = limitAnnotation.key();
        if (methodName.isBlank()) {
            methodName = method.getName();
        }

        // 根据限流的类型返回处理后的redisKey 如果是IP限流，则redisKey = limit:{methodName}:{ip}。
        String suffix = getRedisKey(limitAnnotation, methodName);
        // prefix:suffix
        String redisKey = StrUtil.join(StrPool.COLON, limitAnnotation.prefix(), suffix);

        boolean limitResult = false;
        try {
            // 获取限流情况
            limitResult = redisHelper.luaScriptLimit(redisKey, limitAnnotation.period(), limitAnnotation.count());
        } catch (Exception e) {
            // 获取限流情况失败
            logger.error("获取限流情况失败", e);
            throw new LimitException(limitAnnotation.describe());
        }

        if (!limitResult) {
            // 触发限流
            logger.info("触发接口限流");
            throw new LimitException(limitAnnotation.describe());
        }

        return joinPoint.proceed();
    }


    /**
     * 根据限流的类型返回处理后的redisKey
     *
     * @param limitAnnotation 限流注解
     * @param methodName      方法名
     * @return redisKey: String
     */
    private String getRedisKey(Limit limitAnnotation, String methodName) {
        String redisKey = "";
        switch (limitAnnotation.limitType()) {
            // IP限流
            case IP:
                // 获取请求ip
                String ip = "unknown";
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    ip = ServletUtil.getClientIP(request);
                }
                // methodName:ip
                redisKey = StrUtil.join(StrPool.COLON, methodName, ip);
                break;

            // 用户id限流
            case USERID:
                // 获取用户id
                String userId = ContextUtil.getUserIdStr();
                if (StrUtil.isBlank(userId)) {
                    throw new LimitException("获取用户id失败");
                }
                // methodName:userId
                redisKey = StrUtil.join(StrPool.COLON, methodName, userId);
                break;
        }

        return redisKey;
    }

}
