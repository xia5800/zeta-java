package org.zetaframework.base.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 分页查询返回结果
 *
 * @author gcc
 */
@ApiModel(description = "分页查询返回结果")
public class PageResult<T> {

    /** 当前页数据 */
    @ApiModelProperty(value = "当前页数据")
    private List<T> list;

    /** 总数量 */
    @ApiModelProperty(value = "总数量")
    private Long count = 0L;

    private PageResult() {
    }

    public PageResult(List<T> list, Long count) {
        this.list = list;
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
