package org.zetaframework.extra.websocket.interceptor;

import cn.hutool.core.util.StrUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.zetaframework.extra.websocket.enums.WsUserTypeEnum;
import org.zetaframework.extra.websocket.event.WsUserEvent;
import org.zetaframework.extra.websocket.model.WsUser;

import java.security.Principal;

/**
 * Websocket用户信息 拦截器
 *
 * @author gcc
 */
@Component
public class WsUserInterceptor implements ChannelInterceptor {

    private final ApplicationContext applicationContext;
    public WsUserInterceptor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    /**
     * 在消息实际发送到通道之前调用。
     * 这允许在必要时修改消息。
     * 如果此方法返回null ，则不会发生实际的发送调用。
     *
     * @param message
     * @param channel
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 解码消息
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && accessor.getCommand() != null) {
            switch (accessor.getCommand()) {
                case CONNECT:
                    // 获取用户信息
                    String userId = accessor.getFirstNativeHeader("userId");
                    if (StrUtil.isBlank(userId)) {
                        throw new MessagingException("用户信息获取失败");
                    }

                    // 构造一个websocket用户
                    WsUser wsUser = new WsUser(userId);
                    accessor.setUser(wsUser);

                    // 发布一个用户上线事件，用户上线之后要做的事交给具体的业务去实现
                    applicationContext.publishEvent(new WsUserEvent(wsUser, WsUserTypeEnum.ONLINE));
                    break;
                case DISCONNECT:
                    Principal user = accessor.getUser();
                    // 说明：临时解决客户端断开连接，触发两次DISCONNECT问题
                    if (user != null && accessor.getMessageHeaders().size() == 5) {
                        // 发布一个用户离线事件，用户离线之后要做的事交给具体的业务去实现
                        applicationContext.publishEvent(new WsUserEvent((WsUser) user, WsUserTypeEnum.OFFLINE));
                    }
                    break;
                default:
                    break;
            }
        }
        return message;
    }
}
