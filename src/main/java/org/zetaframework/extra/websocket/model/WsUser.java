package org.zetaframework.extra.websocket.model;

import org.zetaframework.extra.websocket.interceptor.WsUserInterceptor;

import java.security.Principal;

/**
 * Websocket用户信息
 *
 * 说明：主要用于{@link WsUserInterceptor}
 * @author gcc
 */
public class WsUser implements Principal {

    private String userId;

    @Override
    public String getName() {
        return this.userId;
    }

    public WsUser(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "WsUser(userId="+userId+")";
    }
}
