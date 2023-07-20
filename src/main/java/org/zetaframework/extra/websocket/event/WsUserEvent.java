package org.zetaframework.extra.websocket.event;

import org.springframework.context.ApplicationEvent;
import org.zetaframework.extra.websocket.enums.WsUserTypeEnum;
import org.zetaframework.extra.websocket.model.WsUser;

/**
 * Websocket用户 事件
 *
 * 说明：
 * 1.主要用来发送用户上线、下线事件通知
 * 2.在{@link WsUserEventListener}中处理本事件
 * @author gcc
 */
public class WsUserEvent extends ApplicationEvent {

    private WsUser user;

    public WsUserEvent(WsUser user, WsUserTypeEnum source) {
        super(source);
        this.user = user;
    }

    public WsUser getUser() {
        return user;
    }

    public void setUser(WsUser user) {
        this.user = user;
    }
}
