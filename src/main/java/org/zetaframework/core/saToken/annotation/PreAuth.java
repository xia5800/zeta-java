package org.zetaframework.core.saToken.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户权限{}占位符替换
 *
 * 说明：
 * 配合@PreCheckPermission注解使用。只能用于类
 *
 * 使用方式：
 * <pre>{@code
 *      @PreAuth(replace = "sys:user")
 *      // 校验是否有sys:user:add权限
 *      @PreCheckPermission(value = "{}:add")
 *      // 关闭类鉴权
 *      @PreAuth(enabled = false)
 * }</pre>
 * @author gcc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface PreAuth {

    /**
     * 替换@PreCheckPermission注解中value的占位符{}
     */
    String replace() default "";

    /**
     * 该类是否启用权限、角色校验
     */
    boolean enabled() default true;

}
