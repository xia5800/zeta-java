package com.zeta.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zeta.system.model.dto.sysRole.SysRoleDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.zetaframework.base.entity.StateEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * 用户
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户")
@TableName("sys_user")
public class SysUser extends StateEntity<Long, Integer> {

    private static final long serialVersionUID = 1L;

    /** 用户名 */
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Size(max = 32, message = "用户名长度不能超过32")
    @TableField(value = "username")
    private String username;

    /** 账号 */
    @ApiModelProperty(value = "账号", required = true)
    @NotBlank(message = "账号不能为空")
    @Size(max = 64, message = "账号长度不能超过64")
    @TableField(value = "account")
    private String account;

    /** 密码 */
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Size(max = 64, message = "密码长度不能超过64")
    @TableField(value = "password")
    private String password;

    /** 邮箱 */
    @ApiModelProperty(value = "邮箱", required = false)
    @TableField(value = "email")
    private String email;

    /** 手机号 */
    @ApiModelProperty(value = "手机号", required = false)
    @TableField(value = "mobile")
    private String mobile;

    /** 性别 0未知 1男 2女 */
    @ApiModelProperty(value = "性别 0未知 1男 2女", required = true)
    @NotNull(message = "性别不能为空")
    @TableField(value = "sex")
    private Integer sex;

    /** 头像 */
    @ApiModelProperty(value = "头像", required = false)
    @TableField(value = "avatar")
    private String avatar;

    /** 生日 */
    @ApiModelProperty(value = "生日", required = false)
    @TableField(value = "birthday")
    private LocalDate birthday;

    /** 内置 */
    @ApiModelProperty(value = "内置", required = true)
    @TableField(value = "readonly_")
    private Boolean readonly;

    /** 逻辑删除字段 */
    @JsonIgnore
    @ApiModelProperty(value = "逻辑删除字段", required = true)
    @TableLogic
    private Boolean deleted;

    /** 用户角色 */
    @ApiModelProperty(value = "用户角色", required = false)
    @TableField(exist = false)
    private List<SysRoleDTO> roles;


    public SysUser(Long id, String username, String account, String password,  Integer sex, Integer state, Boolean readonly) {
        this.id = id;
        this.username = username;
        this.account = account;
        this.password = password;
        this.sex = sex;
        this.state = state;
        this.readonly = readonly;
    }
}
