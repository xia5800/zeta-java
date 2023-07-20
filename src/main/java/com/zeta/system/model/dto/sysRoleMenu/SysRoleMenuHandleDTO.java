package com.zeta.system.model.dto.sysRoleMenu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量新增、修改角色菜单关联关系
 *
 * @author gcc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "批量新增、修改角色菜单关联关系")
public class SysRoleMenuHandleDTO {

    /** 角色id */
    @ApiModelProperty(value = "角色id", required = true)
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    /** 菜单id列表 为空代表清空角色与菜单的关联 */
    @ApiModelProperty(value = "菜单id列表 为空代表清空角色与菜单的关联", required = false)
    private List<Long> menuIds;

}
