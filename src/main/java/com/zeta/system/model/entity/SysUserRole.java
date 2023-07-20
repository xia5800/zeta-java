package com.zeta.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.zetaframework.base.entity.SuperEntity;

import javax.validation.constraints.NotNull;

/**
 * 用户角色
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户角色")
@TableName("sys_user_role")
public class SysUserRole extends SuperEntity<Long> {

    private static final long serialVersionUID = 1L;

    /** 用户id */
    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "用户id不能为空")
    @TableField(value = "user_id")
    private Long userId;

    /** 角色id */
    @ApiModelProperty(value = "角色id", required = true)
    @NotNull(message = "角色id不能为空")
    @TableField(value = "role_id")
    private Long roleId;
}
