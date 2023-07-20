package org.zetaframework.core.xss.serializer;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zetaframework.core.xss.cleaner.XssCleaner;
import org.zetaframework.core.xss.properties.XssProperties;

import java.io.IOException;

/**
 * 自定义用于XSS防护的Json序列化器
 *
 * @author gcc
 */
public class XssStringJsonSerializer extends JsonSerializer<String> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final XssCleaner xssCleaner;
    private final XssProperties xssProperties;
    public XssStringJsonSerializer(XssCleaner xssCleaner, XssProperties xssProperties) {
        this.xssCleaner = xssCleaner;
        this.xssProperties = xssProperties;
    }

    /**
     * 处理类型
     */
    @Override
    public Class<String> handledType() {
        return String.class;
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
        if (StrUtil.isBlank(value)) return;

        try {
            if (xssProperties.getEnabled()) {
                // 清理有XSS风险的文本
                String filterValue = xssCleaner.clear(value);
                jsonGenerator.writeString(filterValue);
            } else {
                jsonGenerator.writeString(value);
            }
        }catch (Exception e) {
            logger.error("序列化失败：[{}]", value, e);
        }
    }
}
