package com.zeta.system.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录返回结果
 *
 * @author gcc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "登录返回结果")
public class LoginResult {
    
    /** token名称 */
    @ApiModelProperty(value = "token名称")
    private String tokenName;

    /** token值 */
    @ApiModelProperty(value = "token值")
    private String token;
    
}
