package org.zetaframework.extra.crypto;

import cn.hutool.crypto.symmetric.AES;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zetaframework.extra.crypto.helper.AESHelper;
import org.zetaframework.extra.crypto.properties.AesCryptoProperties;

/**
 * 加密解密配置
 *
 * 说明：
 * 只做了加密工具类的配置，使用方法为注入AESHelper之后进行加密解密操作。
 * 没有做接口入参解密、返回值加密。有需求的可以自行实现
 * @author gcc
 */
@Configuration
@EnableConfigurationProperties(AesCryptoProperties.class)
public class CryptoConfiguration {

    private final AesCryptoProperties aesCryptoProperties;
    public CryptoConfiguration(AesCryptoProperties aesCryptoProperties) {
        this.aesCryptoProperties = aesCryptoProperties;
    }

    /**
     * 配置AES加密解密类
     */
    @Bean
    public AES aes() {
        // 检查key的长度
        return new AES(
                aesCryptoProperties.getMode(),
                aesCryptoProperties.getPadding(),
                aesCryptoProperties.getCryptoKey().getBytes(),
                aesCryptoProperties.getCryptoIv().getBytes()
        );
    }

    /**
     * 配置AES加密解密Helper类
     *
     * @param aes 上面配置好的hutool的AES加密解密类
     */
    @Bean
    public AESHelper aesHelper(AES aes) {
        return new AESHelper(aes);
    }

    /**
     * BCryptPasswordEncoder 配置
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
