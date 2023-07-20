package com.zeta.msg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.zetaframework.extra.websocket.model.WsUser;

/**
 * 聊天室
 *
 * @author gcc
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class ChartController {

    private final SimpUserRegistry userRegistry;
    private final SimpMessagingTemplate simpMessagingTemplate;


    /**
     * <h3>群发消息</h3>
     *
     * 前端：
     * <pre>
     * 发送topic: '/group'
     * 订阅topic: '/topic/group'
     * </pre>
     *
     * 前端代码参考：
     * <pre>
     * // 群发
     * stompClient.send("/group", {}, '大家好');
     *
     * // 订阅群发消息
     * stompClient.subscribe('/topic/group', function (data) { console.log(data.body); });
     * </pre>
     *
     * 说明：
     * 广播消息，不指定用户，所有订阅此topic的用户都能接收到消息
     * @param message  演示用，这边只接收String类型的信息。
     */
    @MessageMapping("/group")
    @SendTo("/topic/group")
    public String group(String message) {
        return message;
    }


    /**
     * <h3>私聊</h3>
     *
     * 前端：
     * <pre>
     * 发送topic: '/private'
     * 订阅topic: '/user/queue/private'
     * </pre>
     *
     * 前端代码参考：
     * <pre>
     * // 私聊  send方法3个参数分别是("topic", 请求头，内容)
     * stompClient.send("/private", {toUserId: toUserId}, '你好，这是一条私聊信息');
     *
     * // 订阅私聊消息
     * stompClient.subscribe('/user/queue/private', function (data) { console.log(data.body); });
     * </pre>
     *
     * 说明：
     * 一对一消息，需指定用户
     * @param message  消息内容
     * @param toUserId 请求头中携带的 消息接收用户id
     */
    @MessageMapping("/private")
    public void privateChat(String message, @Header("toUserId") String toUserId) {
        try {
            simpMessagingTemplate.convertAndSendToUser(toUserId, "/queue/private", message);
        }catch (MessagingException e) {
            log.error("私聊消息发送失败", e);
        }
    }


    /**
     * <h3>主动查询自己的信息</h3>
     *
     * 前端：
     * <pre>
     * 发送topic: '/info'
     * 订阅topic: '/user/queue/info'
     * </pre>
     *
     * 前端代码参考：
     * <pre>
     * // 主动查询自己的信息
     * stompClient.send("/info", {}, {});
     * // 订阅
     * stompClient.subscribe('/user/queue/info', function (data) { console.log(data.body); });
     * </pre>
     * @param user: Principal
     */
    @MessageMapping("/info")
    @SendToUser("/queue/info")
    public String getInfo(WsUser user) {
        return user.toString();
    }


    /**
     * <h3>订阅topic获得当前在线人数</h3>
     *
     * 前端：
     * <pre>
     * 订阅topic: '/onlineUserCount'
     * </pre>
     *
     * 前端代码参考：
     * <pre>
     * // 订阅
     * stompClient.subscribe('/onlineUserCount', function (data) { console.log(data.body); });
     * </pre>
     *
     * 说明：
     * websocket连接后，一订阅该topic马上就会收到回复
     */
    @SubscribeMapping("/onlineUserCount")
    public Integer getOnlineUserCount() {
        return userRegistry.getUserCount();
    }

}
