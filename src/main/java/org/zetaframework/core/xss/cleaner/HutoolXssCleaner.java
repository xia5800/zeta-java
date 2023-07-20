package org.zetaframework.core.xss.cleaner;

import cn.hutool.http.HtmlUtil;

/**
 * 使用hutool工具类进行XSS文本清理 接口实现
 *
 * @author gcc
 */
public class HutoolXssCleaner implements XssCleaner {

    /**
     * 清理有XSS风险的文本
     *
     * @param value 有风险的xss文本
     * @return 清理后的xss文本
     */
    @Override
    public String clear(String value) {
        return HtmlUtil.filter(value);
    }
}
