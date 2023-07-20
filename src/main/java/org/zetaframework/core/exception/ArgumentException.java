package org.zetaframework.core.exception;

import cn.hutool.core.util.StrUtil;
import org.zetaframework.core.enums.ErrorCodeEnum;

/**
 * 自定义异常 - 参数异常
 *
 * @author gcc
 */
public class ArgumentException extends RuntimeException {

    /** 异常编码 */
    private Integer code = ErrorCodeEnum.ERR_ARGUMENT.getCode();

    public ArgumentException() {
        super(ErrorCodeEnum.ERR_ARGUMENT.getMsg());
    }

    public ArgumentException(String message) {
        super(message);
    }

    /**
     * 支持message中包含"{}"占位符
     * @param format
     * @param args
     */
    public ArgumentException(String format, Object... args) {
        super(StrUtil.contains(format, "{}") ? StrUtil.format(format, args) : String.format(format, args));
    }

    public ArgumentException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentException(Throwable cause) {
        super(cause);
    }

    public ArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
