package org.zetaframework.core.redis.model;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.zetaframework.core.redis.helper.NullVal;

import java.time.Duration;
import java.util.Map;

/**
 * 缓存key 扩展类
 *
 * @author gcc
 */
public final class CacheKeyExt {


    /**
     * 构造完整key
     *
     * @param prefix 前缀 不可空
     * @param suffix key后缀 可空
     */
    public static String buildKey(String prefix, Object suffix) {
        return ObjectUtil.isNotEmpty(suffix) ? StrUtil.join(StrPool.COLON, prefix, suffix) : prefix;
    }

    /**
     * 设置key过期
     *
     * @param key    完整的key 不可空
     * @param expire 超时时间 可空 为空不设置key过期
     */
    public static void expire(CacheKey cacheKey, String key, Duration expire) {
        if (expire == null) return;

        cacheKey.getRedisTemplate().expire(key, expire);
    }

    /**
     * 判断缓存值是否为空对象
     *
     * @param value T
     * @return Boolean
     */
    public static <T> Boolean isNullVal(T value) {
        boolean isNull = value == null || NullVal.class.equals(value.getClass());
        return isNull || value.getClass().equals(Object.class) || (value instanceof Map && ((Map<?, ?>) value).isEmpty());
    }

    /**
     * 创建一个空对象
     *
     * @return NullVal
     */
    public static NullVal createNullVal() {
        return new NullVal();
    }

    /**
     * 返回正常值或null值
     *
     * @param value T
     * @return T
     */
    public static <T> T returnVal(T value) {
        return isNullVal(value) ? null : value;
    }

}
