package org.zetaframework.base.controller.extra;

import cn.hutool.core.bean.BeanUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.zetaframework.base.controller.BaseController;
import org.zetaframework.base.param.UpdateStateParam;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.saToken.annotation.PreCheckPermission;
import org.zetaframework.core.saToken.annotation.PreMode;

import java.io.Serializable;

/**
 * 修改状态 Controller
 *
 * @param <Entity> 实体
 * @param <Id>     主键字段类型
 * @param <State>  state字段的类型
 * @author gcc
 */
public interface UpdateStateController<Entity, Id extends Serializable, State extends Serializable> extends BaseController<Entity> {

    /**
     * 修改状态
     *
     * @param param UpdateStateParam<Id, State> 修改对象
     * @return ApiResult<Boolean>
     */
    @PreCheckPermission(value = {"{}:edit", "{}:update"}, mode = PreMode.OR)
    @ApiOperationSupport(order = 51, author = "AutoGenerate")
    @ApiOperation(value = "修改状态")
    @PutMapping("/state")
    default ApiResult<Boolean> updateState(@RequestBody UpdateStateParam<Id, State> param) {
        ApiResult<Boolean> result = handlerUpdateState(param);
        if (result.getDefExec()) {
            // updateDTO -> Entity
            Entity entity = BeanUtil.toBean(param, getEntityClass());
            result.setData(getBaseService().updateById(entity));
        }
        return result;
    }

    /**
     * 自定义修改状态
     *
     * @param param UpdateStateParam<Id, State> 修改对象
     * @return ApiResult<Boolean>
     */
    default ApiResult<Boolean> handlerUpdateState(UpdateStateParam<Id, State> param) {
        return ApiResult.successDef();
    }

}
