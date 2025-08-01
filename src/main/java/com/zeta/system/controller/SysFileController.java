package com.zeta.system.controller;

import com.zeta.system.model.entity.SysFile;
import com.zeta.system.model.param.SysFileQueryParam;
import com.zeta.system.service.ISysFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zetaframework.base.controller.SuperSimpleController;
import org.zetaframework.base.controller.curd.DeleteController;
import org.zetaframework.base.controller.curd.QueryController;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.log.annotation.SysLog;
import org.zetaframework.core.satoken.annotation.PreAuth;
import org.zetaframework.core.satoken.annotation.PreCheckPermission;
import org.zetaframework.core.satoken.annotation.PreMode;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 系统文件 前端控制器
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
@Api(tags = "系统文件")
@PreAuth(replace = "sys:file")
@RestController
@RequestMapping("/api/system/file")
public class SysFileController extends SuperSimpleController<ISysFileService, SysFile>
    implements QueryController<SysFile, Long, SysFileQueryParam>,
    DeleteController<SysFile, Long>
{

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @param bizType 业务类型 例如：order、user_avatar等 会影响文件url的值
     */
    @SysLog(request = false)
    @PreCheckPermission(value = {"{}:add", "{}:save"}, mode = PreMode.OR)
    @ApiOperation(value = "上传文件")
    @PostMapping("/upload")
    public ApiResult<SysFile> upload(
            @RequestParam
            MultipartFile file,
            @RequestParam(required = false)
            @ApiParam(value = "业务类型 例如：order、user_avatar等 会影响文件url的值", example = "avatar")
            String bizType
    ) {
        return success(service.upload(file, bizType));
    }

    /**
     * 下载文件
     *
     * @param id 文件id
     * @param response HttpServletResponse
     */
    @SysLog(response = false)
    @PreCheckPermission(value = "{}:export")
    @ApiOperation(value = "下载文件")
    @GetMapping(value = "/download", produces = "application/octet-stream")
    public void download(@RequestParam @ApiParam("文件id") Long id, HttpServletResponse response) {
        service.download(id, response);
    }

    /**
     * 自定义单体删除
     *
     * @param id Id
     * @return ApiResult<Boolean>
     */
    @Override
    public ApiResult<Boolean> handlerDelete(Long id) {
        return success(service.delete(id));
    }

    /**
     * 自定义批量删除
     *
     * @param ids Id
     * @return ApiResult<Boolean>
     */
    @Override
    public ApiResult<Boolean> handlerBatchDelete(List<Long> ids) {
        return success(service.batchDelete(ids));
    }
}

