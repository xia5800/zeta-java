package org.zetaframework.base.entity;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 树形表结构 实体类
 * 包括id、create_time、create_by、update_by、update_time、label、parent_id、sort_value 字段的表继承的树形实体
 *
 * @author gcc
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TreeEntity<E, T extends Serializable> extends Entity<T> implements ITree<E, T> {

    /** 名称 */
    @ApiModelProperty(value = "名称")
    @NotEmpty(message = "名称不能为空")
    @TableField(value = "label", condition = SqlCondition.LIKE)
    protected String label;

    /** 父级Id */
    @ApiModelProperty(value = "父级Id")
    @TableField(value = "parent_id")
    protected T parentId;

    /** 排序 */
    @ApiModelProperty(value = "排序")
    @TableField(value = "sort_value")
    protected Integer sortValue;

    /** 子节点 */
    @ApiModelProperty(value = "子节点")
    @TableField(exist = false)
    protected List<E> children = new ArrayList<>();

    public List<E> getChildren() {
        return children;
    }


    /**
     * 获取树父节点id
     */
    @Override
    public T getParentId() {
        return this.parentId;
    }

    /**
     * 设置树子级
     *
     * @param children
     */
    @Override
    public void setChildren(List<E> children) {
        this.children = CollUtil.isNotEmpty(children) ? children : new ArrayList<>();
    }
}
