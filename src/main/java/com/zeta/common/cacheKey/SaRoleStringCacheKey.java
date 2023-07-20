package com.zeta.common.cacheKey;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.zetaframework.core.constants.RedisKeyConstants;
import org.zetaframework.core.redis.model.StringCacheKey;

import java.time.Duration;

/**
 * 用户角色 String类型的缓存key
 *
 * @author gcc
 */
@RequiredArgsConstructor
@Component
public class SaRoleStringCacheKey implements StringCacheKey {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * key 前缀
     */
    @Override
    public String getPrefix() {
        return RedisKeyConstants.USER_ROLE_KEY;
    }

    /**
     * key 过期时间
     */
    @Override
    public Duration getExpire() {
        // 1天
        return Duration.ofDays(1);
    }

    /**
     * 获取redisTemplate
     */
    @Override
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

}
