package com.zeta.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeta.system.model.dto.sysRole.SysRoleDTO;
import com.zeta.system.model.dto.sysUser.SysUserDTO;
import com.zeta.system.model.dto.sysUser.SysUserSaveDTO;
import com.zeta.system.model.dto.sysUser.SysUserUpdateDTO;
import com.zeta.system.model.entity.SysUser;
import com.zeta.system.model.param.SysUserQueryParam;
import org.zetaframework.base.param.PageParam;
import org.zetaframework.base.result.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 用户 服务类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:51
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 自定义分页查询
     *
     * @param param 分页查询参数
     * @return PageResult<SysUserDTO>
     */
    PageResult<SysUserDTO> customPage(PageParam<SysUserQueryParam> param);

    /**
     * 添加用户
     *
     * @param saveDTO SysUserSaveDTO
     * @return Boolean
     */
    Boolean saveUser(SysUserSaveDTO saveDTO);

    /**
     * 修改用户
     *
     * @param updateDTO SysUserUpdateDTO
     * @return Boolean
     */
    Boolean updateUser(SysUserUpdateDTO updateDTO);


    /**
     * 修改用户基本信息
     *
     * @param changeUser SysUser 待修改的用户信息
     * @return
     */
    Boolean updateUserBaseInfo(SysUser changeUser);

    /**
     * 获取用户角色
     *
     * @param userId Long
     * @return List<SysRole>
     */
    List<SysRoleDTO> getUserRoles(Long userId);

    /**
     * 批量获取用户角色
     *
     * @param userIds List<Long>
     * @return Map<用户id, 用户角色列表>
     */
    Map<Long, List<SysRoleDTO>> getUserRoles(List<Long> userIds);

    /**
     * 通过账号查询用户
     *
     * @param account String
     * @return User
     */
    SysUser getByAccount(String account);

    /**
     * 加密用户密码
     *
     * @param password String 明文
     * @return String   密文
     */
    String encodePassword(String password);


    /**
     * 比较密码
     *
     * @param inputPwd String 用户输入的密码
     * @param dbPwd String    用户数据库中的密码
     * @return Boolean
     */
    Boolean comparePassword(String inputPwd, String dbPwd);

    /**
     * 批量导入用户
     *
     * @param userList 待导入的用户列表
     * @return Boolean
     */
    Boolean batchImportUser(List<SysUser> userList);

}
