package com.zeta.system.model.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码返回结果
 *
 * @author gcc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "验证码返回结果")
public class CaptchaResult {

    /**
     * 验证码key。
     * 后台利用该key去redis中查询正确的验证码值
     */
    @ApiModelProperty(value = "key")
    private Long key;

    /**
     * 验证码base64数据
     */
    @ApiModelProperty(value = "图形验证码base64数据")
    private String base64;

    /**
     * 验证码文本 生产环境不会返回该值
     */
    @ApiModelProperty(value = "验证码文本")
    private String text;

    public CaptchaResult(Long key, String base64) {
        this.key = key;
        this.base64 = base64;
    }
}
