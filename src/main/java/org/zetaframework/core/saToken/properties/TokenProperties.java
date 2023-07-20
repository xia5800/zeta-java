package org.zetaframework.core.saToken.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.zetaframework.core.saToken.enums.TokenTypeEnum;

/**
 * token配置参数
 *
 * @author gcc
 */
@ConfigurationProperties(prefix = "zeta.token")
public class TokenProperties {
    /** jwt签名key */
    private String signerKey = "zeta-java";

    /** token类型 */
    private TokenTypeEnum type = TokenTypeEnum.DEFAULT;

    /** jwt签名前缀 例如填写 Bearer 实际传参 token: Bearer xxxxxx */
    private String prefix = "";

    /** token过期时间 单位：秒; -1代表永不过期 */
    private Long expireTime = 7200L;

    /** token自动续期，每次操作为token续期指定时间 */
    private Boolean renew = false;


    public String getSignerKey() {
        return signerKey;
    }

    public void setSignerKey(String signerKey) {
        this.signerKey = signerKey;
    }

    public TokenTypeEnum getType() {
        return type;
    }

    public void setType(TokenTypeEnum type) {
        this.type = type;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Boolean getRenew() {
        return renew;
    }

    public void setRenew(Boolean renew) {
        this.renew = renew;
    }
}
