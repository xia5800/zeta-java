package com.zeta.system.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zeta.system.model.dto.sysRoleMenu.SysRoleMenuHandleDTO;
import com.zeta.system.model.entity.SysMenu;
import com.zeta.system.model.entity.SysRoleMenu;
import com.zeta.system.service.ISysMenuService;
import com.zeta.system.service.ISysRoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zetaframework.base.controller.SuperSimpleController;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.satoken.annotation.PreAuth;
import org.zetaframework.core.satoken.annotation.PreCheckPermission;
import org.zetaframework.core.satoken.annotation.PreMode;
import org.zetaframework.core.utils.TreeUtil;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色菜单关联 前端控制器
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:01
 */
@RequiredArgsConstructor
@Api(tags = "角色菜单")
@PreAuth(replace = "sys:role")
@RestController
@RequestMapping("/api/system/roleMenu")
public class SysRoleMenuController extends SuperSimpleController<ISysRoleMenuService, SysRoleMenu> {

    private final ISysMenuService menuService;

    /**
     * 查询角色菜单树
     *
     * 说明：
     * 用于前端角色管理查询角色对应的菜单树。
     * @param roleId Long   角色id
     * @return ApiResult<List<SysMenu>>
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "查询角色菜单")
    @GetMapping("/{roleId}")
    public ApiResult<List<SysMenu>> list(@PathVariable("roleId") @ApiParam("角色id") Long roleId) {
        List<SysMenu> menuList = menuService.list(new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSortValue));
        if (menuList.isEmpty()) return success(Collections.emptyList());

        List<SysRoleMenu> roleMenuList = service.list(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        for (SysMenu menu : menuList) {
            boolean checked = roleMenuList.stream().anyMatch(it -> Objects.equals(it.getMenuId(), menu.getId()));
            menu.setChecked(checked);
        }

        return success(TreeUtil.buildTree(menuList));
    }

    /**
     * 新增或修改
     *
     * @param handleDTO SysRoleMenuHandleDTO 批量新增、修改角色菜单关联关系参数
     * @return ApiResult<Boolean>
     */
    @PreCheckPermission(value = {"{}:edit", "{}:update"}, mode = PreMode.OR)
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "新增或修改")
    @PutMapping
    public ApiResult<Boolean> update(@RequestBody @Validated SysRoleMenuHandleDTO handleDTO) {
        // 修改前先删除角色所有权限
        service.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, handleDTO.getRoleId()));

        // 重新关联角色权限
        if (CollUtil.isNotEmpty(handleDTO.getMenuIds())) {
            List<SysRoleMenu> batchList = handleDTO.getMenuIds()
                    .stream()
                    .map(it -> new SysRoleMenu(handleDTO.getRoleId(), it))
                    .collect(Collectors.toList());
            if (!service.saveBatch(batchList)) return fail("操作失败");
        }

        // 删除用户角色、权限缓存
        service.clearUserCache(handleDTO.getRoleId());
        return success(true);
    }
}

