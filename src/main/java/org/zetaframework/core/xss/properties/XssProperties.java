package org.zetaframework.core.xss.properties;

import cn.hutool.core.collection.CollUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * XSS防护配置参数
 *
 * @author gcc
 */
@ConfigurationProperties(prefix = XssProperties.PREFIX)
public class XssProperties {
    public static final String PREFIX = "zeta.xss";
    /** AntPath规则匹配器 */
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    /** XSS防护开关 默认为：false */
    private Boolean enabled = false;

    /** 基础忽略xss防护的地址 */
    private List<String> baseUrl = CollUtil.newArrayList(
            "/doc.html",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/swagger-resources"
    );

    /** 忽略xss防护的地址 */
    private List<String> excludeUrl = CollUtil.newArrayList(
            "/**/noxss/**"
    );

    /**
     * 获取忽略xss防护的地址
     */
    private List<String> getNotMatchUrl() {
        return new ArrayList<String>() {{
            addAll(baseUrl);
            addAll(excludeUrl);
        }};
    }

    /**
     * 是否是忽略xss防护的地址
     *
     * @param path 请求地址
     * @return boolean
     */
    public Boolean isIgnoreUrl(String path) {
        return this.getNotMatchUrl().stream().anyMatch(url ->
            ANT_PATH_MATCHER.match(url, path)
        );
    }

    /**
     * 是否是忽略xss防护的地址
     *
     * @return boolean
     */
    public Boolean isIgnoreUrl() {
        // 获取当前访问的路由。获取不到直接return false
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return false;
        HttpServletRequest request = attributes.getRequest();
        return isIgnoreUrl(String.valueOf(request.getRequestURL()));
    }


    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(List<String> baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<String> getExcludeUrl() {
        return excludeUrl;
    }

    public void setExcludeUrl(List<String> excludeUrl) {
        this.excludeUrl = excludeUrl;
    }
}
