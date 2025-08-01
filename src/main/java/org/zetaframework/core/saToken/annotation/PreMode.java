package org.zetaframework.core.satoken.annotation;

/**
 * 注解鉴权的验证模式
 *
 * @author gcc
 */
public enum PreMode {
    /** 必须具有所有的元素 */
    AND,
    /** 只需具有其中一个元素 */
    OR;
}
