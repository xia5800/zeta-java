package com.zeta.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeta.system.model.dto.sysRole.SysRoleDTO;
import com.zeta.system.model.entity.SysRole;
import com.zeta.system.model.entity.SysUserRole;

import java.util.List;

/**
 * 用户角色 服务类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:30
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 根据用户id查询角色
     *
     * @param userId 用户id
     * @return List<SysRole>
     */
    List<SysRole> listByUserId(Long userId);

    /**
     * 批量根据用户id查询角色
     *
     * @param userIds 用户id列表
     * @return List<RoleResult>
     */
    List<SysRoleDTO> listByUserIds(List<Long> userIds);


    /**
     * 关联用户角色
     *
     * @param userId 用户id
     * @param roleIds 角色id列表
     * @return 是否成功
     */
    Boolean saveUserRole(Long userId, List<Long> roleIds);

    /**
     * 关联用户角色
     *
     * @param userId 用户id
     * @param roleId 角色id
     * @return 是否成功
     */
    Boolean saveUserRole(Long userId, Long roleId);

}
