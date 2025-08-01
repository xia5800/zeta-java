package org.zetaframework.extra.desensitization.serializer;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zetaframework.extra.desensitization.annotation.Desensitization;
import org.zetaframework.extra.desensitization.utils.DesensitizationUtil;

import java.io.IOException;
import java.util.Objects;

/**
 * 用于字段数据脱敏的Json序列化器
 *
 * @author gcc
 */
public class DesensitizationJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String rule = "";
    private String symbol = "";

    public DesensitizationJsonSerializer() {
        // Spring容器加载Bean使用，勿删
    }

    public DesensitizationJsonSerializer(String rule, String symbol) {
        this.rule = rule;
        this.symbol = symbol;
    }

    /**
     * 序列化
     *
     * @param value 要序列化的值
     * @param jsonGenerator json内容生成器
     * @param serializers 可用于获取用于序列化对象的值包含的序列化器的提供程序 (如果有)。
     */
    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        try {
            jsonGenerator.writeString(DesensitizationUtil.deserialization(value, rule, symbol));
        } catch (Exception e) {
            logger.error("序列化失败：[{}]", value, e);
        }
    }

    /**
     * 创建上下文
     *
     * @param prov 序列化器
     * @param property Bean字段
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property == null) return prov.findNullValueSerializer(null);

        // 如果字段类型不是String
        if (!Objects.equals(property.getType().getRawClass(), String.class)) {
            return prov.findNullValueSerializer(property);
        }

        // 获取字段上的@Desensitization注解
        Desensitization desensitization = property.getAnnotation(Desensitization.class);
        if (desensitization == null) {
            desensitization = property.getContextAnnotation(Desensitization.class);
        }

        // 如果获取到，构建DesensitizationJsonSerializer对象
        if (desensitization != null) {
            return new DesensitizationJsonSerializer(
                    desensitization.rule(),
                    desensitization.symbol()
            );
        }

        return prov.findValueSerializer(property.getType(), property);
    }
}
