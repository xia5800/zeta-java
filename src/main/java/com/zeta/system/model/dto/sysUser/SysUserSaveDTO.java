package com.zeta.system.model.dto.sysUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 新增 用户
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:51
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "新增用户")
public class SysUserSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户名 */
    @ApiModelProperty(value = "用户名", required = true)
    @NotEmpty(message = "用户名不能为空")
    @Size(max = 32, message = "用户名长度不能超过32")
    private String username;

    /** 账号 */
    @ApiModelProperty(value = "账号", required = true)
    @NotEmpty(message = "账号不能为空")
    @Size(max = 64, message = "账号长度不能超过64")
    private String account;

    /** 密码 */
    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty(message = "密码不能为空")
    @Size(max = 64, message = "密码长度不能超过64")
    private String password;

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

    /** 头像 */
    @ApiModelProperty(value = "头像", required = false)
    private String avatar;

    /** 生日 */
    @ApiModelProperty(value = "生日", required = false)
    private LocalDate birthday;

    /** 角色id列表 为空代表不关联用户角色 */
    @ApiModelProperty(value = "角色id列表 为空代表不关联用户角色", required = false)
    private List<Long> roleIds;

}
