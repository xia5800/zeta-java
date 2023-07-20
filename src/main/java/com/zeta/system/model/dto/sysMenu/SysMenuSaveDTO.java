package com.zeta.system.model.dto.sysMenu;

import com.zeta.system.model.enums.MenuTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 新增 菜单
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:51:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "新增菜单")
public class SysMenuSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 名称 */
    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 32, message = "菜单名称长度不能超过32")
    private String label;

    /** 父级id */
    @ApiModelProperty(value = "父级id", required = true)
    @NotNull(message = "父级id不能为空")
    private Long parentId;

    /** 排序 */
    @ApiModelProperty(value = "排序", required = false)
    private Integer sortValue;

    /** 路由名称 */
    @ApiModelProperty(value = "路由名称", required = true)
    @NotBlank(message = "路由名称不能为空")
    private String name;

    /** 路由地址 */
    @ApiModelProperty(value = "路由地址", required = false)
    private String path;

    /** 组件地址 */
    @ApiModelProperty(value = "组件地址", required = false)
    private String component;

    /** 重定向地址 */
    @ApiModelProperty(value = "重定向地址", required = false)
    private String redirect;

    /** 图标 */
    @ApiModelProperty(value = "图标", required = false)
    private String icon;

    /** 权限标识 */
    @ApiModelProperty(value = "权限标识", required = false)
    private String authority;

    /** 菜单类型 */
    @ApiModelProperty(value = "菜单类型", required = true)
    @NotNull(message = "菜单类型不能为空")
    private MenuTypeEnum type;

    /** 是否隐藏 0否 1是 */
    @ApiModelProperty(value = "是否隐藏 0否 1是", required = false)
    private Boolean hide;

    /** 是否缓存 0否 1是 */
    @ApiModelProperty(value = "是否缓存 0否 1是", required = false)
    private Boolean keepAlive;

    /** 外链地址 */
    @ApiModelProperty(value = "外链地址", required = false)
    private String href;

    /** 内链地址 */
    @ApiModelProperty(value = "内链地址", required = false)
    private String frameSrc;

}
