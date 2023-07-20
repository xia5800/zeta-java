package org.zetaframework.core.exception;

import cn.hutool.core.util.StrUtil;
import org.zetaframework.core.enums.ErrorCodeEnum;

/**
 * 自定义异常 - 业务异常
 *
 * @author gcc
 */
public class BusinessException extends RuntimeException {

    /** 异常编码 */
    private Integer code = ErrorCodeEnum.ERR_BUSINESS.getCode();

    public BusinessException() {
        super(ErrorCodeEnum.ERR_BUSINESS.getMsg());
    }

    public BusinessException(String message) {
        super(message);
    }

    /**
     * 支持message中包含"{}"占位符
     * @param format
     * @param args
     */
    public BusinessException(String format, Object... args) {
        super(StrUtil.contains(format, "{}") ? StrUtil.format(format, args) : String.format(format, args));
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
