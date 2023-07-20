package com.zeta.system.model.dto.sysUser;

import com.zeta.system.model.dto.sysRole.SysRoleDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.zetaframework.extra.desensitization.annotation.Desensitization;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户 详情
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:51
 */
@Data
@ApiModel(description = "用户详情")
public class SysUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    /** 修改人 */
    @ApiModelProperty(value = "修改人")
    private Long updatedBy;

    /** 状态 */
    @ApiModelProperty(value = "状态")
    private Integer state;

    /** 用户名 */
    @ApiModelProperty(value = "用户名")
    private String username;

    /** 账号 */
    @Desensitization(rule = "3_4") // 账号脱敏
    @ApiModelProperty(value = "账号")
    private String account;

    /** 密码 */
    @Desensitization(rule = "3_4") // 密码脱敏
    @ApiModelProperty(value = "密码")
    private String password;

    /** 邮箱 */
    @Desensitization(rule = "3~@") // 邮箱脱敏
    @ApiModelProperty(value = "邮箱")
    private String email;

    /** 手机号 */
    @Desensitization(rule = "3_4") // 手机号脱敏
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /** 性别 0未知 1男 2女 */
    @ApiModelProperty(value = "性别 0未知 1男 2女")
    private Integer sex;

    /** 头像 */
    @ApiModelProperty(value = "头像")
    private String avatar;

    /** 生日 */
    @ApiModelProperty(value = "生日")
    private LocalDate birthday;

    /** 用户角色 */
    @ApiModelProperty(value = "用户角色")
    private List<SysRoleDTO> roles;

}
