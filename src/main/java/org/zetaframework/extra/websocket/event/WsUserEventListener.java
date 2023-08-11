package org.zetaframework.extra.websocket.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.zetaframework.extra.websocket.enums.WsUserTypeEnum;
import org.zetaframework.extra.websocket.model.WsUser;

import java.util.function.BiConsumer;

/**
 * Websocket用户状态 事件监听
 *
 * 说明：
 * 监听用户上线、下线事件
 *
 * 使用说明：
 * 1. 在业务包中，@Bean配置一个WsUserEventListener
 * 2. 用户上线、下线之后要做的事交给具体的业务去实现
 * @author gcc
 */
public class WsUserEventListener {
    private final BiConsumer<WsUser, WsUserTypeEnum> consumer;

    /**
     * 构造方法
     *
     * 使用方式：
     * <pre>
     *     new WsUserEventListener((WsUser user, WsUserTypeEnum typeEnum) -> {
     *          // Do something
     *     })
     * </pre>
     * @param consumer
     */
    public WsUserEventListener(BiConsumer<WsUser, WsUserTypeEnum> consumer) {
        this.consumer = consumer;
    }

    /**
     * 处理用户上线、下线事件
     *
     * 说明：
     * 该方法不实现，交给具体业务去实现
     * @param event 用户上线、下线事件
     */
    @Async
    @EventListener(WsUserEvent.class)
    public void saveSysLoginLog(WsUserEvent event) {
        WsUserTypeEnum userType = (WsUserTypeEnum) event.getSource();
        consumer.accept(event.getUser(), userType);
    }

}
