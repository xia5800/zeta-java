package com.zeta.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zeta.system.model.dto.sysMenu.SysMenuSaveDTO;
import com.zeta.system.model.dto.sysMenu.SysMenuUpdateDTO;
import com.zeta.system.model.entity.SysMenu;
import com.zeta.system.model.param.SysMenuQueryParam;
import com.zeta.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zetaframework.base.controller.SuperController;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.log.annotation.SysLog;
import org.zetaframework.core.saToken.annotation.PreAuth;
import org.zetaframework.core.utils.TreeUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 菜单 前端控制器
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:51:47
 */
@Api(tags = "菜单")
@PreAuth(replace = "sys:menu")
@RestController
@RequestMapping("/api/system/menu")
public class SysMenuController extends SuperController<ISysMenuService, Long, SysMenu, SysMenuQueryParam, SysMenuSaveDTO, SysMenuUpdateDTO> {

    /**
     * 自定义批量查询
     *
     * @param param QueryParam
     * @return List<Entity>
     */
    @Override
    public List<SysMenu> handlerBatchQuery(SysMenuQueryParam param) {
        SysMenu entity = BeanUtil.toBean(param, getEntityClass());

        // 批量查询
        List<SysMenu> list = service.list(
                new LambdaQueryWrapper<SysMenu>(entity)
                        .orderByAsc(Arrays.asList(SysMenu::getSortValue, SysMenu::getId))
        );

        // 处理批量查询数据
        super.handlerBatchData(list);
        return list;
    }

    /**
     * 查询菜单树
     *
     * @param param SysMenuQueryParam
     * @return List<Menu>
     */
    @ApiOperationSupport(ignoreParameters = "children")
    @ApiOperation(value = "查询菜单树")
    @SysLog(response = false)
    @PostMapping("/tree")
    public ApiResult<List<SysMenu>> tree(@RequestBody SysMenuQueryParam param) {
        // 查询所有菜单
        List<SysMenu> menuList = super.handlerBatchQuery(param);
        if (menuList.isEmpty()) return success(Collections.emptyList());

        // 转换成树形结构
        List<SysMenu> sysMenus = TreeUtil.buildTree(menuList, false);
        return success(sysMenus);
    }
}

