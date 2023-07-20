package org.zetaframework.base.controller.curd;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.zetaframework.base.controller.BaseController;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.log.annotation.SysLog;
import org.zetaframework.core.saToken.annotation.PreCheckPermission;
import org.zetaframework.core.saToken.annotation.PreMode;

import java.io.Serializable;
import java.util.List;

/**
 * 基础删除 Controller
 *
 * @param <Entity> 实体
 * @param <Id>     主键字段类型
 * @author gcc
 */
public interface DeleteController<Entity, Id extends Serializable> extends BaseController<Entity> {

    /**
     * 单体删除
     *
     * @param id Id
     * @return ApiResult<Boolean>
     */
    @PreCheckPermission(value = {"{}:delete", "{}:remove"}, mode = PreMode.OR)
    @ApiOperationSupport(order = 60, author = "AutoGenerate")
    @ApiOperation(value = "单体删除")
    @SysLog
    @DeleteMapping("/{id}")
    default ApiResult<Boolean> delete(@PathVariable @ApiParam("主键")Id id) {
        ApiResult<Boolean> result = handlerDelete(id);
        if (result.getDefExec()) {
            result.setData(getBaseService().removeById(id));
        }
        return result;
    }

    /**
     * 自定义单体删除
     *
     * @param id Id
     * @return ApiResult<Boolean>
     */
    default ApiResult<Boolean> handlerDelete(Id id) {
        return ApiResult.successDef();
    }

    /**
     * 批量删除
     *
     * @param ids List<Id>
     * @return ApiResult<Boolean>
     */
    @PreCheckPermission(value = {"{}:delete", "{}:remove"}, mode = PreMode.OR)
    @ApiOperationSupport(order = 70, author = "AutoGenerate")
    @ApiOperation(value = "批量删除")
    @SysLog
    @DeleteMapping("/batch")
    default ApiResult<Boolean> batchDelete(@RequestBody List<Id> ids) {
        ApiResult<Boolean> result = handlerBatchDelete(ids);
        if (result.getDefExec()) {
            result.setData(getBaseService().removeByIds(ids));
        }
        return result;
    }

    /**
     * 自定义批量删除
     *
     * @param ids List<Id>
     * @return ApiResult<Boolean>
     */
    default ApiResult<Boolean> handlerBatchDelete(List<Id> ids) {
        return ApiResult.successDef();
    }

}
