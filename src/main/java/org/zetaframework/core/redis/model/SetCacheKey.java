package org.zetaframework.core.redis.model;

import org.springframework.data.redis.core.SetOperations;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;

/**
 * Set类型的缓存key
 *
 * 说明：
 * 只封装了几个特别常用的方法
 *
 * 使用前先搞懂几个概念：
 * prefix:
 *      顾名思义，缓存key的前缀。 例如：system:captcha、user、msg:push:send  注意：前缀不要有结尾的冒号
 * suffix:
 *      顾名思义，缓存key的后缀。 是一些动态的值(也可以是固定的值)。 例如：用户的id、当前的时间戳、abc:def、md5值等
 * key:
 *      缓存key，即redis的key。 key = prefix + ":" + suffix。 例如： user:9527、system:captcha:1348646464等
 * @author gcc
 */
public interface SetCacheKey extends CacheKey {

    default SetOperations<String, Object> getSetOps() {
        return getRedisTemplate().opsForSet();
    }

    /**
     * set集合添加元素
     *
     * @param value  缓存值 不可空
     */
    default void set (Object value) {
        set(null, value);
    }

    /**
     * set集合添加元素
     *
     * @param suffix  key后缀 可空
     *        示例："", "123", "abc:123"
     *        得到的完整key："前缀", "前缀:123", "前缀:abc:123"
     * @param value  缓存值 不可空
     */
    default void set(Object suffix, Object value) {
        set(suffix, value, getExpire());
    }

    /**
     * set集合添加元素
     *
     * @param suffix  key后缀 可空
     *        示例："", "123", "abc:123"
     *        得到的完整key："前缀", "前缀:123", "前缀:abc:123"
     * @param value  缓存值 不可空
     * @param expire key过期时间 可空 为空不设置过期时间
     */
    default void set(Object suffix, Object value, Duration expire) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);

        // 缓存key
        getSetOps().add(key, value);

        // 设置key过期
        CacheKeyExt.expire(this, key, expire);
    }

    /**
     * 返回集合 key 中的所有成员。
     */
    default Set<Object> get() {
        return get(null);
    }

    /**
     * 返回集合 key 中的所有成员。
     *
     * @param suffix   key后缀 可空
     */
    default Set<Object> get(Object suffix) {
        return getSetOps().members(CacheKeyExt.buildKey(getPrefix(), suffix));
    }

    /**
     * 删除集合中的指定元素
     *
     * @param suffix   key后缀 可空
     * @param value   缓存值 不可空
     */
    default Long delete(Object suffix, Object value) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);
        return getSetOps().remove(key, value);
    }

    /**
     * 判断集合是否包含value
     *
     * @param value   缓存值 不可空
     */
    default Boolean isMember(Object value) {
        return isMember(null, value);
    }

    /**
     * 判断集合是否包含value
     *
     * @param suffix   key后缀 可空
     * @param value   缓存值 不可空
     */
    default Boolean isMember(Object suffix, Object value) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);
        return getSetOps().isMember(key, value);
    }

    /**
     * 删除多个元素
     *
     * @param suffix   key后缀 可空
     * @param values   缓存值 不可空
     */
    default Long deleteByValues(Object suffix, Collection<Object> values) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);
        return getSetOps().remove(key, values);
    }

    // 如果需要用到这里没有的方法，可以在这里加，或者使用getSetOps()自己实现

}
