package org.zetaframework.core.log.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统日志配置参数
 *
 * @author gcc
 */
@ConfigurationProperties(prefix = LogProperties.PREFIX)
public class LogProperties {
    public static final String PREFIX = "zeta.log";

    /** 日志开关 默认为：false 不记录用户的操作日志、异常日志 */
    private Boolean enabled = false;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
