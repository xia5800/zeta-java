package org.zetaframework.core.saToken.properties;

import cn.hutool.core.collection.CollUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * security忽略鉴权配置类
 * @author gcc
 */
@ConfigurationProperties(prefix = "zeta.security.ignore")
public class IgnoreProperties {

    /** 基础忽略鉴权地址 */
    private List<String> baseUrl = CollUtil.newArrayList(
        "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/**/*.ico",
                "/**/*.jpg",
                "/**/*.png",
                "/**/*.gif",
                "/**/api-docs/**",
                "/**/api-docs-ext/**",
                "/**/swagger-resources/**",
                "/**/webjars/**",
                "/druid/**",
                "/error",
                "/ws/**",
                "/api/login",
                "/api/captcha"
    );

    /** 忽略鉴权的地址 */
    private List<String> ignoreUrl = CollUtil.newArrayList("/**/noToken/**");


    /**
     * 获取saToken放行路由
     */
    public List<String> getNotMatchUrl() {
        return new ArrayList<String>() {{
            addAll(baseUrl);
            addAll(ignoreUrl);
        }};
    }

    public List<String> getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(List<String> baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<String> getIgnoreUrl() {
        return ignoreUrl;
    }

    public void setIgnoreUrl(List<String> ignoreUrl) {
        this.ignoreUrl = ignoreUrl;
    }
}
