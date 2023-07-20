package com.zeta;

import cn.dev33.satoken.secure.BCrypt;
import org.junit.jupiter.api.Test;

/**
 * 密码加密测试
 *
 * @author gcc
 */
public class PasswordCryptoTest {

    /**
     * 生成加密后的密码
     */
    @Test
    public void cryptoPassword() {
        // 使用 saToken提供的 BCrypt 加密密码
        System.out.println(BCrypt.hashpw("zetaAdmin"));
    }

    /**
     * 比较密码
     */
    @Test
    public void comparePassword() {
        // 使用 saToken提供的 BCrypt 解密密码
        System.out.println(BCrypt.checkpw(
                "zetaAdmin",
                "$2a$10$d/1iNA9xj1mfyhylUx7G5.OFcWOZOyZ6UZSxNwgTatmwJANwGSuYW"
        ));
    }
}
