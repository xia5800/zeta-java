package com.zeta.system.model.dto.sysRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色 详情
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "角色详情")
public class SysRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 角色id */
    @ApiModelProperty(value = "角色id")
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

    /** 角色名 */
    @ApiModelProperty(value = "角色名")
    private String name;

    /** 角色编码 */
    @ApiModelProperty(value = "角色编码")
    private String code;

    /** 描述 */
    @ApiModelProperty(value = "描述")
    private String describe;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    private Long userId;

}
