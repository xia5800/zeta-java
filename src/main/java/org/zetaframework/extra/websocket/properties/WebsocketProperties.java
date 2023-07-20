package org.zetaframework.extra.websocket.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * websocket配置
 *
 * @author gcc
 */
@ConfigurationProperties(prefix = WebsocketProperties.PREFIX)
public class WebsocketProperties {
    public static final String PREFIX = "zeta.websocket";

    /** websocket开关 默认为：false */
    private Boolean enabled = false;


    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
