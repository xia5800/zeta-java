package org.zetaframework.core.redis.model;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

/**
 * 缓存key
 *
 * @author gcc
 */
public interface CacheKey {

    /**
     * key 前缀
     */
    String getPrefix();

    /**
     * key 过期时间
     */
    default Duration getExpire() {
        return null;
    }

    /**
     * 获取redisTemplate
     */
    RedisTemplate<String, Object> getRedisTemplate();

}
