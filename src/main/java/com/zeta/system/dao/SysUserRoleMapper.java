package com.zeta.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeta.system.model.dto.sysRole.SysRoleDTO;
import com.zeta.system.model.entity.SysRole;
import com.zeta.system.model.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色 Mapper 接口
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:30
 */
@Repository
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 根据用户id查询角色
     *
     * @param userId 用户id
     * @return List<Role>
     */
    List<SysRole> selectByUserId(@Param("userId") Long userId);

    /**
     * 批量根据用户id查询角色
     *
     * @param userIds 用户id集合
     * @return List<RoleResult>
     */
    List<SysRoleDTO> selectByUserIds(@Param("userIds") List<Long> userIds);

}
