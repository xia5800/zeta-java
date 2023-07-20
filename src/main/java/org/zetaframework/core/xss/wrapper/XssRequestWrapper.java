package org.zetaframework.core.xss.wrapper;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.zetaframework.core.xss.cleaner.XssCleaner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 自定义用于XSS防护的 请求包装器
 *
 * @author gcc
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {

    private final XssCleaner xssCleaner;
    public XssRequestWrapper(HttpServletRequest request, XssCleaner xssCleaner) {
        super(request);
        this.xssCleaner = xssCleaner;
    }

    /**
     * 重写getParameterMap方法，实现XSS防御
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> requestMap = super.getParameterMap();
        Map<String, String[]> result = new HashMap<>();

        // 处理参数值
        requestMap.forEach( (key, values) -> {
                if (ArrayUtil.isNotEmpty(values)) {
                    // result.put(key, 清理过的value)
                    String[] cleanValues = Arrays.stream(values)
                            .map(xssCleaner::clear)
                            .collect(Collectors.toList())
                            .toArray(new String[values.length]);
                    result.put(key, cleanValues);
                } else {
                    result.put(key, values);
                }
            }
        );

        return result;
    }

    /**
     * 重写getQueryString方法，实现XSS防御
     */
    @Override
    public String getQueryString() {
        String value = super.getQueryString();
        return StrUtil.isBlank(value) ? value : xssCleaner.clear(value);
    }

    /**
     * 重写getParameterValues方法，实现XSS防御
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (ArrayUtil.isEmpty(values)) return new String[0];

        return Arrays.stream(values)
                .map(xssCleaner::clear)
                .collect(Collectors.toList())
                .toArray(new String[values.length]);
    }

    /**
     * 重写getParameter方法，实现XSS防御
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return StrUtil.isBlank(value) ? value : xssCleaner.clear(value);
    }

    /**
     * 重写getHeader方法，实现XSS防御
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return StrUtil.isBlank(value) ? value : xssCleaner.clear(value);
    }
}
