package org.zetaframework.base.controller.curd;

import cn.hutool.core.bean.BeanUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.zetaframework.base.controller.BaseController;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.log.annotation.SysLog;
import org.zetaframework.core.satoken.annotation.PreCheckPermission;
import org.zetaframework.core.satoken.annotation.PreMode;
import org.zetaframework.core.validation.group.Update;

/**
 * 基础修改 Controller
 *
 * @param <Entity> 实体
 * @param <UpdateDTO> 修改对象
 * @author gcc
 */
public interface UpdateController<Entity, UpdateDTO> extends BaseController<Entity> {

    /**
     * 修改
     *
     * @param updateDTO UpdateDTO 修改对象
     * @return ApiResult<Boolean>
     */
    @PreCheckPermission(value = {"{}:edit", "{}:update"}, mode = PreMode.OR)
    @ApiOperationSupport(order = 50, author = "AutoGenerate")
    @ApiOperation(value = "修改")
    @SysLog
    @PutMapping
    default ApiResult<Boolean> update(@RequestBody @Validated(Update.class) UpdateDTO updateDTO) {
        ApiResult<Boolean> result = handlerUpdate(updateDTO);
        if (result.getDefExec()) {
            // updateDTO -> Entity
            Entity entity = BeanUtil.toBean(updateDTO, getEntityClass());
            result.setData(getBaseService().updateById(entity));
        }
        return result;
    }

    /**
     * 自定义修改
     *
     * @param updateDTO UpdateDTO 修改对象
     * @return ApiResult<Boolean>
     */
    default ApiResult<Boolean> handlerUpdate(UpdateDTO updateDTO) {
        return ApiResult.successDef();
    }

}
