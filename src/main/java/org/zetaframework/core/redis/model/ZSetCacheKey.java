package org.zetaframework.core.redis.model;

import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ZSet类型的缓存key
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
 * value:
 *      zSet对象的value
 * score:
 *      zSet对象的得分。排序用
 *
 * @author gcc
 */
public interface ZSetCacheKey extends CacheKey {

    default ZSetOperations<String, Object> getZSetOps() {
        return getRedisTemplate().opsForZSet();
    }

    /**
     * 添加元素，有序集合是按照元素的score值由小到大排列
     *
     * @param value  缓存值 不可空
     * @param score  得分  不可空
     */
    default void set(Object value, Double score) {
        set(null, value, score);
    }

    /**
     * 添加元素，有序集合是按照元素的score值由小到大排列
     *
     * @param suffix  key后缀 可空
     *        示例："", "123", "abc:123"
     *        得到的完整key："前缀", "前缀:123", "前缀:abc:123"
     * @param value  缓存值 不可空
     * @param score  得分  不可空
     */
    default void set(Object suffix, Object value, Double score) {
        set(suffix, value, score, getExpire());
    }

    /**
     * 添加元素，有序集合是按照元素的score值由小到大排列
     *
     * @param suffix  key后缀 可空
     *        示例："", "123", "abc:123"
     *        得到的完整key："前缀", "前缀:123", "前缀:abc:123"
     * @param value  缓存值 不可空
     * @param score  得分  不可空
     * @param expire key过期时间 可空 为空不设置过期时间
     */
    default void set(Object suffix, Object value, Double score, Duration expire) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);

        // 缓存key
        getZSetOps().add(key, value, score);

        // 设置key过期
        CacheKeyExt.expire(this, key, expire);
    }

    /**
     * 从有序集中删除values。返回已移除元素的数量
     *
     * @param suffix  key后缀 可空
     * @param value  缓存值 不可空
     */
    default Long delete(Object suffix, Object value) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);
        return getZSetOps().remove(key, value);
    }

    /**
     * 从有序集中删除values。返回已移除元素的数量
     *
     * @param suffix  key后缀 可空
     * @param value  缓存值 不可空
     */
    default Long delete(Object suffix, Collection<Object> value) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);
        return getZSetOps().remove(key, value);
    }

    /**
     * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
     *
     * @param value  缓存值 不可空
     * @return 0表示第一位
     */
    default Long rank(Object value) {
        return rank(null, value);
    }

    /**
     * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
     *
     * @param suffix  key后缀 可空
     * @param value  缓存值 不可空
     * @return 0表示第一位
     */
    default Long rank(Object suffix, Object value) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);
        return getZSetOps().rank(key, value);
    }

    /**
     * 返回元素在集合的排名,按元素的score值由大到小排列
     *
     * @param value  缓存值 不可空
     * @return 0表示第一位
     */
    default Long reverseRank(Object value) {
        return reverseRank(null, value);
    }

    /**
     * 返回元素在集合的排名,按元素的score值由大到小排列
     *
     * @param suffix  key后缀 可空
     * @param value  缓存值 不可空
     * @return 0表示第一位
     */
    default Long reverseRank(Object suffix, Object value) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);
        return getZSetOps().reverseRank(key, value);
    }

    /**
     * 获取集合的元素, 从小到大排序
     *
     * @param start  开始位置 不可空
     * @param end    结束位置 不可空 -1查询所有
     */
    default Set<Object> range(Long start, Long end) {
        return range(null, start, end);
    }


    /**
     * 获取集合的元素, 从小到大排序
     *
     * @param suffix  key后缀 可空
     * @param start  开始位置 不可空
     * @param end    结束位置 不可空 -1查询所有
     */
    default Set<Object> range(Object suffix, Long start, Long end) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);
        return getZSetOps().range(key, start, end);
    }

    /**
     * 根据Score值查询集合元素
     *
     * @param min    最小值
     * @param max    最大值
     */
    default Set<Object> rangeByScore(Double min, Double max) {
        return rangeByScore(null, min, max);
    }
    
    /**
     * 根据Score值查询集合元素
     *
     * @param suffix  key后缀 可空
     * @param min    最小值
     * @param max    最大值
     */
    default Set<Object> rangeByScore(Object suffix, Double min, Double max) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);
        return getZSetOps().rangeByScore(key, min, max);
    }

    /**
     * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
     *
     * @param scoreMembers
     */
    default Long setAll(Map<Object, Double> scoreMembers) {
        return setAll(null, scoreMembers);
    }

    /**
     * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
     *
     * @param suffix  key后缀 可空
     * @param scoreMembers
     */
    default Long setAll(Object suffix, Map<Object, Double> scoreMembers) {
        String key = CacheKeyExt.buildKey(getPrefix(), suffix);

        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();
        scoreMembers.forEach( (score, member) -> {
            tuples.add(new DefaultTypedTuple<>(score, member));
        });
        return getZSetOps().add(key, tuples);
    }

    // 如果需要用到这里没有的方法，可以在这里加，或者使用getZSetOps()自己实现
    
}
