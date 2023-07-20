package com.zeta.system.model.dto.sysMenu;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 路由描述
 *
 * @author gcc
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "路由描述")
public class RouteMeta {

    /** 路由标题 */
    @ApiModelProperty(value = "路由标题")
    private String title;

    /** 菜单和面包屑对应的图标 */
    @ApiModelProperty(value = "菜单和面包屑对应的图标")
    private String icon;

    /** 是否在菜单中隐藏 */
    @ApiModelProperty(value = "是否在菜单中隐藏")
    private Boolean hide;

    /** 是否缓存 */
    @ApiModelProperty(value = "是否缓存")
    private Boolean keepAlive;

    /** 外链地址 */
    @ApiModelProperty(value = "外链链接")
    private String href;

    /** 内链地址 */
    @ApiModelProperty(value = "内链地址")
    private String frameSrc;

}
