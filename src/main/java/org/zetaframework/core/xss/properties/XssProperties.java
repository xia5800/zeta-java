package org.zetaframework.core.xss.properties;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

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
    private Set<String> baseUrl = CollUtil.newHashSet(
            "/doc.html",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/swagger-resources"
    );

    /** 忽略xss防护的地址 */
    private Set<String> excludeUrl = CollUtil.newHashSet(
            "/**/noxss/**"
    );

    /**
     * 获取忽略xss防护的地址
     */
    public Set<String> getNotMatchUrl() {
        return new HashSet<String>() {{
            addAll(baseUrl);
            addAll(excludeUrl);
        }};
    }

    /**
     * 是否是忽略xss防护的地址
     *
     * 匹配三种类型的路由地址
     * <pre>{@code
     * 1.带*号的。用ANT_PATH_MATCHER处理
     * 2.类似“/api/user/save”、“/doc.html”这样的。用ANT_PATH_MATCHER处理
     * 3.带请求方式的，类似“GET:/api/demo”、“POST:/api/demo”这样的。“:”切割后再用ANT_PATH_MATCHER处理
     * }</pre>
     *
     * @param requestMethod 请求方式 GET、PUT、POST...
     * @param path 请求地址
     * @return boolean
     */
    public Boolean isIgnoreUrl(String requestMethod, String path) {
        return this.getNotMatchUrl().stream().anyMatch(url -> {
            // 先正常匹配一遍。
            if (ANT_PATH_MATCHER.match(url, path)) return true;

            // 没匹配到，就将ignoreUrl中包含“:"的切开来单独匹配
            if (StrUtil.contains(url, StrPool.COLON)) {
                // 将url切开。 例如："GET:/api/demo" -> ["GET", "/api/demo"]
                String[] array = url.split(StrPool.COLON);

                // 只处理请求方式一致的
                if (requestMethod.equalsIgnoreCase(array[0])) {
                    return ANT_PATH_MATCHER.match(array[1], path);
                }
            }

            return false;
        });
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
        return isIgnoreUrl(request.getMethod(), String.valueOf(request.getRequestURL()));
    }


    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<String> getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(Set<String> baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Set<String> getExcludeUrl() {
        return excludeUrl;
    }

    public void setExcludeUrl(Set<String> excludeUrl) {
        this.excludeUrl = excludeUrl;
    }
}
