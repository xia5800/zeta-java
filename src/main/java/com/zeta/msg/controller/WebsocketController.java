package com.zeta.msg.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zeta.msg.model.PrivateMessageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zetaframework.base.result.ApiResult;

/**
 * websocket测试
 * @author gcc
 */
@Slf4j
@RequiredArgsConstructor
@Api(tags = "websocket测试")
@RestController
@RequestMapping("/api/msg")
public class WebsocketController {

    private final SimpUserRegistry userRegistry;
    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 群发消息
     *
     * @param message
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "群发消息")
    @GetMapping("/group")
    public ApiResult<Boolean> group(@RequestParam String message) {
        try {
            simpMessagingTemplate.convertAndSend("/topic/group", message);
        }catch (MessagingException e) {
            log.error("群发消息发送失败", e);

            // 演示不实现SuperBaseController接口，接口怎么返回
            return ApiResult.fail("群发消息发送失败", false);
        }
        return ApiResult.success(true);
    }


    /**
     * 私聊消息
     *
     * @param message
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "私聊消息")
    @PostMapping("/group")
    public ApiResult<Boolean> privateChat(@RequestBody @Validated PrivateMessageParam message) {
        try {
            simpMessagingTemplate.convertAndSendToUser(
                    message.getToUserId(),
                    "/queue/private",
                    message.getMessage()
            );
        }catch (MessagingException e) {
            log.error("私聊消息发送失败", e);

            // 演示不实现SuperBaseController接口，接口怎么返回
            return ApiResult.fail("私聊消息发送失败", false);
        }
        return ApiResult.success(true);
    }


    /**
     * 获取当前在线人数
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "获取当前在线人数")
    @GetMapping("/onlineUser")
    public ApiResult<Integer> onlineUser() {
        return ApiResult.success(userRegistry.getUserCount());
    }
}
