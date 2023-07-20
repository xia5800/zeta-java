package org.zetaframework.core.utils;

import cn.hutool.core.collection.CollUtil;
import org.zetaframework.base.entity.ITree;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * List转tree工具类
 *
 * @author gcc
 */
public class TreeUtil {

    /**
     * buildTree
     *
     * @param list List<TreeEntity>
     * @return List<E>
     */
    public static <E extends ITree<E, ? extends Serializable>> List<E> buildTree(List<E> list) {
        return buildTree(list, true);
    }

    /**
     * buildTree
     *
     * @param list List<TreeEntity>
     * @param isShowNullChildren Boolean 是否显示为空的children
     * @return List<E>
     */
    public static <E extends ITree<E, ? extends Serializable>> List<E> buildTree(List<E> list, boolean isShowNullChildren) {
        if (CollUtil.isEmpty(list)) { return list; }

        /**
         * 将
         * [
         *  { id: 1, pid: 0, name: "1" },
         *  { id: 2, pid: 1, name: "1-1" },
         *  { id: 3, pid: 1, name: "1-2" },
         *  { id: 4, pid: 0, name: "2" },
         * ]
         *
         * 转换为：
         * [
         *   { id: 1, pid: 0, name: "1", children: [{ id: 2, pid: 1, name: "1-1" }, { id: 3, pid: 1, name: "1-2" }] },
         *   { id: 2, pid: 1, name: "1-1", children: [] },
         *   { id: 3, pid: 1, name: "1-2", children: [] },
         *   { id: 4, pid: 0, name: "2", children: [] },
         * ]
         */
        for (E entity : list) {
            // filter出 entity 的子节点。eg: entity.id = 1, children = [{ id: 2, pid: 1, name: "1-1" }, { id: 3, pid: 1, name: "1-2" }]
            List<E> children = list.stream()
                    .filter(it -> it.getParentId().equals(entity.getId()))
                    .collect(Collectors.toList());

            // eg: { id: 1, pid: 0, name: "1", children: [{ id: 2, pid: 1, name: "1-1" }, { id: 3, pid: 1, name: "1-2" }] },
            if (isShowNullChildren) {
                entity.setChildren(children);
            } else {
                if (CollUtil.isNotEmpty(children)) {
                    entity.setChildren(children);
                }
            }
        }

        /**
         * filter出 entity 的父节点
         *
         * 得到：
         * [
         *   { id: 1, pid: 0, name: "1", children: [{ id: 2, pid: 1, name: "1-1" }, { id: 3, pid: 1, name: "1-2" }] },
         *   { id: 4, pid: 0, name: "2", children: [] },
         * ]
         */
        List<E> parentList = list.stream().filter(it ->
                it.getParentId() == null
                    || "".equals(it.getParentId().toString())
                    || "0".equals(it.getParentId().toString())
        ).collect(Collectors.toList());

        // 如果parentList不为空，返回parentList，否则返回list。
        return CollUtil.isNotEmpty(parentList) ? parentList : list;
    }

}
