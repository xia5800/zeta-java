package com.zeta.system.model.dto.sysUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户信息
 *
 * @author gcc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户信息")
public class UserInfoDTO {

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    private Long id;

    /** 用户名 */
    @ApiModelProperty(value = "用户名")
    private String username;

    /** 账号 */
    @ApiModelProperty(value = "账号")
    private String account;

    /** 性别 */
    @ApiModelProperty(value = "性别 0未知 1男 2女", example = "0", allowableValues = "0,1,2")
    private Integer sex;

    /** 头像 */
    @ApiModelProperty(value = "头像")
    private String avatar;

    /** 状态 */
    @ApiModelProperty(value = "状态")
    private Integer state;

    /** 角色列表 */
    @ApiModelProperty(value = "角色列表")
    private List<String> roles;

    /** 权限列表 */
    @ApiModelProperty(value = "权限列表")
    private List<String> permissions;
    
}
