package com.zeta.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeta.system.model.entity.SysMenu;
import com.zeta.system.model.entity.SysRoleMenu;

import java.util.List;

/**
 * 角色菜单关联 服务类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:01
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 查询用户对应的菜单
     *
     * @param userId    用户id
     * @return List<SysMenu>
     */
    default List<SysMenu> listMenuByUserId(Long userId) {
        return listMenuByUserId(userId, null);
    }

    /**
     * 查询用户对应的菜单
     *
     * @param userId    用户id
     * @param menuType  菜单类型
     * @return List<SysMenu>
     */
    List<SysMenu> listMenuByUserId(Long userId, String menuType);

    /**
     * 根据角色id查询菜单
     *
     * @param roleIds   角色id
     * @return List<SysMenu>
     */
    default List<SysMenu> listMenuByRoleIds(List<Long> roleIds) {
        return listMenuByRoleIds(roleIds, null);
    }

    /**
     * 根据角色id查询菜单
     *
     * @param roleIds   角色id
     * @param menuType  菜单类型
     * @return List<SysMenu>
     */
    List<SysMenu> listMenuByRoleIds(List<Long> roleIds, String menuType);

    /**
     * 删除用户角色、权限缓存
     *
     * @param roleId Long
     */
    void clearUserCache(Long roleId);

}
