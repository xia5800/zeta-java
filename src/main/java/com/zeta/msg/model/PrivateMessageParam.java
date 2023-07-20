package com.zeta.msg.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 私聊消息参数
 *
 * @author gcc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "私聊消息")
public class PrivateMessageParam {

    /** 接收人 */
    @ApiModelProperty(value = "接收人")
    @NotBlank(message = "消息接收人不能为空")
    private String toUserId;

    /** 发送的消息 */
    @ApiModelProperty(value = "发送的消息")
    @NotBlank(message= "消息不能为空")
    private String message;

}
