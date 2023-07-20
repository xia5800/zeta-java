package org.zetaframework.base.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 树接口
 *
 * 说明：
 * 实现了该接口的，可以被转成树
 *
 * @author gcc
 */
public interface ITree<E, T extends Serializable> {

    /**
     * 获取树节点id
     */
    T getId();

    /**
     * 获取树父节点id
     */
    T getParentId();

    /**
     * 设置树子级
     */
    void setChildren(List<E> children);

}
