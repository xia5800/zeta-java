package com.zeta.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeta.system.model.entity.SysRole;

import java.util.List;

/**
 * 角色 服务类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 通过角色名查询角色
     *
     * @param name 角色名
     * @return 角色名对应的角色
     */
    SysRole getRoleByName(String name);

    /**
     * 通过角色名查询角色
     *
     * @param names 角色名列表
     * @return 角色名对应的角色
     */
    List<SysRole> getRolesByNames(List<String> names);

    /**
     * 通过角色编码查询角色
     *
     * @param code 角色编码
     * @return 角色编码对应的角色
     */
    SysRole getRoleByCode(String code);

    /**
     * 通过角色编码查询角色
     *
     * @param codes 角色编码列表
     * @return 角色编码对应的角色
     */
    List<SysRole> getRolesByCodes(List<String> codes);
}
