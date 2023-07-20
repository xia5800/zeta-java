package org.zetaframework.base.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.zetaframework.core.enums.ErrorCodeEnum;

/**
 * 返回结果
 *
 * @author gcc
 */
@ApiModel(description = "返回结果")
public class ApiResult<T> {

    /** 状态码 */
    @ApiModelProperty(value = "状态码")
    private Integer code;

    /** 是否成功 */
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    /** 状态信息 */
    @ApiModelProperty(value = "状态信息")
    private String message;

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private T data;

    /** 错误信息 */
    @ApiModelProperty(value = "错误信息")
    private String error;

    /** 是否执行默认操作 */
    @JsonIgnore
    @ApiModelProperty(value = "是否执行默认操作", hidden = true)
    private Boolean defExec = false;

    public ApiResult() {}

    public ApiResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.defExec = false;
    }

    public ApiResult(Integer code, String message, T data, Boolean defExec) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.defExec = defExec;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return this.code == ErrorCodeEnum.SUCCESS.getCode();
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public ApiResult<T> setError(String error) {
        this.error = error;
        return this;
    }

    public Boolean getDefExec() {
        return defExec;
    }

    public void setDefExec(Boolean defExec) {
        this.defExec = defExec;
    }

    /**
     * 返回结果
     * @param code 状态码
     * @param message 状态信息
     * @param data 返回数据
     * @return ApiResult<T>
     */
    public static <T> ApiResult<T> result(Integer code, String message, T data) {
        return new ApiResult<>(code, message, data);
    }

    /**
     * 请求成功
     * @return ApiResult<T>
     */
    public static <T> ApiResult<T> success() {
        return success(null);
    }

    /**
     * 请求成功
     * @param data 返回数据
     * @return ApiResult<T>
     */
    public static <T> ApiResult<T> success(T data) {
        return success(ErrorCodeEnum.SUCCESS.getMsg(), data);
    }

    /**
     * 请求成功
     * @param message 状态信息
     * @param data 返回数据
     * @return ApiResult<T>
     */
    public static <T> ApiResult<T> success(String message, T data) {
        return result(ErrorCodeEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 请求成功，需要执行默认操作
     * @return ApiResult<Entity>
     */
    public static <T> ApiResult<T> successDef() {
        return new ApiResult<>(ErrorCodeEnum.SUCCESS.getCode(), ErrorCodeEnum.SUCCESS.getMsg(), null, true);
    }

    public static <T> ApiResult<T> fail() {
        return fail(null);
    }

    /**
     * 请求失败
     * @param message 状态信息
     * @return ApiResult<T>
     */
    public static <T> ApiResult<T> fail(String message) {
        return fail(message, null);
    }

    /**
     * 请求失败
     * @param message 状态信息
     * @param data 返回数据
     * @return ApiResult<T>
     */
    public static <T> ApiResult<T> fail(String message, T data) {
        return result(ErrorCodeEnum.FAIL.getCode(), message, data);
    }

}
