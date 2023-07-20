package com.zeta.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeta.system.dao.SysUserRoleMapper;
import com.zeta.system.model.dto.sysRole.SysRoleDTO;
import com.zeta.system.model.entity.SysRole;
import com.zeta.system.model.entity.SysUserRole;
import com.zeta.system.service.ISysUserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色 服务实现类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:30
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    /**
     * 根据用户id查询角色
     *
     * @param userId 用户id
     * @return List<SysRole>
     */
    @Override
    public List<SysRole> listByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    /**
     * 批量根据用户id查询角色
     *
     * @param userIds 用户id列表
     * @return List<RoleResult>
     */
    @Override
    public List<SysRoleDTO> listByUserIds(List<Long> userIds) {
        return baseMapper.selectByUserIds(userIds);
    }

    /**
     * 关联用户角色
     *
     * @param userId 用户id
     * @param roleIds 角色id列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean saveUserRole(Long userId, List<Long> roleIds) {
        // 删除用户角色关联
        this.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));

        if (roleIds == null || roleIds.isEmpty()) {
            return true;
        }

        // 批量关联
        List<SysUserRole> batchList = roleIds.stream()
                .map(it -> new SysUserRole(userId, it))
                .collect(Collectors.toList());
        return this.saveBatch(batchList);
    }

    /**
     * 关联用户角色
     *
     * @param userId 用户id
     * @param roleId 角色id
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean saveUserRole(Long userId, Long roleId) {
        return this.save(new SysUserRole(userId, roleId));
    }
}
