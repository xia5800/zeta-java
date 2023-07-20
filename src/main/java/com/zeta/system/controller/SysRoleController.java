package com.zeta.system.controller;

import cn.hutool.core.util.StrUtil;
import com.zeta.system.model.dto.sysRole.SysRoleSaveDTO;
import com.zeta.system.model.dto.sysRole.SysRoleUpdateDTO;
import com.zeta.system.model.entity.SysRole;
import com.zeta.system.model.param.SysRoleQueryParam;
import com.zeta.system.service.ISysRoleService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zetaframework.base.controller.SuperController;
import org.zetaframework.base.param.ExistParam;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.exception.BusinessException;
import org.zetaframework.core.saToken.annotation.PreAuth;

import java.util.List;

/**
 * 角色 前端控制器
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
@Api(tags = "角色")
@PreAuth(replace = "sys:role")
@RestController
@RequestMapping("/api/system/role")
public class SysRoleController extends SuperController<ISysRoleService, Long, SysRole, SysRoleQueryParam, SysRoleSaveDTO, SysRoleUpdateDTO> {

    /**
     * 自定义新增
     *
     * @param saveDTO SaveDTO 保存对象
     * @return ApiResult<Boolean>
     */
    @Override
    public ApiResult<Boolean> handlerSave(SysRoleSaveDTO saveDTO) {
        // 判断是否存在
        if (new ExistParam<SysRole, Long>("name", saveDTO.getName()).isExist(service)) {
            return fail("角色名已存在");
        }
        if (new ExistParam<SysRole, Long>("code", saveDTO.getCode()).isExist(service)) {
            return fail("角色编码已存在");
        }

        return super.handlerSave(saveDTO);
    }


    /**
     * 自定义修改
     *
     * @param updateDTO UpdateDTO 修改对象
     * @return ApiResult<Boolean>
     */
    @Override
    public ApiResult<Boolean> handlerUpdate(SysRoleUpdateDTO updateDTO) {
        // 判断是否存在
        if (new ExistParam<SysRole, Long>("name", updateDTO.getName(), updateDTO.getId()).isExist(service)) {
            return fail("角色名已存在");
        }
        if (new ExistParam<SysRole, Long>("code", updateDTO.getCode(), updateDTO.getId()).isExist(service)) {
            return fail("角色编码已存在");
        }

        return super.handlerUpdate(updateDTO);
    }

    /**
     * 自定义单体删除
     *
     * @param id Id
     * @return ApiResult<Boolean>
     */
    @Override
    public ApiResult<Boolean> handlerDelete(Long id) {
        SysRole role = service.getById(id);
        if (role == null) return success(true);

        // 判断角色是否允许删除
        if (role.getReadonly() != null && role.getReadonly()) {
            throw new BusinessException("角色[{}]禁止删除", role.getName());
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
        List<SysRole> roleList = service.listByIds(ids);
        if (roleList.isEmpty()) return success(true);

        // 判断是否存在不允许删除的角色
        roleList.forEach(role -> {
            if (role.getReadonly() != null && role.getReadonly()) {
                throw new BusinessException("角色[{}]禁止删除", role.getName());
            }
        });

        return super.handlerBatchDelete(ids);
    }

}

