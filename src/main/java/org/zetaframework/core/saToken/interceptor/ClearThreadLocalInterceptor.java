package org.zetaframework.core.satoken.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.zetaframework.core.utils.ContextUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 清空ThreadLocal数据拦截器
 *
 * 说明：
 * 执行完Controller之后清空ContextUtil中ThreadLocal的值
 *
 * @author gcc
 */
public class ClearThreadLocalInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 每次请求之前触发的方法
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param handler Object
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("本次请求的请求路径为: {}", request.getServletPath());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 执行完Controller之后，要做的事
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param handler Object
     * @param ex Exception
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清空ThreadLocal的值，防止下次请求时获取到的值是旧数据，同时也能防止内存溢出
        ContextUtil.remove();
    }
}
