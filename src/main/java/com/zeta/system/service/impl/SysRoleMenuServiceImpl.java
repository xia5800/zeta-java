package com.zeta.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeta.common.cacheKey.SaPermissionStringCacheKey;
import com.zeta.common.cacheKey.SaRoleStringCacheKey;
import com.zeta.system.dao.SysRoleMenuMapper;
import com.zeta.system.model.entity.SysMenu;
import com.zeta.system.model.entity.SysRoleMenu;
import com.zeta.system.model.entity.SysUserRole;
import com.zeta.system.service.ISysRoleMenuService;
import com.zeta.system.service.ISysUserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色菜单关联 服务实现类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:01
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    private final ISysUserRoleService userRoleService;
    private final SaRoleStringCacheKey saRoleStringCacheKey;
    private final SaPermissionStringCacheKey saPermissionStringCacheKey;

    /**
     * 查询用户对应的菜单
     *
     * @param userId   用户id
     * @param menuType 菜单类型
     * @return List<SysMenu>
     */
    @Override
    public List<SysMenu> listMenuByUserId(Long userId, String menuType) {
        return baseMapper.listMenuByUserId(userId, menuType);
    }

    /**
     * 根据角色id查询菜单
     *
     * @param roleIds  角色id
     * @param menuType 菜单类型
     * @return List<SysMenu>
     */
    @Override
    public List<SysMenu> listMenuByRoleIds(List<Long> roleIds, String menuType) {
        return baseMapper.listMenuByRoleIds(roleIds, menuType);
    }

    /**
     * 删除用户角色、权限缓存
     *
     * @param roleId Long
     */
    @Override
    public void clearUserCache(Long roleId) {
        List<SysUserRole> userRoleList = userRoleService.list(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId, roleId)
        );
        if (userRoleList.isEmpty()) return;

        List<Long> userIds = userRoleList.stream()
                .map(SysUserRole::getUserId)
                .collect(Collectors.toList());
        for (Long userId : userIds) {
            // 删除用户权限缓存
            saPermissionStringCacheKey.delete(userId);
            // 删除用户角色缓存
            saRoleStringCacheKey.delete(userId);
        }
    }
}
