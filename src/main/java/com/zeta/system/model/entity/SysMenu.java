package com.zeta.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zeta.system.model.enums.MenuTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.zetaframework.base.entity.TreeEntity;

import javax.validation.constraints.NotBlank;

/**
 * 菜单
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:51:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "菜单")
@TableName("sys_menu")
public class SysMenu extends TreeEntity<SysMenu, Long> {

    private static final long serialVersionUID = 1L;

    /** 路由名称 */
    @ApiModelProperty(value = "路由名称", required = false)
    @TableField(value = "name")
    private String name;

    /** 路由地址 */
    @ApiModelProperty(value = "路由地址", required = false)
    @TableField(value = "path")
    private String path;

    /** 组件地址 */
    @ApiModelProperty(value = "组件地址", required = false)
    @TableField(value = "component")
    private String component;

    /** 重定向地址 */
    @ApiModelProperty(value = "重定向地址", required = false)
    @TableField(value = "redirect")
    private String redirect;

    /** 图标 */
    @ApiModelProperty(value = "图标", required = false)
    @TableField(value = "icon")
    private String icon;

    /** 权限标识 */
    @ApiModelProperty(value = "权限标识", required = false)
    @TableField(value = "authority")
    private String authority;

    /** 菜单类型 */
    @ApiModelProperty(value = "菜单类型", required = true)
    @NotBlank(message = "菜单类型不能为空")
    @TableField(value = "type")
    private MenuTypeEnum type;

    /** 逻辑删除字段 */
    @JsonIgnore
    @ApiModelProperty(value = "逻辑删除字段", hidden = true, required = true)
    @TableLogic
    private Boolean deleted;

    /** 是否隐藏 0否 1是 */
    @ApiModelProperty(value = "是否隐藏 0否 1是", required = false)
    @TableField(value = "hide")
    private Boolean hide;

    /** 是否缓存 0否 1是 */
    @ApiModelProperty(value = "是否缓存 0否 1是", required = false)
    @TableField(value = "keep_alive")
    private Boolean keepAlive;

    /** 外链地址 */
    @ApiModelProperty(value = "外链地址", required = false)
    @TableField(value = "href")
    private String href;

    /** 内链地址 */
    @ApiModelProperty(value = "内链地址", required = false)
    @TableField(value = "frame_src")
    private String frameSrc;

    /** 角色权限树选中状态 */
    @ApiModelProperty(value = "角色权限树选中状态", required = false)
    @TableField(exist = false)
    private Boolean checked;
}
