package org.zetaframework.core.satoken.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限认证
 *
 * 说明：
 * 参考sa-token框架的@SaCheckRole注解实现
 *
 * 使用方式:
 * <pre>{@code
 *      // 校验是否有admin角色
 *      @PreCheckRole(value = "admin")
 *      // 校验是否有admin 和 super_admin角色
 *      @PreCheckRole(value = {"admin", "super_admin"})
 *      // 校验是否有admin 或 super_admin角色
 *      @PreCheckRole(value = {"admin", "super_admin"}, mode = PreMode.OR)
 *      // 关闭角色鉴权
 *      @PreCheckRole(enable = false)
 *      @PreCheckRole(value = "admin", enable = false)
 * }</pre>
 * @author gcc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface PreCheckRole {

    /**
     * 需要校验的权限码
     */
    String[] value() default {};

    /**
     * 验证模式：AND | OR，默认AND
     */
    PreMode mode() default PreMode.AND;

    /**
     * 该类、方法是否启用角色校验
     */
    boolean enabled() default true;

}
