package org.zetaframework.base.controller.curd;

import cn.hutool.core.bean.BeanUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.zetaframework.base.controller.BaseController;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.log.annotation.SysLog;
import org.zetaframework.core.satoken.annotation.PreCheckPermission;
import org.zetaframework.core.satoken.annotation.PreMode;

/**
 * 基础新增 Controller
 *
 * @param <Entity> 实体
 * @param <SaveDTO> 保存对象
 * @author gcc
 */
public interface SaveController<Entity, SaveDTO> extends BaseController<Entity> {

    /**
     * 新增
     *
     * @param saveDTO SaveDTO 保存对象
     * @return ApiResult<Boolean>
     */
    @PreCheckPermission(value = {"{}:add", "{}:save"}, mode = PreMode.OR)
    @ApiOperationSupport(order = 40, author = "AutoGenerate")
    @SysLog
    @ApiOperation(value = "新增")
    @PostMapping
    default ApiResult<Boolean> save(@RequestBody @Validated SaveDTO saveDTO) {
        ApiResult<Boolean> result = handlerSave(saveDTO);
        if (result.getDefExec()) {
            // SaveDTO -> Entity
            Entity entity = BeanUtil.toBean(saveDTO, getEntityClass());
            result.setData(getBaseService().save(entity));
        }
        return result;
    }

    /**
     * 自定义新增
     *
     * @param saveDTO SaveDTO 保存对象
     * @return ApiResult<Boolean>
     */
    default ApiResult<Boolean> handlerSave(SaveDTO saveDTO) {
        return ApiResult.successDef();
    }

}
