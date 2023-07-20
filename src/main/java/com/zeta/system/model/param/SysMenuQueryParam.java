package com.zeta.system.model.param;

import com.zeta.system.model.enums.MenuTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单 查询参数
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
@ApiModel(description = "菜单查询参数")
public class SysMenuQueryParam implements Serializable {

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

    /** 名称 */
    @ApiModelProperty(value = "名称")
    private String label;

    /** 父级id */
    @ApiModelProperty(value = "父级id")
    private Long parentId;

    /** 排序 */
    @ApiModelProperty(value = "排序")
    private Integer sortValue;

    /** 路由名称 */
    @ApiModelProperty(value = "路由名称")
    private String name;

    /** 路由地址 */
    @ApiModelProperty(value = "路由地址")
    private String path;

    /** 组件地址 */
    @ApiModelProperty(value = "组件地址")
    private String component;

    /** 重定向地址 */
    @ApiModelProperty(value = "重定向地址")
    private String redirect;

    /** 图标 */
    @ApiModelProperty(value = "图标")
    private String icon;

    /** 权限标识 */
    @ApiModelProperty(value = "权限标识")
    private String authority;

    /** 菜单类型 */
    @ApiModelProperty(value = "菜单类型")
    private MenuTypeEnum type;

    /** 是否隐藏 0否 1是 */
    @ApiModelProperty(value = "是否隐藏 0否 1是")
    private Boolean hide;

    /** 是否缓存 0否 1是 */
    @ApiModelProperty(value = "是否缓存 0否 1是")
    private Boolean keepAlive;

    /** 外链地址 */
    @ApiModelProperty(value = "外链地址")
    private String href;

    /** 内链地址 */
    @ApiModelProperty(value = "内链地址")
    private String frameSrc;
}
