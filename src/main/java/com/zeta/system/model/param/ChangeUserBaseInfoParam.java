package com.zeta.system.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 修改用户基本信息参数
 *
 * @author gcc
 */
@Data
@ApiModel(description = "修改用户基本信息参数")
public class ChangeUserBaseInfoParam {

    /** 用户名 */
    @ApiModelProperty(value = "用户名", required = true)
    @NotEmpty(message = "用户名不能为空")
    @Size(max = 32, message = "用户名长度不能超过32")
    private String username;

    /** 邮箱 */
    @ApiModelProperty(value = "邮箱", required = false)
    private String email;

    /** 手机号 */
    @ApiModelProperty(value = "手机号", required = false)
    private String mobile;

    /** 性别 0未知 1男 2女 */
    @ApiModelProperty(value = "性别 0未知 1男 2女", required = true)
    @NotNull(message = "性别不能为空")
    private Integer sex;

    /** 生日 */
    @ApiModelProperty(value = "生日", required = false)
    private LocalDate birthday;

}
