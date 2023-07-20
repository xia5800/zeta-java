package org.zetaframework.base.param;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页查询参数
 *
 * @author gcc
 */
@ApiModel(description = "分页查询参数")
public class PageParam<T> {

    /** 当前页 */
    @ApiModelProperty(value = "当前页", example = "1", required = true)
    private Long page = 1L;

    /** 每页显示条数 */
    @ApiModelProperty(value = "每页显示条数", example = "10", required = true)
    private Long limit = 10L;

    /** 查询条件 */
    @ApiModelProperty(value = "查询条件", required = true)
    @Valid  // 见[docs/03功能介绍/参数校验.md]常见问题
    private T model;

    /** 排序字段 */
    @ApiModelProperty(value = "排序字段", allowableValues = "id,createTime,updateTime", example = "id", required = false)
    private String sort = "id";

    /** 排序规则 */
    @ApiModelProperty(value = "排序规则", allowableValues = "desc,asc", example = "desc", required = false)
    private String order = "desc";

    private PageParam() {
    }

    public PageParam(Long page, Long limit) {
        this.page = page;
        this.limit = limit;
    }

    public PageParam(Long page, Long limit, T model) {
        this.page = page;
        this.limit = limit;
        this.model = model;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }


    /**
     * 构建分页对象
     * @return IPage<E>
     */
    public <E> IPage<E> buildPage() {
        Page<E> page = new Page<E>(this.page, this.limit);

        // 判断是否有排序
        if (StrUtil.isBlank(this.sort)) {
            return page;
        }

        // 处理排序
        List<OrderItem> orders = new ArrayList<>();
        String[] sortArr = StrUtil.splitToArray(this.sort, StrUtil.COMMA);
        String[] orderArr = StrUtil.splitToArray(this.order, StrUtil.COMMA);

        for (int i = 0; i < sortArr.length ; i++) {
            // 驼峰转下划线
            String sortField = StrUtil.toUnderlineCase(sortArr[i]);
            orders.add(
                    StrUtil.equalsAny(orderArr[i], "asc", "ascending") ?
                            OrderItem.asc(sortField) : OrderItem.desc(sortField)
            );
        }
        page.setOrders(orders);
        return page;
    }

    /**
     * 排序字段别名处理
     *
     * 说明适用于下面这种情况：
     * <pre>
     * select t1.id, t2.name from order t1 left join user t2 on t1.user_id = t2.id
     * order by t1.id desc
     * </pre>
     *
     * 使用方式：
     * <pre>
     * // 查询条件
     * { "page": 1, "limit": 10, "model": { }, "order": "desc", "sort": "id" }
     *
     * // 使用
     * new PageParam(1, 10, new Order()).setSortAlias("t1.")
     * 或者
     * public void customPage(@RequestBody PageParam<QueryParam> param) {
     *     param.setSortAlias("t1.")
     *     // ...
     * }
     *
     * // 实际生成sql
     * select t1.id, t2.name from order t1 left join user t2 on t1.user_id = t2.id
     * order by t1.id desc
     * </pre>
     * @param alias 别名 eg: "t1.","a.","order."
     */
    @JsonIgnore
    public void setSortAlias(String alias) {
        String[] sortArr = StrUtil.splitToArray(this.sort, StrUtil.COMMA);
        if (ArrayUtil.isNotEmpty(sortArr)) {
            this.sort = Arrays.stream(sortArr).map(it -> {
                // 判断字段是否有别名，没有则加上
                if (it.startsWith(alias)) {
                    return it;
                }
                return alias + it;
            }).collect(Collectors.joining(StrUtil.COMMA));
        }
    }

}
