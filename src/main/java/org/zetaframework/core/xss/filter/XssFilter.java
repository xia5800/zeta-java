package org.zetaframework.core.xss.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zetaframework.core.xss.cleaner.XssCleaner;
import org.zetaframework.core.xss.properties.XssProperties;
import org.zetaframework.core.xss.wrapper.XssRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义用于XSS防护的 过滤器
 *
 * @author gcc
 */
public class XssFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final XssCleaner xssCleaner;
    private final XssProperties xssProperties;
    public XssFilter(XssCleaner xssCleaner, XssProperties xssProperties) {
        this.xssCleaner = xssCleaner;
        this.xssProperties = xssProperties;
    }

    /**
     * 执行过滤
     *
     * @param request 请求
     * @param response 响应
     * @param filterChain 过滤器链
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(new XssRequestWrapper(request, xssCleaner), response);
        }catch (Exception e) {
            logger.error("执行XSS过滤失败", e);
        }
    }

    /**
     * 如果返回true，则这个请求不会被过滤
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // XSS防护开关是否关闭
        if (!xssProperties.getEnabled()) {
            return true;
        }

        // 当前url是否是忽略xss防护的地址
        return xssProperties.isIgnoreUrl(request.getRequestURI());
    }
}
