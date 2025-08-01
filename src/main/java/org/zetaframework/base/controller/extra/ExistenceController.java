package org.zetaframework.base.controller.extra;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.zetaframework.base.controller.BaseController;
import org.zetaframework.base.param.ExistParam;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.satoken.annotation.PreCheckPermission;

/**
 * 验证存在 Controller
 *
 * @param <Entity> 实体
 * @param <Id>     主键字段类型
 * @author gcc
 */
public interface ExistenceController<Entity, Id> extends BaseController<Entity> {

    /**
     * 验证字段是否存在
     * @param param ExistenceParam<Entity, Id>
     * @return ApiResult<Boolean>
     */
    @PreCheckPermission(value = "{}:view")
    @ApiOperation(value = "验证字段是否存在", notes = "<pre>\n" +
    "例如：\n" +
    "\n" +
    "新增用户的时候，验证用户名(username)的值(张三)是否被人使用了\n" +
    "{\"field\": \"username\",  \"value\": \"张三\"}\n" +
    "修改用户的时候，验证用户名(username)的值(李四)是否被除了当前用户id(2011214167781)的人使用了\n" +
    "{\"field\": \"username\",  \"value\": \"李四\",  \"id\": \"2011214167781\"}\n" +
    "</pre>")
    @GetMapping("/existence")
    default ApiResult<Boolean> existence(ExistParam<Entity, Id> param) {
        if (param.isExist(getBaseService())) {
            return success(StrUtil.format("{}已存在", param.getValue()), true);
        }
        return success(StrUtil.format("{}不存在", param.getValue()), false);
    }

}
