package org.zetaframework.base.controller.extra;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.zetaframework.base.controller.BaseController;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.log.annotation.SysLog;
import org.zetaframework.core.saToken.annotation.PreCheckPermission;

import java.io.Serializable;
import java.util.List;

/**
 * 没有分页查询方法的QueryController
 *
 * @param <Id>          主键字段类型
 * @param <Entity>      实体
 * @param <QueryParam>   分页参数
 * @author gcc
 */
public interface NoPageQueryController<Entity, Id extends Serializable, QueryParam> extends BaseController<Entity> {

    /**
     * 批量查询
     *
     * @param param PageQuery 批量查询参数
     * @return ApiResult<List<Entity>>
     */
    @PreCheckPermission(value = "{}:view")
    @ApiOperationSupport(order = 20, author = "AutoGenerate")
    @ApiOperation(value = "批量查询")
    @SysLog(response = false)
    @PostMapping("/query")
    default ApiResult<List<Entity>> list(@RequestBody QueryParam param) {
        return success(handlerBatchQuery(param));
    }

    /**
     * 自定义批量查询
     *
     * @param param QueryParam
     * @return List<Entity>
     */
    default List<Entity> handlerBatchQuery(QueryParam param) {
        Entity entity = BeanUtil.toBean(param, getEntityClass());
        // 批量查询
        List<Entity> list = getBaseService().list(new QueryWrapper<Entity>(entity));
        // 处理批量查询数据
        handlerBatchData(list);
        return list;
    }

    /**
     * 处理批量查询数据
     *
     * @param list List<Entity>
     */
    default void handlerBatchData(List<Entity> list) { }

    /**
     * 单体查询
     *
     * @param id Id 主键
     * @return ApiResult<Entity>
     */
    @PreCheckPermission(value = "{}:view")
    @ApiOperationSupport(order = 30, author = "AutoGenerate")
    @ApiOperation(value = "单体查询", notes = "根据主键查询唯一数据，若查询不到则返回null")
    @SysLog
    @GetMapping("/{id}")
    default ApiResult<Entity> get(@PathVariable("id") @ApiParam("主键") Id id) {
        Entity entity = getBaseService().getById(id);
        // 处理单体查询数据
        handlerGetData(entity);
        return success(entity);
    }

    /**
     * 处理单体查询数据
     *
     * @param entity Entity
     */
    default void handlerGetData(Entity entity) { }

}
