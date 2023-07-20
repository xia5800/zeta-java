package com.zeta.system.model.dto.sysMenu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zeta.system.model.entity.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.zetaframework.base.entity.TreeEntity;

import java.util.List;

/**
 * 前端路由
 *
 * 说明：
 * 内含转换方法，将系统菜单转换成前端路由
 * @author gcc
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "前端路由")
public class FrontRoute extends TreeEntity<FrontRoute, Long> {

    /** id */
    @ApiModelProperty(value = "路由id")
    @Override
    public Long getId() {
        return super.getId();
    }

    /** 子路由 */
    @ApiModelProperty(value = "子路由")
    @Override
    public List<FrontRoute> getChildren() {
        return super.getChildren();
    }


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

    /** 路由元数据 */
    @ApiModelProperty(value = "路由元数据")
    private RouteMeta meta;


    /**
     * 将菜单转换成前端路由
     *
     * @param sysMenu 系统菜单
     * @return 前端路由
     */
    public static FrontRoute convert(SysMenu sysMenu) {
        RouteMeta meta = RouteMeta.builder()
                .title(sysMenu.getLabel())
                .icon(sysMenu.getIcon())
                .hide(sysMenu.getHide())
                .keepAlive(sysMenu.getKeepAlive())
                .href(sysMenu.getHref())
                .frameSrc(sysMenu.getFrameSrc())
                .build();

        FrontRoute frontRoute = new FrontRoute();
        frontRoute.setId(sysMenu.getId());
        frontRoute.setName(sysMenu.getName());
        frontRoute.setPath(sysMenu.getPath());
        frontRoute.setComponent(sysMenu.getComponent());
        frontRoute.setRedirect(sysMenu.getRedirect());
        frontRoute.setParentId(sysMenu.getParentId());
        frontRoute.setMeta(meta);
        return frontRoute;
    }
}
