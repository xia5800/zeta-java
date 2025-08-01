package org.zetaframework.core.saToken;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.filter.SaFilterAuthStrategy;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.jwt.StpLogicJwtForMixin;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.jwt.StpLogicJwtForStateless;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.enums.ErrorCodeEnum;
import org.zetaframework.core.mybatisplus.properties.DatabaseProperties;
import org.zetaframework.core.saToken.interceptor.ClearThreadLocalInterceptor;
import org.zetaframework.core.saToken.properties.IgnoreProperties;
import org.zetaframework.core.saToken.properties.TokenProperties;
import org.zetaframework.core.utils.ContextUtil;
import org.zetaframework.core.utils.JSONUtil;

import javax.servlet.http.HttpServletResponse;

/**
 * Sa-Token 权限认证 配置类
 * @author gcc
 */
@Configuration
@EnableConfigurationProperties({IgnoreProperties.class, TokenProperties.class})
public class SaTokenConfigure implements WebMvcConfigurer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IgnoreProperties ignoreProperties;
    private final TokenProperties tokenProperties;
    private final DatabaseProperties dataBaseProperties;
    public SaTokenConfigure(IgnoreProperties ignoreProperties, TokenProperties tokenProperties, DatabaseProperties dataBaseProperties) {
        this.ignoreProperties = ignoreProperties;
        this.tokenProperties = tokenProperties;
        this.dataBaseProperties = dataBaseProperties;
    }

    /**
     * 跨域配置
     *
     * 说明：
     * 非saToken拦截的接口的跨域配置
     * @param registry CorsRegistry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowCredentials(true)
                .maxAge(3600L);
    }

    /**
     * SaToken过滤器[前置函数]：在每次[认证函数]之前执行
     *
     * 说明：
     * saToken拦截的接口的跨域配置
     * BeforeAuth 不受 includeList 与 excludeList 的限制，所有请求都会进入
     */
    private final SaFilterAuthStrategy beforeAuth = r -> {
        // saToken跨域配置
        SaHolder.getResponse()
                .setHeader("Access-Control-Allow-Origin", "*")
                .setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH")
                .setHeader("Access-Control-Max-Age", "3600")
                .setHeader("Access-Control-Allow-Headers", "*")
                // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
                .setHeader("X-XSS-Protection", "1; mode=block");

        // 如果是预检请求，则立即返回到前端
        SaRouter.match(SaHttpMethod.OPTIONS).back();
    };

    /**
     * SaToken过滤器[认证函数]: 每次请求都会执行
     *
     * 说明：
     * saToken接口拦截并处理
     * 主要是校验token是否有效，对token进行续期，设置用户id和token到ThreadLocal中
     */
    private final SaFilterAuthStrategy auth = new SaFilterAuthStrategy() {
        @Override
        public void run(Object r) {
            // 需要登录认证的路由:所有, 排除登录认证的路由:/api/login、swagger等
            SaRouter.match("/**").check(() -> {
                StpUtil.checkLogin();

                // token续期
                if (tokenProperties.getAutoRenew()) {
                    StpUtil.renewTimeout(tokenProperties.getExpireTime());
                }

                // 获取用户id，并设置到ThreadLocal中。（mybatis-plus自动填充用到）
                switch (dataBaseProperties.getUserIdType()) {
                    case Long:
                        ContextUtil.setUserId(StpUtil.getLoginIdAsLong());
                        break;
                    case Int:
                        ContextUtil.setUserId(StpUtil.getLoginIdAsInt());
                        break;
                    case String:
                        ContextUtil.setUserId(StpUtil.getLoginIdAsString());
                        break;
                    default:
                        break;
                }
                ContextUtil.setToken(StpUtil.getTokenValue());
            });
        }
    };

    /**
     * 拦截器配置
     *
     * 说明：
     * 可以在这里使用<a href="https://sa-token.dev33.cn/doc/index.html#/use/route-check">拦截器鉴权</a>
     * 针对某个接口，某些接口单独进行权限校验
     * @param registry InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 清空ThreadLocal数据拦截器。
        registry.addInterceptor(new ClearThreadLocalInterceptor()).addPathPatterns("/api/**");
    }

    /**
     * 注册 [Sa-Token全局过滤器]
     * @return SaServletFilter
     */
    @Bean
    public SaServletFilter saServletFilter() {
        return new SaServletFilter()
                // 指定拦截路由
                .addInclude("/**")
                // 指定放行路由
                .setExcludeList(ignoreProperties.getNotMatchUrl())
                .setBeforeAuth(beforeAuth)
                .setAuth(auth)
                .setError(this::returnFail);
    }

    /**
     * Sa-Token token风格配置
     * @return StpLogic
     */
    @Bean
    public StpLogic stpLogic() {
        StpLogic stpLogic;
        switch (tokenProperties.getType()) {
            case SIMPLE:
                logger.info("检测到sa-token采用了[jwt-simple模式]");
                stpLogic = new StpLogicJwtForSimple();
                break;
            case MIXIN:
                logger.info("检测到sa-token采用了[jwt-mixin模式]");
                stpLogic = new StpLogicJwtForMixin();
                break;
            case STATELESS:
                logger.info("检测到sa-token采用了[jwt-stateless模式]");
                stpLogic = new StpLogicJwtForStateless();
                break;
            default:
                logger.info("检测到sa-token采用了default模式");
                stpLogic = new StpLogic(StpUtil.TYPE);
                break;
        }
        return stpLogic;
    }

    /**
     * return 错误消息
     *
     * 注意：这里的异常不会被GlobalExceptionHandler(全局异常处理器)捕获处理
     * @param e
     * @return
     */
    private String returnFail(Throwable e) {
        // 初始化错误码和错误信息
        int statusCode = HttpStatus.BAD_REQUEST.value();
        int code = ErrorCodeEnum.FAIL.getCode();
        String message = "";

        if (e instanceof NotLoginException) {
            // 处理NotLoginException异常的错误信息
            switch (((NotLoginException) e).getType()) {
                case NotLoginException.NOT_TOKEN:
                    message = NotLoginException.NOT_TOKEN_MESSAGE;
                    break;
                case NotLoginException.INVALID_TOKEN:
                    message = NotLoginException.INVALID_TOKEN_MESSAGE;
                    break;
                case NotLoginException.TOKEN_TIMEOUT:
                    message = NotLoginException.TOKEN_TIMEOUT_MESSAGE;
                    break;
                case NotLoginException.BE_REPLACED:
                    message = NotLoginException.BE_REPLACED_MESSAGE;
                    break;
                case NotLoginException.KICK_OUT:
                    message = NotLoginException.KICK_OUT_MESSAGE;
                    break;
                case NotLoginException.TOKEN_FREEZE:
                    message = NotLoginException.TOKEN_FREEZE_MESSAGE;
                    break;
                case NotLoginException.NO_PREFIX:
                    message = NotLoginException.NO_PREFIX_MESSAGE;
                    break;
                default:
                    message = NotLoginException.DEFAULT_MESSAGE;
                    break;
            }
            code = ErrorCodeEnum.UNAUTHORIZED.getCode();
            statusCode = HttpStatus.UNAUTHORIZED.value();
        } else if (e instanceof NotRoleException || e instanceof NotPermissionException) {
            // 处理NotRoleException和NotPermissionException异常的错误信息
            message = ErrorCodeEnum.FORBIDDEN.getMsg();
            code = ErrorCodeEnum.FORBIDDEN.getCode();
            statusCode = HttpStatus.FORBIDDEN.value();
        } else {
            // 处理其它异常的错误信息
            message = e.getMessage();
        }

        // 手动设置Content-Type为json格式，替换之前重写SaServletFilter.doFilter方法的写法
        HttpServletResponse response = SpringMVCUtil.getResponse();
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        response.setStatus(statusCode);

        return JSONUtil.toJsonStr(new ApiResult<Boolean>(code, message));
    }
}
