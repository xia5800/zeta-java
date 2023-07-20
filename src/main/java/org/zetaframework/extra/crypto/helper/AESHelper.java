package org.zetaframework.extra.crypto.helper;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import org.zetaframework.extra.crypto.enums.KeyLength;

import java.util.Locale;

/**
 * Aes加密解密Helper类
 *
 * 说明：
 * 1.JDK 的 PKCS5 就是按 PKCS7 实现的，直接用 PKCS5Padding 即可
 * 2.为什么叫Helper类，因为util类应该是只具有静态方法的类，不需要new出来也无需注入bean。所以用Helper命名比较好
 *
 * @author gcc
 */
public class AESHelper {

    private final AES ase;
    public AESHelper(AES ase) {
        this.ase = ase;
    }

    /**
     * 加密, 返回bas64字符串
     *
     * @param data 需要加密的数据
     */
     public String encryptBase64(String data) {
        return Base64.encode(encrypt(data));
    }

    /**
     * 加密，返回16进制字符串
     *
     * @param data 需要加密的数据
     */
     public String encryptHex(String data) {
        return HexUtil.encodeHexStr(encrypt(data));
    }

    /**
     * 加密，返回字节数组
     *
     * @param data 需要加密的数据
     */
     public byte[] encrypt(String data) {
         return ase.encrypt(data);
    }


    /**
     * 解密, 返回String
     *
     * @param data 需要解密的数据
     */
     public String decryptStr(String data) {
        return StrUtil.str(decrypt(data), CharsetUtil.CHARSET_UTF_8);
    }


    /**
     * 解密，返回字节数组
     *
     * @param data 需要解密的数据
     */
     public byte[] decrypt(String data) {
         return ase.decrypt(data);
    }

    /**
     * 生成16位长度加密key
     */
    public String generateEncryptKey() {
         return generateEncryptKey(KeyLength.L_16);
    }


    /**
     * 生成加密key
     *
     * @param length 长度。可选：16、24、32 默认：16
     */
     public String generateEncryptKey(KeyLength length) {
        String baseString = RandomUtil.BASE_CHAR_NUMBER + RandomUtil.BASE_CHAR.toUpperCase(Locale.getDefault());
        return RandomUtil.randomString(baseString, length.getValue());
    }

}
