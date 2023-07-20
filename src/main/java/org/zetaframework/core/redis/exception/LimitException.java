package org.zetaframework.core.redis.exception;

import cn.hutool.core.util.StrUtil;
import org.zetaframework.core.enums.ErrorCodeEnum;

/**
 * 自定义限流异常 - 接口请求频繁
 *
 * @author gcc
 */
public class LimitException extends RuntimeException {
    /** 异常编码 */
    private Integer code = ErrorCodeEnum.TOO_MANY_REQUESTS.getCode();

    public LimitException() {
        super(ErrorCodeEnum.TOO_MANY_REQUESTS.getMsg());
    }

    public LimitException(String message) {
        super(message);
    }

    /**
     * 支持message中包含"{}"占位符
     * @param format
     * @param args
     */
    public LimitException(String format, Object... args) {
        super(StrUtil.contains(format, "{}") ? StrUtil.format(format, args) : String.format(format, args));
    }

    public LimitException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public LimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public LimitException(Throwable cause) {
        super(cause);
    }

    public LimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
