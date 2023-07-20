package com.zeta.system.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 登录参数
 *
 * @author gcc
 */
@Data
@ApiModel(description = "登录参数")
public class LoginParam {

    /** 账号 */
    @ApiModelProperty(value = "账号", required = true)
    @NotBlank(message = "账号不能为空")
    private String account;

    /** 密码 */
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    /** 验证码key */
    @ApiModelProperty(value = "验证码key", required = true)
    @NotNull(message = "验证码key不能为空")
    private Long key;

    /** 验证码 */
    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;

}
