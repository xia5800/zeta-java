package com.zeta.common.cacheKey;

import com.zeta.common.constants.SystemRedisKeyConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.zetaframework.core.redis.model.StringCacheKey;

import java.time.Duration;

/**
 * 验证码 String类型的缓存key
 *
 * 使用方法：
 * <pre>
 *  // 添加缓存（使用默认的过期时间）
 *  captchaStringCacheKey.set("时间戳", "验证码的值")
 *  // 添加缓存（使用指定的过期时间）
 *  captchaStringCacheKey.set("时间戳", "验证码的值", Duration.ofMinutes(10))
 *
 *  // 获取缓存
 *  String captcha = captchaStringCacheKey.get<String>("时间戳")
 *
 *  // 删除缓存
 *  captchaStringCacheKey.delete("时间戳")
 * </pre>
 *
 * 说明：
 * 更多的方法请查看{@link org.zetaframework.core.redis.model.StringCacheKey}
 *
 * @author gcc
 */
@RequiredArgsConstructor
@Component
public class CaptchaStringCacheKey implements StringCacheKey {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * key 前缀
     */
    @Override
    public String getPrefix() {
        return SystemRedisKeyConstants.CAPTCHA_KEY;
    }

    /**
     * key 过期时间
     */
    @Override
    public Duration getExpire() {
        // 5分钟
        return Duration.ofMinutes(5);
    }

    /**
     * 获取 redisTemplate
     */
    @Override
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

}
