package com.zeta.system.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeta.system.dao.SysUserMapper;
import com.zeta.system.model.dto.sysRole.SysRoleDTO;
import com.zeta.system.model.dto.sysUser.SysUserDTO;
import com.zeta.system.model.dto.sysUser.SysUserSaveDTO;
import com.zeta.system.model.dto.sysUser.SysUserUpdateDTO;
import com.zeta.system.model.entity.SysMenu;
import com.zeta.system.model.entity.SysRole;
import com.zeta.system.model.entity.SysUser;
import com.zeta.system.model.enums.UserStateEnum;
import com.zeta.system.model.param.SysUserQueryParam;
import com.zeta.system.service.ISysRoleMenuService;
import com.zeta.system.service.ISysUserRoleService;
import com.zeta.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zetaframework.base.param.PageParam;
import org.zetaframework.base.result.PageResult;
import org.zetaframework.core.constants.RedisKeyConstants;
import org.zetaframework.core.exception.BusinessException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户 服务实现类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:51
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService, StpInterface {

    private final ISysUserRoleService userRoleService;
    private final ISysRoleMenuService roleMenuService;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 返回指定账号id所拥有的权限码集合
     *
     * @param loginId  账号id
     * @param loginType 账号类型
     * @return 该账号id具有的权限码集合
     */
    @Cacheable(value = RedisKeyConstants.USER_PERMISSION_KEY, key = "#p0")
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        if (ObjectUtil.isNull(loginId)) return Collections.emptyList();

        List<SysMenu> authorities = roleMenuService.listMenuByUserId(Long.parseLong(loginId.toString()));
        if (CollUtil.isEmpty(authorities)) return Collections.emptyList();

        // 筛选出 authority 不为空的数据
        return authorities.stream()
                .map(SysMenu::getAuthority)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
    }

    /**
     * 返回指定账号id所拥有的角色标识集合
     *
     * @param loginId  账号id
     * @param loginType 账号类型
     * @return 该账号id具有的角色标识集合
     */
    @Cacheable(value = RedisKeyConstants.USER_ROLE_KEY, key = "#p0")
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        if (ObjectUtil.isNull(loginId)) return Collections.emptyList();

        List<SysRole> roleList = userRoleService.listByUserId(Long.parseLong(loginId.toString()));
        if (CollUtil.isEmpty(roleList)) return Collections.emptyList();

        // 筛选出 code 不为空的数据
        return roleList.stream()
                .map(SysRole::getCode)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
    }

    /**
     * 自定义分页查询
     *
     * @param param 分页查询参数
     * @return PageResult<SysUserDTO>
     */
    @Override
    public PageResult<SysUserDTO> customPage(PageParam<SysUserQueryParam> param) {
        // 构造分页page
        IPage<SysUser> page = param.buildPage();

        // 构造查询条件
        SysUserQueryParam model = Optional.ofNullable(param.getModel())
                .orElse(new SysUserQueryParam());
        SysUser entity = BeanUtil.toBean(model, SysUser.class);

        // 分页查询
        page = this.page(page, new LambdaQueryWrapper<>(entity));

        // 批量获取用户角色 Map<用户id, 用户角色列表>
        List<Long> userIds = page.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
        Map<Long, List<SysRoleDTO>> userRoleMap = userIds.isEmpty() ? Collections.emptyMap() : this.getUserRoles(userIds);

        // 处理返回结果
        List<SysUserDTO> result = page.getRecords().stream().map(user -> {
            // 设置用户角色
            user.setRoles(userRoleMap.getOrDefault(user.getId(), Collections.emptyList()));
            // Entity -> EntityDTO
            return BeanUtil.toBean(user, SysUserDTO.class);
        }).collect(Collectors.toList());

        return new PageResult<>(result, page.getTotal());
    }

    /**
     * 添加用户
     *
     * @param saveDTO SysUserSaveDTO
     * @return Boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean saveUser(SysUserSaveDTO saveDTO) {
        SysUser user = BeanUtil.toBean(saveDTO, SysUser.class);
        user.setPassword(encodePassword(saveDTO.getPassword()));
        user.setReadonly(false);
        user.setState(UserStateEnum.NORMAL.getCode());
        if (!this.save(user)) {
            throw new BusinessException("新增用户失败");
        }

        // 删除并重新关联角色
        return userRoleService.saveUserRole(user.getId(), saveDTO.getRoleIds());
    }

    /**
     * 修改用户
     *
     * @param updateDTO SysUserUpdateDTO
     * @return Boolean
     */
    @CacheEvict(value = {RedisKeyConstants.USER_PERMISSION_KEY, RedisKeyConstants.USER_ROLE_KEY}, key = "#updateDTO.id")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateUser(SysUserUpdateDTO updateDTO) {
        SysUser user = BeanUtil.toBean(updateDTO, SysUser.class);
        if (!this.updateById(user)) {
            throw new BusinessException("修改用户失败");
        }

        // 删除并重新关联角色
        return userRoleService.saveUserRole(user.getId(), updateDTO.getRoleIds());
    }

    /**
     * 修改用户基本信息
     *
     * @param changeUser SysUser 待修改的用户信息
     * @return
     */
    @CacheEvict(value = {RedisKeyConstants.USER_PERMISSION_KEY, RedisKeyConstants.USER_ROLE_KEY}, key = "#changeUser.id")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateUserBaseInfo(SysUser changeUser) {
        if (!this.updateById(changeUser)) {
            throw new BusinessException("修改用户失败");
        }
        return true;
    }

    /**
     * 获取用户角色
     *
     * @param userId Long
     * @return List<SysRole>
     */
    @Override
    public List<SysRoleDTO> getUserRoles(Long userId) {
        // 根据用户id查询角色
        List<SysRole> roleList = userRoleService.listByUserId(userId);
        if (roleList.isEmpty()) return Collections.emptyList();

        // List<Entity> -> List<EntityDTO>
        return roleList.stream()
                .map(it -> BeanUtil.toBean(it, SysRoleDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * 批量获取用户角色
     *
     * @param userIds List<Long>
     * @return Map<用户id, 用户角色列表>
     */
    @Override
    public Map<Long, List<SysRoleDTO>> getUserRoles(List<Long> userIds) {
        // 批量根据用户id查询角色
        List<SysRoleDTO> roleList = userRoleService.listByUserIds(userIds);
        if (roleList.isEmpty()) return Collections.emptyMap();

        // 处理返回值, 得到 Map<用户id, 用户角色列表>
        return roleList.stream()
                .filter(it -> it.getUserId() != null)
                .collect(Collectors.groupingBy(SysRoleDTO::getUserId));
    }

    /**
     * 通过账号查询用户
     *
     * @param account String
     * @return User
     */
    @Override
    public SysUser getByAccount(String account) {
        return this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getAccount, account)
        );
    }

    /**
     * 加密用户密码
     *
     * @param password String 明文
     * @return String   密文
     */
    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * 比较密码
     *
     * @param inputPwd String 用户输入的密码
     * @param dbPwd    String    用户数据库中的密码
     * @return Boolean
     */
    @Override
    public Boolean comparePassword(String inputPwd, String dbPwd) {
        return passwordEncoder.matches(inputPwd, dbPwd);
    }

    /**
     * 批量导入用户
     *
     * @param userList 待导入的用户列表
     * @return Boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean batchImportUser(List<SysUser> userList) {
        // 保存用户
        if (!this.saveBatch(userList)) {
            throw new BusinessException("新增用户失败");
        }

        try {
            // 筛选出有角色的用户
            userList.stream().filter(it -> CollUtil.isNotEmpty(it.getRoles())).forEach(user -> {
                // 删除并重新关联角色
                List<Long> roleIds = user.getRoles().stream()
                        .map(SysRoleDTO::getId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                userRoleService.saveUserRole(user.getId(), roleIds);
            });
        } catch (Exception e) {
            throw new BusinessException("关联用户角色失败");
        }

        return true;
    }

}
