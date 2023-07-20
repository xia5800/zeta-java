package com.zeta.system.model.enums;

import lombok.Getter;

/**
 * 性别 枚举
 *
 * @author gcc
 */
@Getter
public enum SexEnum {
    /** 未知 */
    UNKNOWN(0, "未知"),
    /** 男 */
    MALE(1, "男"),
    /** 女 */
    FEMALE(2, "女");

    /** 性别编码 */
    private final Integer code;

    /** 性别描述 */
    private final String msg;

    SexEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
