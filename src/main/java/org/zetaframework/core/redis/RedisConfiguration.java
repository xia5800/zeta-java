package org.zetaframework.core.redis;

import cn.hutool.core.text.StrPool;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.zetaframework.core.jackson.ZetaObjectMapper;
import org.zetaframework.core.redis.helper.RedisHelper;
import org.zetaframework.core.redis.serializer.CustomJackson2JsonRedisSerializer;

import java.time.Duration;

/**
 * redis 配置类
 *
 * @author gcc
 */
@EnableCaching
@Configuration
public class RedisConfiguration {

    /**
     * 配置spring cache管理器
     * @param connectionFactory RedisConnectionFactory
     * @return CacheManager
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory)  {
        CustomJackson2JsonRedisSerializer<Object> jsonRedisSerializer = getRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                // 缓存双冒号变成单冒号  test::key::123  -> test:key:123
                .computePrefixWith(prefix -> prefix.concat(StrPool.COLON))
                // 禁用空值
                .disableCachingNullValues()
                // 使用StringRedisSerializer来序列化和反序列化redis的key值
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
                // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonRedisSerializer))
                // 缓存有效期
                .entryTtl(Duration.ofDays(1));
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(configuration).build();
    }


    /**
     * 配置 RedisTemplate
     * @param connectionFactory RedisConnectionFactory
     * @return RedisTemplate<String, Object>
     */
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        CustomJackson2JsonRedisSerializer<Object> jsonRedisSerializer = getRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);

        return redisTemplate;
    }


    /**
     * 配置 StringRedisTemplate
     * @param connectionFactory RedisConnectionFactory
     * @return StringRedisTemplate
     */
    @Bean("stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        return stringRedisTemplate;
    }

    /**
     * 配置Redis帮助类
     * @param redisTemplate RedisTemplate<String, Object>
     * @return RedisOps
     */
    @Bean
    public RedisHelper redisHelper(RedisTemplate<String, Object> redisTemplate) {
        return new RedisHelper(redisTemplate);
    }

    /**
     * 获取配置好的 Jackson2JsonRedisSerializer对象
     *
     * @return
     */
    private CustomJackson2JsonRedisSerializer<Object> getRedisSerializer()  {
        CustomJackson2JsonRedisSerializer<Object> jsonRedisSerializer = new CustomJackson2JsonRedisSerializer<>(Object.class);
        jsonRedisSerializer.setObjectMapper(ZetaObjectMapper.objectMapper);
        return jsonRedisSerializer;
    }

}
