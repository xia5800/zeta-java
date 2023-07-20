package org.zetaframework.extra.crypto.properties;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Aes加密配置参数
 * @author gcc
 */
@ConfigurationProperties(prefix = "zeta.crypto.aes")
public class AesCryptoProperties {

    /** AES加密秘钥 长度规定：16、24、32 */
    private String cryptoKey = "";

    /** AES加密盐 */
    private String cryptoIv = "";

    /** 加密模式 */
    private Mode mode = Mode.ECB;

    /**
     * 填充方式
     *
     * 说明：
     * JDK的PKCS5就是按PKCS7实现的，直接用 PKCS5Padding 即可
     */
    private Padding padding = Padding.PKCS5Padding;

    public String getCryptoKey() {
        return cryptoKey;
    }

    public void setCryptoKey(String cryptoKey) {
        this.cryptoKey = cryptoKey;
    }

    public String getCryptoIv() {
        return cryptoIv;
    }

    public void setCryptoIv(String cryptoIv) {
        this.cryptoIv = cryptoIv;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Padding getPadding() {
        return padding;
    }

    public void setPadding(Padding padding) {
        this.padding = padding;
    }
}
