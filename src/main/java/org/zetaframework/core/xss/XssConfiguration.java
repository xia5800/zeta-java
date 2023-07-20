package org.zetaframework.core.xss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zetaframework.core.xss.cleaner.HutoolXssCleaner;
import org.zetaframework.core.xss.cleaner.XssCleaner;
import org.zetaframework.core.xss.filter.XssFilter;
import org.zetaframework.core.xss.properties.XssProperties;
import org.zetaframework.core.xss.serializer.XssStringJsonDeserializer;

/**
 * XSS跨站脚本攻击防护 配置
 *
 * 说明：
 * XSS攻击通常指的是通过利用网页开发时留下的漏洞，通过巧妙的方法注入恶意指令代码到网页，使用户加载并执行攻击者恶意制造的网页程序。
 * 这些恶意网页程序通常是JavaScript，但实际上也可以包括Java、 VBScript、ActiveX、 Flash 或者甚至是普通的HTML。
 * 攻击成功后，攻击者可能得到包括但不限于更高的权限（如执行一些操作）、私密网页内容、会话和cookie等各种内容。 -- 摘自：百度百科
 *
 * @author gcc
 */
@Configuration
@EnableConfigurationProperties(XssProperties.class)
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = XssProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = false)
public class XssConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(XssConfiguration.class);

    private final XssProperties xssProperties;
    public XssConfiguration(XssProperties xssProperties) {
        logger.info("XSS跨站脚本攻击防护功能：启动");
        this.xssProperties = xssProperties;
    }

    /**
     * 配置 XSS文本清理者
     * @return XssCleaner
     */
    @Bean
    public XssCleaner xssCleaner() {
        // ps：这里图省事直接用的hutool的XSS过滤方法，如果需要自定义XSS过滤，仿造着实现一个XssCleaner接口就行
        return new HutoolXssCleaner();
    }

    /**
     * 配置XSS过滤器
     *
     * @param xssCleaner
     * @return
     */
    @Bean
    public FilterRegistrationBean<XssFilter> xssFilter(XssCleaner xssCleaner) {
        XssFilter xssFilter = new XssFilter(xssCleaner, xssProperties);
        FilterRegistrationBean<XssFilter> bean = new FilterRegistrationBean<>(xssFilter);
        bean.setOrder(-1);
        return bean;
    }

    /**
     * 配置Json对象也能使用XSS清理
     *
     * @param xssCleaner
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer2(XssCleaner xssCleaner) {
        return build -> {
            build.deserializerByType(String.class, new XssStringJsonDeserializer(xssCleaner, xssProperties));
        };
    }

}
