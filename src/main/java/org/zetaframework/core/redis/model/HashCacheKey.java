package org.zetaframework.core.redis.model;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.data.redis.core.HashOperations;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Hash(Map)类型的缓存key
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
 * hashKey:
 *      你可以理解为HashMap的key。不能为空
 * value:
 *      你可以理解为HashMap的value。可以为空
 *
 * @author gcc
 */
public interface HashCacheKey extends CacheKey {

    default HashOperations<String, Object, Object> getHashOps() {
        return getRedisTemplate().opsForHash();
    }

    /**
     * 将哈希表key中的字段hashKey的值设为value
     *
     * @param hashKey hash表中的key 不可空
     * @param value  缓存值  可空  为空缓存NullVal对象
     */
    default void set(Object hashKey, Object value) {
        set("", hashKey, value, getExpire());
    }

    /**
     * 将哈希表key中的字段hashKey的值设为value
     *
     * @param suffix  key后缀 可空
     *        示例："", "123", "abc:123"
     *        得到的完整key："前缀", "前缀:123", "前缀:abc:123"
     * @param hashKey hash表中的key 不可空
     * @param value  缓存值  可空  为空缓存NullVal对象
     */
    default void set(Object suffix, Object hashKey, Object value) {
        set(suffix, hashKey, value, getExpire());
    }

    /**
     * 将哈希表key中的字段hashKey的值设为value
     *
     * @param suffix  key后缀 可空
     *        示例："", "123", "abc:123"
     *        得到的完整key："前缀", "前缀:123", "前缀:abc:123"
     * @param hashKey hash表中的key 不可空
     * @param value  缓存值  可空  为空缓存NullVal对象
     * @param expire key过期时间 可空 为空不设置过期时间
     */
    default void set(Object suffix, Object hashKey, Object value, Duration expire) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);

        // 缓存key
        value = ObjectUtil.isNull(value) ? CacheKeyExt.createNullVal() : value;
        getHashOps().put(key, hashKey, value);

        // 设置key过期
        CacheKeyExt.expire(this, key, expire);
    }

    /**
     * 获取指定key对应的hash表
     */
    default Map<Object, Object> get() {
        return get(null);
    }

    /**
     * 获取指定key对应的hash表
     *
     * @param suffix   key后缀 可空
     */
    default Map<Object, Object> get(Object suffix) {
        return getHashOps().entries(CacheKeyExt.buildKey(getPrefix(), suffix));
    }

    /**
     * 获取存储在哈希表中指定hashKey对应的值
     *
     * @param hashKey  hash表中的key 不可空
     */
    default <T> T getValue(Object hashKey) {
        return getValue(null, hashKey);
    }

    /**
     * 获取存储在哈希表中指定hashKey对应的值
     *
     * @param suffix   key后缀 可空
     * @param hashKey  hash表中的key 不可空
     */
    default <T> T getValue(Object suffix, Object hashKey) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);
        // opsForHash方法两个泛型分别代表: hashKey类型、hashKey对应value的类型
        return CacheKeyExt.returnVal((T) getHashOps().get(key, hashKey));
    }

    /**
     * 删除指定hashKey
     *
     * @param suffix   key后缀 可空
     * @param hashKey  hash表中的key 不可空
     */
    default Long delete(Object suffix, Object... hashKey) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);
        return getHashOps().delete(key, hashKey);
    }

    /**
     * 获取哈希表中的所有hashkey
     *
     * @param suffix key后缀 可空
     * @return hashKeySet
     */
    default Set<Object> getKeys(Object suffix) {
        return getHashOps().keys(CacheKeyExt.buildKey(getPrefix(), suffix));
    }

    /**
     * 获取哈希表中的所有value
     * @return valueList
     */
    default List<Object> getValues() {
        return getValues(null);
    }

    /**
     * 获取哈希表中的所有value
     *
     * @param suffix   key后缀 可空
     * @return valueList
     */
    default List<Object> getValues(Object suffix) {
        return getHashOps().values(CacheKeyExt.buildKey(getPrefix(), suffix));
    }

    /**
     * 获取存储在哈希表中指定hashKey对应的值
     *
     * @param hashKeys hash表中的key 不可空
     */
    default List<Object> getValues(Collection<Object> hashKeys) {
        return getValues(null, hashKeys);
    }

    /**
     * 获取存储在哈希表中指定hashKey对应的值
     *
     * @param suffix   key后缀 可空
     * @param hashKeys hash表中的key 不可空
     */
    default List<Object> getValues(Object suffix, Collection<Object> hashKeys) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);
        return getHashOps().multiGet(key, hashKeys);
    }

    default void setAll(Map<Object, Object> keyValues) {
        setAll(null, keyValues);
    }

    /**
     * 将多个键值对设置到hash表中
     *
     * @param suffix    key后缀 可空
     * @param keyValues 键值对 键不可空 值可空
     */
    default void setAll(Object suffix, Map<Object, Object> keyValues) {
        setAll(suffix, keyValues, getExpire());
    }

    /**
     * 将多个键值对设置到hash表中
     *
     * @param suffix    key后缀 可空
     * @param keyValues 键值对 键不可空 值可空
     * @param expire    过期时间 可空 为空不设置过期时间
     */
    default void setAll(Object suffix, Map<Object, Object> keyValues, Duration expire) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);

        // 缓存key
        getHashOps().putAll(key, keyValues);

        // 设置key过期
        CacheKeyExt.expire(this, key, expire);
    }

    // 如果需要用到这里没有的方法，可以在这里加，或者使用getRedisTemplate()自己实现
}
