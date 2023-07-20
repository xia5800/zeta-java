package com.zeta.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.zetaframework.base.entity.SuperEntity;

import javax.validation.constraints.NotNull;

/**
 * 角色菜单关联
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "角色菜单关联")
@TableName("sys_role_menu")
public class SysRoleMenu extends SuperEntity<Long> {

    private static final long serialVersionUID = 1L;

    /** 角色id */
    @ApiModelProperty(value = "角色id", required = true)
    @NotNull(message = "角色id不能为空")
    @TableField(value = "role_id")
    private Long roleId;

    /** 菜单id */
    @ApiModelProperty(value = "菜单id", required = true)
    @NotNull(message = "菜单id不能为空")
    @TableField(value = "menu_id")
    private Long menuId;
}
