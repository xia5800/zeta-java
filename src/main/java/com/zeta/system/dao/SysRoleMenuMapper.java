package com.zeta.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeta.system.model.entity.SysMenu;
import com.zeta.system.model.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色菜单关联 Mapper 接口
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:01
 */
@Repository
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * 查询用户对应的菜单
     *
     * @param userId Long
     * @param menuType String
     * @return List<Menu>
     */
    List<SysMenu> listMenuByUserId(@Param("userId") Long userId,
                                   @Param("menuType") String menuType);

    /**
     * 根据角色id查询菜单
     *
     * @param roleIds   角色id
     * @param menuType  菜单类型
     * @return List<Menu>
     */
    List<SysMenu> listMenuByRoleIds(@Param("roleIds") List<Long> roleIds,
                                    @Param("menuType") String menuType);

}
