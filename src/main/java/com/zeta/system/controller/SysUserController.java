package com.zeta.system.controller;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zeta.system.model.dto.sysMenu.FrontRoute;
import com.zeta.system.model.dto.sysRole.SysRoleDTO;
import com.zeta.system.model.dto.sysUser.SysUserDTO;
import com.zeta.system.model.dto.sysUser.SysUserSaveDTO;
import com.zeta.system.model.dto.sysUser.SysUserUpdateDTO;
import com.zeta.system.model.dto.sysUser.UserInfoDTO;
import com.zeta.system.model.entity.SysMenu;
import com.zeta.system.model.entity.SysRole;
import com.zeta.system.model.entity.SysUser;
import com.zeta.system.model.enums.MenuTypeEnum;
import com.zeta.system.model.enums.UserStateEnum;
import com.zeta.system.model.param.ChangePasswordParam;
import com.zeta.system.model.param.ChangeUserBaseInfoParam;
import com.zeta.system.model.param.ResetPasswordParam;
import com.zeta.system.model.param.SysUserQueryParam;
import com.zeta.system.model.poi.SysUserExportPoi;
import com.zeta.system.model.poi.SysUserImportPoi;
import com.zeta.system.poi.SysUserExcelVerifyHandler;
import com.zeta.system.service.ISysRoleMenuService;
import com.zeta.system.service.ISysRoleService;
import com.zeta.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zetaframework.base.controller.SuperNoQueryController;
import org.zetaframework.base.controller.extra.ExistenceController;
import org.zetaframework.base.controller.extra.NoPageQueryController;
import org.zetaframework.base.controller.extra.PoiController;
import org.zetaframework.base.controller.extra.UpdateStateController;
import org.zetaframework.base.param.ExistParam;
import org.zetaframework.base.param.PageParam;
import org.zetaframework.base.param.UpdateStateParam;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.base.result.PageResult;
import org.zetaframework.core.exception.ArgumentException;
import org.zetaframework.core.exception.BusinessException;
import org.zetaframework.core.log.annotation.SysLog;
import org.zetaframework.core.log.enums.LoginStateEnum;
import org.zetaframework.core.log.event.LoginEvent;
import org.zetaframework.core.log.model.LoginLogDTO;
import org.zetaframework.core.satoken.annotation.PreAuth;
import org.zetaframework.core.satoken.annotation.PreCheckPermission;
import org.zetaframework.core.utils.ContextUtil;
import org.zetaframework.core.utils.TreeUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户 前端控制器
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:51
 */
@RequiredArgsConstructor
@Api(tags = "用户")
@PreAuth(replace = "sys:user")
@RestController
@RequestMapping("/api/system/user")
public class SysUserController extends SuperNoQueryController<ISysUserService, Long, SysUser, SysUserSaveDTO, SysUserUpdateDTO>
    implements NoPageQueryController<SysUser, Long, SysUserQueryParam>,
    UpdateStateController<SysUser, Long, Integer>,
    ExistenceController<SysUser, Long>,
    PoiController<SysUserImportPoi, SysUserExportPoi, SysUser, SysUserQueryParam>
{
    private final ISysRoleService roleService;
    private final ISysRoleMenuService roleMenuService;
    private final ApplicationContext applicationContext;
    private final SysUserExcelVerifyHandler sysUserExcelVerifyHandler;

    /**
     * 分页查询
     *
     * @param param 分页查询参数
     * @return ApiResult<PageResult<SysUserDTO>>
     */
    @PreCheckPermission(value = "{}:view")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "分页查询")
    @SysLog(response = false)
    @PostMapping("/page")
    public ApiResult<PageResult<SysUserDTO>> page(@RequestBody PageParam<SysUserQueryParam> param) {
        return success(service.customPage(param));
    }

    /**
     * 处理单体查询数据
     *
     * @param sysUser Entity
     */
    @Override
    public void handlerGetData(SysUser sysUser) {
        if (ObjectUtil.isNull(sysUser)) return;

        // 查询并设置用户拥有的角色
        List<SysRoleDTO> userRoles = service.getUserRoles(sysUser.getId());
        sysUser.setRoles(userRoles);
    }

    /**
     * 处理批量查询数据
     *
     * @param list List<Entity>
     */
    @Override
    public void handlerBatchData(List<SysUser> list) {
        if (list.isEmpty()) return;
        List<Long> userIds = list.stream().map(SysUser::getId).collect(Collectors.toList());
        // 批量获取用户角色 Map<用户id, 用户角色列表>
        Map<Long, List<SysRoleDTO>> userRoleMap = service.getUserRoles(userIds);
        list.forEach(user -> {
            user.setRoles(userRoleMap.getOrDefault(user.getId(), Collections.emptyList()));
        });
    }

    /**
     * 自定义新增
     *
     * @param saveDTO SaveDTO 保存对象
     * @return ApiResult<Boolean>
     */
    @Override
    public ApiResult<Boolean> handlerSave(SysUserSaveDTO saveDTO) {
        // 判断是否存在
        if (new ExistParam<SysUser, Long>("account", saveDTO.getAccount()).isExist(service)) {
            return fail("账号已存在");
        }

        return success(service.saveUser(saveDTO));
    }

    /**
     * 自定义修改
     *
     * @param updateDTO UpdateDTO 修改对象
     * @return ApiResult<Boolean>
     */
    @Override
    public ApiResult<Boolean> handlerUpdate(SysUserUpdateDTO updateDTO) {
        return success(service.updateUser(updateDTO));
    }

    /**
     * 自定义修改状态
     *
     * @param param UpdateStateParam<Id, State> 修改对象
     * @return ApiResult<Boolean>
     */
    @Override
    public ApiResult<Boolean> handlerUpdateState(UpdateStateParam<Long, Integer> param) {
        // 判断状态值是否正常
        Assert.notNull(param.getId(), "用户id不能为空");
        Assert.notNull(param.getState(), "状态不能为空");

        // 判断状态参数是否在定义的用户状态中
        if (!UserStateEnum.getAllCode().contains(param.getState())) {
            return fail("参数异常");
        }

        // 判断用户是否允许修改
        SysUser user = service.getById(param.getId());
        if (user == null) return fail("用户不存在", false);
        if (user.getReadonly() != null && user.getReadonly()) {
            throw new BusinessException("用户[]禁止修改状态", user.getUsername());
        }

        // 修改状态
        return UpdateStateController.super.handlerUpdateState(param);
    }

    /**
     * 自定义单体删除
     *
     * @param id Id
     * @return ApiResult<Boolean>
     */
    @Override
    public ApiResult<Boolean> handlerDelete(Long id) {
        SysUser user = service.getById(id);
        if (user == null) return success(true);

        // 判断用户是否允许删除
        if (user.getReadonly() != null && user.getReadonly()) {
            throw new BusinessException("用户[{}]禁止删除", user.getUsername());
        }

        return super.handlerDelete(id);
    }

    /**
     * 自定义批量删除
     *
     * @param ids Id
     * @return ApiResult<Boolean>
     */
    @Override
    public ApiResult<Boolean> handlerBatchDelete(List<Long> ids) {
        List<SysUser> userList = service.listByIds(ids);
        if (userList.isEmpty()) return success(true);

        // 判断是否存在不运行删除的用户
        userList.forEach(user -> {
            if (user.getReadonly() != null && user.getReadonly()) {
                throw new BusinessException("用户[{}]禁止删除", user.getUsername());
            }
        });

        return super.handlerBatchDelete(ids);
    }

    /**
     * 导入参数增强
     * <p>
     * 说明：
     * 你可以在这里对ImportParams配置进行一些补充
     * 例如设置excel验证规则、校验组、校验处理接口等
     *
     * @param importParams 导入参数 不可为空
     */
    @Override
    public void enhanceImportParams(ImportParams importParams) {
        // 为了保证SysUserImportPoi中的校验注解生效。将Excel校验开启
        importParams.setNeedVerify(true);
        // 校验处理接口：用户名重复校验
        importParams.setVerifyHandler(sysUserExcelVerifyHandler);
        // 也可以这样写 (写一个匿名接口)
        // importParams.setVerifyHandler((IExcelVerifyHandler<SysUserImportPoi>) obj -> {
        //     // 判断是否存在
        //     if (new ExistParam<SysUser, Long>(SysUser::getAccount, obj.getAccount()).isExist(service)) {
        //         return new ExcelVerifyHandlerResult(false, "账号已存在");
        //     }
        //     return new ExcelVerifyHandlerResult(true, "");
        // });
    }

    /**
     * 处理导入数据
     * <p>
     * 说明：
     * 1.你需要手动实现导入逻辑
     * 2.enhanceImportParams方法里，配置了导入参数校验、用户名是否重复校验。所以这里的list里面的数据是不需要校验的
     *
     * @param list 导入数据。不可为空
     */
    @Override
    public ApiResult<Boolean> handlerImport(List<SysUserImportPoi> list) {
        // List<ImportPoi> -> List<Entity>
        List<SysUser> batchList = list.stream()
                .map(importPoi -> {
                    SysUser user = BeanUtil.toBean(importPoi, SysUser.class);
                    // 处理密码
                    user.setPassword(service.encodePassword(importPoi.getPassword()));
                    // 处理角色
                    if (StrUtil.isNotBlank(importPoi.getRoleNames())) {
                        // 通过角色名查询角色
                        List<String> names = StrUtil.split(importPoi.getRoleNames(), StrUtil.COMMA);
                        List<SysRole> roles = roleService.getRolesByNames(names);

                        // List<SysRole> -> List<SysRoleDTO>
                        List<SysRoleDTO> roleDTOs = roles.stream().map(it -> BeanUtil.toBean(it, SysRoleDTO.class)).collect(Collectors.toList());
                        user.setRoles(roleDTOs);
                    }
                    // 其它处理
                    user.setReadonly(false);
                    user.setState(UserStateEnum.NORMAL.getCode());
                    return user;
                })
                .collect(Collectors.toList());
        return success(service.batchImportUser(batchList));
    }

    /**
     * 获取待导出的数据
     *
     * @param param 查询参数 不可为空
     * @return List<ExportBean>
     */
    @Override
    public List<SysUserExportPoi> findExportList(SysUserQueryParam param) {
        // 判断状态参数是否在定义的用户状态中
        if (param.getState() != null && !UserStateEnum.getAllCode().contains(param.getState())) {
            throw new ArgumentException("状态参数异常");
        }

        // 条件查询Entity数据
        List<SysUser> list = handlerBatchQuery(param);
        if (list.isEmpty()) return Collections.emptyList();

        // List<Entity> -> List<ExportPoi>
        return list.stream()
                .map(user -> {
                    SysUserExportPoi exportPoi = BeanUtil.toBean(user, SysUserExportPoi.class);
                    // 处理用户角色 ps:导出角色名还是导出角色编码看需求
                    List<String> roles = user.getRoles().stream()
                            .map(SysRoleDTO::getName)
                            .filter(StrUtil::isNotBlank)
                            .collect(Collectors.toList());

                    // 设置用户角色
                    exportPoi.setRoles(roles);
                    return exportPoi;
                })
                .collect(Collectors.toList());
    }

    /**
     * 修改自己的密码
     *
     * @param param 修改密码的参数
     * @param request HttpServletRequest
     * @return
     */
    @ApiOperation(value = "修改自己的密码")
    @ResponseBody
    @PutMapping("/changePwd")
    public ApiResult<Boolean> changePwd(@RequestBody @Validated ChangePasswordParam param, HttpServletRequest request) {
        SysUser user = service.getById(ContextUtil.getUserId());
        if (user == null) throw new BusinessException("用户不存在");

        // 旧密码是否正确
        if (!service.comparePassword(param.getOldPwd(), user.getPassword())) {
            return fail("旧密码不正确", false);
        }

        // 修改成新密码
        user.setPassword(service.encodePassword(param.getNewPwd()));
        if (!service.updateById(user)) {
            return fail("修改失败", false);
        }

        // 登出日志
        LoginEvent event = new LoginEvent(LoginLogDTO.loginFail(user.getAccount(), LoginStateEnum.LOGOUT, "修改密码", request));
        applicationContext.publishEvent(event);

        // 下线
        StpUtil.logout(user.getId());
        return success("修改成功", true);
    }

    /**
     * 重置密码
     *
     * @param param ResetPasswordParam 重置密码参数
     * @return ApiResult<Boolean>
     */
    @PreCheckPermission(value = "{}:update")
    @ApiOperation(value = "重置密码")
    @PutMapping("/restPwd")
    public ApiResult<Boolean> updatePwd(@RequestBody @Validated ResetPasswordParam param, HttpServletRequest request) {
        SysUser user = service.getById(param.getId());
        if (user == null) return success(true);

        // 判断用户是否允许重置密码
        if (user.getReadonly() != null && user.getReadonly()) {
            throw new BusinessException("用户[]禁止重置密码", user.getUsername());
        }

        // 密码加密
        user.setPassword(service.encodePassword(param.getPassword()));
        // 修改密码
        if (!service.updateById(user)) {
            return fail("重置密码失败");
        }

        // 登出日志
        LoginEvent event = new LoginEvent(LoginLogDTO.loginFail(user.getAccount(), LoginStateEnum.LOGOUT, "重置密码", request));
        applicationContext.publishEvent(event);

        // 让被修改密码的人下线
        StpUtil.logout(user.getId());
        return success(true);
    }

    /**
     * 获取当前用户基本信息
     */
    @ApiOperationSupport(order = 100)
    @ApiOperation(value = "获取当前用户基本信息")
    @GetMapping("/info")
    public ApiResult<UserInfoDTO> userInfo(){
        SysUser user = service.getById(ContextUtil.getUserId());
        if (user == null) return fail("用户不存在");
        UserInfoDTO userInfoDTO = BeanUtil.toBean(user, UserInfoDTO.class);

        // 获取用户角色列表
        userInfoDTO.setRoles(StpUtil.getRoleList());
        // 获取用户权限列表
        userInfoDTO.setPermissions(StpUtil.getPermissionList());
        return success(userInfoDTO);
    }

    /**
     * 修改用户基础信息（右上角用户信息页面-保存修改时调用）
     *
     * @param param 修改用户基本信息参数
     * @return
     */
    @ApiOperationSupport(order = 101)
    @ApiOperation(value = "修改用户基础信息")
    @PutMapping("/changeUserBaseInfo")
    public ApiResult<Boolean> changeUserBaseInfo(@RequestBody @Validated ChangeUserBaseInfoParam param) {
        SysUser user = service.getById(ContextUtil.getUserId());
        if (user == null) return fail("用户不存在");

        // 判断用户是否允许修改
        if (user.getReadonly() != null && user.getReadonly()) {
            throw new BusinessException("用户[]禁止修改", user.getUsername());
        }

        // param -> entity
        BeanUtil.copyProperties(param, user);

        // 修改用户信息
        boolean result = service.updateUserBaseInfo(user);
        return  result ? success("修改成功", true) : fail("修改失败", false);
    }

    /**
     * 获取当前用户菜单
     */
    @ApiOperationSupport(order = 110)
    @ApiOperation(value = "获取当前用户菜单")
    @GetMapping("/menu")
    public ApiResult<List<FrontRoute>> userMenu() {
        // 查询用户对应的菜单
        List<SysMenu> menuList = roleMenuService.listMenuByUserId(ContextUtil.getUserId(), MenuTypeEnum.MENU.name());

        // List<SysMenu> -> List<FrontRoute>
        List<FrontRoute> frontRouteList = menuList.stream()
                .map(FrontRoute::convert)
                .collect(Collectors.toList());
        return success(TreeUtil.buildTree(frontRouteList, false));
    }

}

