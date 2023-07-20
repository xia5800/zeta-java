package com.zeta.system.model.enums;

import cn.hutool.core.collection.CollUtil;
import lombok.Getter;

import java.util.List;

/**
 * 用户状态 枚举
 *
 * @author gcc
 */
@Getter
public enum UserStateEnum {
    /** 禁用 */
    FORBIDDEN(0, "禁用"),
    /** 正常 */
    NORMAL(1, "正常");

    /** 状态码 */
    private final Integer code;

    /** 状态描述 */
    private final String msg;

    UserStateEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static List<Integer> getAllCode() {
        return CollUtil.newArrayList(
               NORMAL.getCode(),
               FORBIDDEN.getCode()
        );
    }

}
