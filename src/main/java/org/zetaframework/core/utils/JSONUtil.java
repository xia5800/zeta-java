package org.zetaframework.core.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 封装Jackson得到的JSON工具类
 *
 * @author gcc
 */
public class JSONUtil {

    private static final Logger logger = LoggerFactory.getLogger(JSONUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectWriter objectWrite = objectMapper.writerWithDefaultPrettyPrinter();

    static  {
        // 禁止:将日期写为时间戳，解决日期格式化问题
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 禁止:FAIL_ON_EMPTY_BEANS
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 允许：枚举使用toString方式
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        // 自定义扩展
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DatePattern.NORM_DATETIME_FORMATTER));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DatePattern.NORM_TIME_FORMATTER));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DatePattern.NORM_DATE_FORMATTER));
        javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);
        javaTimeModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        javaTimeModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
        javaTimeModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DatePattern.NORM_DATETIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DatePattern.NORM_TIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DatePattern.NORM_DATE_FORMATTER));

        // 注册模块
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(javaTimeModule);
    }

    /**
     * 对象转json字符串
     *
     * @param value   对象
     * @return json字符串
     */
    public static String toJsonStr(Object value) {
        return toJsonStr(value, false);
    }

    /**
     * 对象转json字符串
     *
     * @param value 对象
     * @param pretty 是否格式化输出
     * @return json字符串
     */
    public static String toJsonStr(Object value, Boolean pretty) {
        if (value == null) { return null; }
        // 如果对象是字符串，直接返回
        if (value instanceof String) {
            return (String) value;
        }

        try {
            // 是否需要格式化输出
            return pretty ? objectWrite.writeValueAsString(value) : objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            logger.error("对象转json字符串失败", e);
        }
        return null;
    }


    /**
     * json字符串转对象
     *
     * @param json  json字符串
     * @param clazz 对象class
     * @return T
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (StrUtil.isBlank(json)) { return null; }

        try {
            return objectMapper.readValue(json, clazz);
        }catch (Exception e) {
            logger.error("json字符串转对象失败", e);
        }
        return null;
    }

    /**
     * <h1>json字符串转对象</h1>
     * <br>
     * 说明：
     * 用于转换有泛型的对象
     * <br>
     * 使用方法：
     * <pre>
     * // 将jsonStr转成List<SysUser>对象
     * List<SysUser> userList = JSONUtil.parseObject(jsonStr, new TypeReference<List<SysUser>>(){})
     *
     * // 将jsonStr转成ApiResult<Map<String, Object>>对象
     * ApiResult<Map<String, Object>> result = JSONUtil.parseObject(jsonStr, new TypeReference<ApiResult<Map<String, Object>>>(){})
     * </pre>
     * @param json json字符串
     * @param valueTypeRef  值类型参考
     * @return T
     */
    public static <T> T parseObject(String json, TypeReference<T> valueTypeRef) {
        if (StrUtil.isBlank(json)) { return null; }

        try {
            return objectMapper.readValue(json, valueTypeRef);
        }catch (Exception e) {
            logger.error("json字符串转对象失败", e);
        }
        return null;
    }

}
