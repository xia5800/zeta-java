package org.zetaframework.base.controller.curd;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.zetaframework.base.controller.BaseController;
import org.zetaframework.base.param.PageParam;
import org.zetaframework.base.result.PageResult;

/**
 * 分页 Controller
 *
 * @param <Entity>     实体
 * @param <QueryParam>  查询参数
 * @author gcc
 */
public interface PageController<Entity, QueryParam> extends BaseController<Entity> {

    /**
     * 分页查询
     *
     * @param param PageParam<QueryParam>
     * @return PageResult<Entity>
     */
    default PageResult<Entity> query(PageParam<QueryParam> param) {
        // 处理查询参数
        handlerQueryParams(param);

        // 构建分页对象
        IPage<Entity> page = param.buildPage();
        // PageQuery -> Entity
        Entity model = BeanUtil.toBean(param.getModel(), getEntityClass());

        // 构造分页查询条件
        QueryWrapper<Entity> wrapper = handlerWrapper(model, param);
        // 执行单表分页查询
        getBaseService().page(page, wrapper);

        // 处理查询后的分页结果
        handlerResult(page);

        return new PageResult<>(page.getRecords(), page.getTotal());
    }


    /**
     * 构造查询条件
     *
     * @param model Entity
     * @param param PageParam<PageQuery>
     * @return QueryWrapper<Entity>
     */
    default QueryWrapper<Entity> handlerWrapper(Entity model, PageParam<QueryParam> param) {
        if (model == null) {
            return new QueryWrapper<Entity>();
        }
        return new QueryWrapper<Entity>(model);
    }


    /**
     * 处理查询参数
     *
     * @param param 查询参数
     */
    default void handlerQueryParams(PageParam<QueryParam> param) { }

    /**
     * 处理查询后的数据
     *
     * @param page IPage
     */
    default void handlerResult(IPage<Entity> page) { }

}
