package com.zeta.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeta.system.dao.SysRoleMapper;
import com.zeta.system.model.entity.SysRole;
import com.zeta.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 角色 服务实现类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    /**
     * 通过角色名查询角色
     *
     * @param name 角色名
     * @return 角色名对应的角色
     */
    @Override
    public SysRole getRoleByName(String name) {
        return this.getOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getName, name)
                .orderByDesc(SysRole::getId)
        );
    }

    /**
     * 通过角色名查询角色
     *
     * @param names 角色名列表
     * @return 角色名对应的角色
     */
    @Override
    public List<SysRole> getRolesByNames(List<String> names) {
        if (names.isEmpty()) return Collections.emptyList();

        return this.list(new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getName, names)
        );
    }

    /**
     * 通过角色编码查询角色
     *
     * @param code 角色编码
     * @return 角色编码对应的角色
     */
    @Override
    public SysRole getRoleByCode(String code) {
        return this.getOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getCode, code)
                .orderByDesc(SysRole::getId)
        );
    }

    /**
     * 通过角色编码查询角色
     *
     * @param codes 角色编码列表
     * @return 角色编码对应的角色
     */
    @Override
    public List<SysRole> getRolesByCodes(List<String> codes) {
        if (codes.isEmpty()) return Collections.emptyList();

        return this.list(new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getCode, codes)
        );
    }
}
