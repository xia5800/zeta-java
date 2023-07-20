package org.zetaframework.core.xss.serializer;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zetaframework.core.xss.cleaner.XssCleaner;
import org.zetaframework.core.xss.properties.XssProperties;

import java.io.IOException;

/**
 * 自定义用于XSS防护的Json反序列化器
 *
 * @author gcc
 */
public class XssStringJsonDeserializer extends JsonDeserializer<String> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final XssCleaner xssCleaner;
    private final XssProperties xssProperties;
    public XssStringJsonDeserializer(XssCleaner xssCleaner, XssProperties xssProperties) {
        this.xssCleaner = xssCleaner;
        this.xssProperties = xssProperties;
    }

    /**
     * 反序列化
     *
     * @param p 用于读取JSON内容的JsonParser对象
     * @param ctxt 可用于访问有关此反序列化活动的信息的上下文。
     * @return
     */
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        if (!p.hasToken(JsonToken.VALUE_STRING)) {
            return null;
        }

        // 获取要清理的文本内容
        String value = p.getValueAsString();
        if (StrUtil.isBlank(value)) return value;

        // XSS防护开关是否关闭
        if (xssProperties.getEnabled()) return value;
        // 当前url是否是忽略xss防护的地址
        if (xssProperties.isIgnoreUrl()) return value;

        // 清理有XSS风险的文本 ps:生产环境打印日志可能会有风险
        logger.debug("正在执行xss反序列化。。。。。需要清理的文本内容为：{}", value);
        return xssCleaner.clear(value);
    }
}
