package org.zetaframework.base.controller;

import org.zetaframework.base.result.ApiResult;

/**
 * 基础接口
 *
 * @author gcc
 */
public interface SuperBaseController {

    /**
     * 返回成功
     *
     * @return ApiResult<T>
     */
    default <T> ApiResult<T> success() {
        return ApiResult.success();
    }

    /**
     * 返回成功
     *
     * @param data T
     * @return ApiResult<T>
     */
    default <T> ApiResult<T> success(T data) {
        return ApiResult.success(data);
    }

    /**
     * 返回成功
     *
     * @param message String
     * @param data T
     * @return ApiResult<T>
     */
    default <T> ApiResult<T> success(String message, T data) {
        return ApiResult.success(message, data);
    }

    /**
     * 返回失败
     *
     * @return ApiResult<T>
     */
    default <T> ApiResult<T> fail() {
        return ApiResult.fail();
    }

    /**
     * 返回失败
     *
     * @param message String
     * @return ApiResult<T>
     */
    default <T> ApiResult<T> fail(String message) {
        return ApiResult.fail(message);
    }

    /**
     * 返回失败
     *
     * @param message String
     * @param data T
     * @return ApiResult<T>
     */
    default <T> ApiResult<T> fail(String message, T data) {
        return ApiResult.fail(message, data);
    }

}
