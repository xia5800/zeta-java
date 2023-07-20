package org.zetaframework.core.redis.serializer;

import cn.hutool.core.util.ArrayUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;
import org.zetaframework.core.redis.helper.NullVal;

/**
 * 自定义Jackson2JsonRedisSerializer类
 *
 * 说明：
 * 1.该类的实现参考了Jackson2JsonRedisSerializer类
 * 2.为什么要自定义这个类？是为了反序列化时把{@link NullVal}对象转换成null对象
 *
 * 为什么要自定义这个类：
 * 为了防止缓存穿透，redisUtil中将null值替换成了{@link NullVal}对象。
 * 然后就导致同时使用Spring注解@Cacheable和自定义的CacheKey（使用RedisUtil也一样）缓存了null值之后，
 * “@Cacheable”在读取缓存值时会抛出异常，提示“{@link NullVal}对象不能转换为XXX对象”问题。
 *
 * 解决方法也很简单：
 * 在反序列化对象时，判断对象是否是{@link NullVal}对象，如果是则返回null。否则返回对象值
 *
 * @author gcc
 */
public class CustomJackson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    /** java类型 */
    private final JavaType javaType;
    /**
     * 默认初始化一个ObjectMapper对象。（啥都没配置的那种）
     */
    private ObjectMapper objectMapper = new ObjectMapper();


    public CustomJackson2JsonRedisSerializer(Class<T> type) {
        this.javaType = getJavaType(type);
    }

    public CustomJackson2JsonRedisSerializer(JavaType javaType) {
        this.javaType = javaType;
    }

    /**
     * 利用Jackson的ObjectMapper将对象序列化成二进制数据
     *
     * @param t 要序列化的对象。可以为空。
     * @return 等效的二进制数据。可以为空。
     */
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) { return new byte[0]; }

        try {
            return objectMapper.writeValueAsBytes(t);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    /**
     * 从给定的二进制数据反序列化对象
     *
     * @param bytes 对象的二进制数据。可以为空。
     * @return 等价的对象实例。可以为空。
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || ArrayUtil.isEmpty(bytes)) { return null; }

        try {
            // 得到序列化对象的值
            T value = objectMapper.readValue(bytes, 0, bytes.length, javaType);

            // 判断对象是否为null或NullVal对象。
            return isNullVal(value) ? null : value;
        } catch (Exception ex) {
            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    /**
     * 设置一个自定义的ObjectMapper对象
     *
     * @param objectMapper 自定义的ObjectMapper
     */
    public void setObjectMapper(ObjectMapper objectMapper) {

        Assert.notNull(objectMapper, "'objectMapper' must not be null");
        this.objectMapper = objectMapper;
    }

    /**
     * 获取java类型
     *
     * @param clazz Class<T>
     */
    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }

    /**
     * 判断对象是否为空对象(null or [NullVal])
     *
     * @param value T
     * @return Boolean
     */
    private <T> Boolean isNullVal(T value) {
        return value == null || NullVal.class == value.getClass();
    }
}
