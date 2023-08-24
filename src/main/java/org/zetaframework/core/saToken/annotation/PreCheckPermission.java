package org.zetaframework.core.saToken.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限认证
 *
 * 说明：
 * 参考sa-token框架的@SaCheckPermission注解实现
 *
 * 使用方式:
 * <pre>{@code
 *      @PreAuth(replace = "sys:user")
 *      // 校验是否有sys:user:add权限
 *      @PreCheckPermission(value = "{}:add")
 *      // 校验是否有sys:user:add 和 sys:user:save权限
 *      @PreCheckPermission(value = {"{}:add", "{}:save"})
 *      // 校验是否有sys:user:add 或 sys:user:save权限
 *      @PreCheckPermission(value = {"{}:add", "{}:save"}, mode = PreMode.OR)
 *      // 校验是否有sys:user:add权限 或 是否有admin角色
 *      @PreCheckPermission(value = "{}:add", orRole = "admin")
 *      // 关闭接口鉴权
 *      @PreCheckPermission(enabled = false)
 *      @PreCheckPermission(value = "{}:add", enabled = false)
 * }</pre>
 * @author gcc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface PreCheckPermission {

    /**
     * 需要校验的权限码
     */
    String[] value() default {};

    /**
     * 验证模式：AND | OR，默认AND
     */
    PreMode mode() default PreMode.AND;

    /**
     * 在权限认证不通过时的次要选择，两者只要其一认证成功即可通过校验
     */
    String[] orRole() default {};

    /**
     * 该类、方法是否启用权限校验
     */
    boolean enabled() default true;

}
