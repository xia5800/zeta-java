package com.zeta.system.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户 查询参数
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
@ApiModel(description = "用户查询参数")
public class SysUserQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id")
    private Long id;


    /** 用户名 */
    @ApiModelProperty(value = "用户名")
    private String username;

    /** 账号 */
    @ApiModelProperty(value = "账号")
    private String account;

    /** 邮箱 */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /** 手机号 */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /** 性别 0未知 1男 2女 */
    @ApiModelProperty(value = "性别 0未知 1男 2女")
    private Integer sex;

    /** 状态 */
    @ApiModelProperty(value = "状态")
    private Integer state;

}
