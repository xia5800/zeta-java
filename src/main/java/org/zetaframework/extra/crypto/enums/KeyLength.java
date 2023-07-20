package org.zetaframework.extra.crypto.enums;

/**
 * 生成加密key的长度
 *
 * @author gcc
 */
public enum KeyLength {
    /** 16字节，128位 */
    L_16(16),
    /** 24字节，192位 */
    L_24(24),
    /** 32字节，256位 */
    L_32(32);

    private final Integer value;

    KeyLength(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
