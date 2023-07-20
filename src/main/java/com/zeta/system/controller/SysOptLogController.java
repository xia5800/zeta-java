package com.zeta.system.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zeta.system.model.dto.sysOptLog.SysOptLogTableDTO;
import com.zeta.system.model.entity.SysOptLog;
import com.zeta.system.model.entity.SysUser;
import com.zeta.system.model.param.SysOptLogQueryParam;
import com.zeta.system.service.ISysOptLogService;
import com.zeta.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zetaframework.base.controller.SuperSimpleController;
import org.zetaframework.base.param.PageParam;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.base.result.PageResult;
import org.zetaframework.core.saToken.annotation.PreAuth;
import org.zetaframework.core.saToken.annotation.PreCheckPermission;

/**
 * 操作日志 前端控制器
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:01
 */
@RequiredArgsConstructor
@Api(tags = "操作日志")
@PreAuth(replace = "sys:optLog")
@RestController
@RequestMapping("/api/system/optLog")
public class SysOptLogController extends SuperSimpleController<ISysOptLogService, SysOptLog> {

    private final ISysUserService userService;

    /**
     * 分页查询
     * @param param PageParam<PageQuery> 分页查询参数
     * @return ApiResult<PageResult<SysOptLogTableDTO>>
     */
    @PreCheckPermission(value = "{}:view")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public ApiResult<PageResult<SysOptLogTableDTO>> page(@RequestBody PageParam<SysOptLogQueryParam> param) {
        return success(service.pageTable(param));
    }

    /**
     * 单体查询
     * @param id 主键
     * @return ApiResult<Entity>
     */
    @PreCheckPermission(value = "{}:view")
    @ApiOperationSupport(order = 20)
    @ApiOperation(value = "单体查询", notes = "根据主键查询唯一数据，若查询不到则返回null")
    @GetMapping("/{id}")
    public ApiResult<SysOptLog> get(@PathVariable("id") @ApiParam("主键") Long id) {
        SysOptLog entity = service.getById(id);
        if (entity == null) return success(null);

        // 查询操作人
        SysUser user = userService.getById(entity.getCreatedBy());
        entity.setUserName(user != null ? user.getUsername() : "");
        return success(entity);
    }

}

