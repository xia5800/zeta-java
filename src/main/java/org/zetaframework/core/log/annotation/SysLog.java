package org.zetaframework.core.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 系统日志（操作、异常日志）
 *
 * 说明：
 * 1. 主要用于记录用户的操作
 * 2. 配合swagger的@Api和 @ApiOperation注解使用
 * 3. 只能用于方法上
 *
 * 使用方式：
 * <pre>{@code
 *      // 使用方式一
 *      @Api(tags = "用户管理")  // 类上的注解
 *      @ApiOperation(value = "分页查询")
 *      @SysLog // 可以不写value值。 默认使用@ApiOperation注解的value值
 *      public ApiResult<PageResult<User>> page(PageParam<UserQueryParam> params) { ... }
 *
 *      // 使用方式二
 *      @SysLog(value = "分页查询用户")
 *      public ApiResult<PageResult<User>> page(PageParam<UserQueryParam> params) { ... }
 *
 *      // 关闭记录请求参数
 *      @SysLog(value = "" , request = false)
 *
 *      // 关闭记录返回值
 *      @SysLog(value = "" , response = false)
 *
 *      // 关闭记录操作日志
 *      @SysLog(enable = false)
 * }</pre>
 * @author gcc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface SysLog {

    /**
     * 操作描述
     */
    String value() default  "";

    /**
     * 是否记录方法的入参
     */
    boolean request() default true;

    /**
     * 是否记录返回值
     */
    boolean response() default true;

    /**
     * 是否记录操作日志
     */
    boolean enabled() default true;
}
