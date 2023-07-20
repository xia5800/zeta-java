package com.zeta.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zetaframework.extra.websocket.enums.WsUserTypeEnum;
import org.zetaframework.extra.websocket.event.WsUserEventListener;
import org.zetaframework.extra.websocket.model.WsUser;

/**
 * websocket事件配置
 * @author gcc
 */
@Slf4j
@Configuration
public class WebsocketEventConfiguration {

    /**
     * 配置"Websocket用户状态"事件监听器
     *
     * @return WsUserEventListener
     */
    @Bean
    public WsUserEventListener wsUserEventListener() {
        return new WsUserEventListener((WsUser user, WsUserTypeEnum typeEnum) -> {
            switch (typeEnum) {
                case ONLINE:
                    log.info("websocket用户上线: {}", user.getUserId());
                    break;
                case OFFLINE:
                    log.info("websocket用户离线: {}", user.getUserId());
                    break;
            }
        });
    }

}
