package org.zetaframework.core.redis.annotation;

import org.zetaframework.core.redis.enmus.LimitType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>接口限流</h1>
 *
 * 使用方式：
 * <pre>{@code
 *     // 登录限流，1分钟只允许调用3次。 => 限流的redisKey为：`limit:login123:{ip}`
 *     @Limit(name = "登录限流", key = "login123", period = "60", count = "3")
 *     public ApiResult<LoginResult> login(loginParam: LoginParam) {}
 *
 *     // 获取当前登录用户信息，1分钟只允许调用3次 => 限流的redisKey为：`limit:userInfo:{userId}`
 *     @Limit(period = "60", count = "3", limitType = LimitType.USERID)
 *     public ApiResult<SysUser> userInfo() {}
 *
 *     // 最简单的写法。1分钟只允许调用10次 => 限流的redisKey为：`limit:custom:{ip}`
 *     @Limit
 *     public ApiResult<Boolean> custom() {}
 *
 *     // 修改限流后返回描述
 *     @Limit(describe = "哎呀呀，你的请求太频繁了，请稍后再试哦")
 *     public ApiResult<Boolean> custom() {}
 * }</pre>
 *
 * @author gcc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface Limit {

    /**
     * 名字, 当做备注用吧
     */
    String name() default "";

    /**
     * key
     *
     * 说明：为空自动设置方法名, 详情见prefix注释
     */
    String key() default "";

    /**
     * # Key的前缀
     *
     * 说明：
     *
     * 最终的redis限流key =  key前缀 + ":" + 处理后的key
     *
     * 例如：
     * ```
     * limit:login:127.0.0.1   (IP限流，key为""的情况下自动获取方法名)
     * limit:getUserInfo:1     (USERID限流，key为"getUserInfo")
     * ```
     */
    String prefix() default "limit";

    /**
     * 给定的时间范围 单位(秒)
     */
    int period() default 60;

    /**
     * 一定时间内最多访问次数
     */
    int count() default 10;

    /**
     * 限流的类型(用户id 或者 请求ip)
     */
    LimitType limitType() default LimitType.IP;

    /**
     * 限流后返回描述
     */
    String describe() default "你的访问过于频繁，请稍后再试";

    /**
     * 是否启动限流，用于那些不想注释代码的人
     */
    boolean enabled() default true;

}
