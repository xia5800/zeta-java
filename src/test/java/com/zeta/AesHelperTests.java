package com.zeta;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zetaframework.extra.crypto.helper.AESHelper;

import java.util.Objects;

/**
 * Aes加密解密Helper测试类
 *
 * @author gcc
 */
@SpringBootTest
public class AesHelperTests {

    @Autowired
    private AESHelper aesHelper;

    /**
     * 登录密码AES加密
     *
     * 说明：
     * 因为前端登录时将用户输入的明文密码用aes算法加密了一遍。
     * 所以本项目登录时需要将密文解密成明文，再去和数据中的密码进行比较
     */
    @Test
    public void cryptoPassword() {
        // 加密
        String result = aesHelper.encryptBase64("admin");
        System.out.println(result);
        assert(Objects.equals(result, "dDEWFk6fJKwZ55cL3zVUsQ=="));

        // 解密
        String password = aesHelper.decryptStr(result);
        assert(Objects.equals(password, "admin"));
    }

}
