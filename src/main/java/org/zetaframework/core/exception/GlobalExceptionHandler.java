package org.zetaframework.core.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.enums.ErrorCodeEnum;
import org.zetaframework.core.redis.exception.LimitException;

import java.util.HashMap;
import java.util.Objects;

/**
 * 全局异常处理器
 *
 * @author gcc
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 业务异常处理
     *
     * @param ex BusinessException
     * @return ApiResult<?>
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> businessExceptionHandler(BusinessException ex) {
        logger.warn("抛出业务异常：", ex);
        return ApiResult.result(ex.getCode(), ex.getMessage(), null);
    }

    /**
     * 参数异常处理
     *
     * @param ex ArgumentException
     * @return ApiResult<?>
     */
    @ExceptionHandler(ArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> argumentExceptionHandler(ArgumentException ex) {
        logger.warn("抛出参数异常：", ex);
        return ApiResult.result(ex.getCode(), ex.getMessage(), null);
    }

    /**
     * 非法参数异常处理
     *
     * 说明：
     * 主要用于Hutool的Assert断言异常处理
     * @param ex IllegalArgumentException
     * @return ApiResult<?>
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        logger.warn("抛出非法参数异常：", ex);
        return ApiResult.result(ErrorCodeEnum.ERR_ARGUMENT.getCode(), ex.getMessage(), null);
    }

    /**
     * 限流异常处理
     *
     * 说明：
     * 主要用于接口限流注解[Limit]的异常处理
     * @param ex LimitException
     * @return ApiResult<?>
     */
    @ExceptionHandler(LimitException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> limitExceptionHandler(LimitException ex) {
        logger.warn("抛出接口限流异常：", ex);
        return ApiResult.result(ex.getCode(), ex.getMessage(), null);
    }

    /**
     * 绑定异常
     *
     * @param ex BindException
     * @return ApiResult<?>
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> bindExceptionHandler(BindException ex) {
        logger.warn("抛出绑定异常：", ex);
        try {
            String msg = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
            if (StrUtil.isNotEmpty(msg)) {
                return ApiResult.result(ErrorCodeEnum.ERR_BIND_EXCEPTION.getCode(), msg, ex.getMessage());
            }
        } catch (Exception e) {
            logger.warn("获取异常描述失败", e);
        }

        StringBuilder msg = new StringBuilder();
        ex.getFieldErrors().forEach(it -> {
            HashMap<Object, Object> map = MapUtil.newHashMap(3);
            map.put("objectName", it.getObjectName());
            map.put("field", it.getField());
            map.put("rejectedValue", it.getRejectedValue());
            msg.append(StrUtil.format("参数：{objectName}.{field}的传入值:[{rejectedValue}]与预期的字段类型不匹配.", map));
        });

        return ApiResult.result(ErrorCodeEnum.ERR_BIND_EXCEPTION.getCode(), msg.toString(), ex.getMessage());
    }

    /**
     * 方法参数类型不匹配异常
     *
     * @param ex MethodArgumentTypeMismatchException
     * @return ApiResult<?>
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        logger.warn("抛出方法参数类型不匹配异常：", ex);

        String typeName = ex.getRequiredType() == null ? StrUtil.EMPTY : ex.getRequiredType().getName();
        HashMap<Object, Object> map = MapUtil.newHashMap(3);
        map.put("param", ex.getName());
        map.put("value", ex.getValue());
        map.put("typeName", typeName);
        String msg = StrUtil.format("参数：[{param}]的传入值：[{value}]与预期的字段类型：[{typeName}]不匹配", map);

        return ApiResult.result(ErrorCodeEnum.ERR_ARGUMENT_TYPE_MISMATCH_EXCEPTION.getCode(), msg, ex.getMessage());
    }

    /**
     * 从请求中读取数据失败异常
     *
     * @param ex HttpMessageNotReadableException
     * @return ApiResult<?>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.warn("抛出从请求中读取数据失败异常：", ex);
        String msg = ex.getMessage();
        if (StrUtil.containsAny(msg, "Could not read document:")) {
            String sub = StrUtil.subBetween(msg, "Could not read document:", " at ");
            msg = "无法正确的解析json类型的参数：" + sub;
        }

        // Controller接口参数为空
        if (StrUtil.containsAny(msg, "Required request body is missing")) {
            msg = "请求参数为空";
        }

        // 接口参数枚举值不正确
        if (StrUtil.containsAny(msg, "not one of the values accepted for Enum class")) {
            msg = "枚举参数值不正确";
        }
        return ApiResult.result(ErrorCodeEnum.ERR_REQUEST_PARAM_EXCEPTION.getCode(), msg, ex.getMessage());
    }

    /**
     * 登录认证异常
     *
     * @param ex NotLoginException
     * @return ApiResult<?>
     */
    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResult<?> notLoginExceptionHandler(NotLoginException ex) {
        logger.warn("抛出登录认证异常：", ex);

        String message = "";
        switch (ex.getType()) {
            case NotLoginException.NOT_TOKEN:
                message = NotLoginException.NOT_TOKEN_MESSAGE;
                break;
            case NotLoginException.INVALID_TOKEN:
                message = NotLoginException.INVALID_TOKEN_MESSAGE;
                break;
            case NotLoginException.TOKEN_TIMEOUT:
                message = NotLoginException.TOKEN_TIMEOUT_MESSAGE;
                break;
            case NotLoginException.BE_REPLACED:
                message = NotLoginException.BE_REPLACED_MESSAGE;
                break;
            case NotLoginException.KICK_OUT:
                message = NotLoginException.KICK_OUT_MESSAGE;
                break;
            case NotLoginException.TOKEN_FREEZE:
                message = NotLoginException.TOKEN_FREEZE_MESSAGE;
                break;
            case NotLoginException.NO_PREFIX:
                message = NotLoginException.NO_PREFIX_MESSAGE;
                break;
            default:
                message = NotLoginException.DEFAULT_MESSAGE;
                break;
        }

        return ApiResult.result(ErrorCodeEnum.UNAUTHORIZED.getCode(), message, null).setError(ex.getMessage());
    }


    /**
     * 角色认证异常处理
     *
     * @param ex SaTokenException
     * @return ApiResult<?>
     */
    @ExceptionHandler(NotRoleException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResult<?> notRoleExceptionHandler(NotRoleException ex) {
        logger.warn("抛出角色认证异常：", ex);
        return ApiResult.result(ErrorCodeEnum.FORBIDDEN.getCode(), ErrorCodeEnum.FORBIDDEN.getMsg(), null);
    }

    /**
     * 权限认证异常处理
     *
     * @param ex SaTokenException
     * @return ApiResult<?>
     */
    @ExceptionHandler(NotPermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResult<?> notPermissionExceptionHandler(NotPermissionException ex) {
        logger.warn("抛出权限认证异常：", ex);
        return ApiResult.result(ErrorCodeEnum.FORBIDDEN.getCode(), ErrorCodeEnum.FORBIDDEN.getMsg(), null);
    }


    /**
     * 其它异常
     * @param ex Exception
     * @return ApiResult<?>
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> otherExceptionHandler(Exception ex) {
        logger.warn("抛出其它异常：", ex);
        return ApiResult.result(ErrorCodeEnum.SYSTEM_BUSY.getCode(), ex.getMessage(), null);
    }

}
