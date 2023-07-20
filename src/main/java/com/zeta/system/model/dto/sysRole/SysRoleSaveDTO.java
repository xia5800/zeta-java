package com.zeta.system.model.dto.sysRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 新增 角色
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "新增角色")
public class SysRoleSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 角色名 */
    @ApiModelProperty(value = "角色名", required = true)
    @NotEmpty(message = "角色名不能为空")
    @Size(max = 32, message = "角色名长度不能超过32")
    private String name;

    /** 角色编码 */
    @ApiModelProperty(value = "角色编码", required = true)
    @NotEmpty(message = "角色编码不能为空")
    @Size(max = 32, message = "角色编码长度不能超过32")
    private String code;

    /** 描述 */
    @ApiModelProperty(value = "描述", required = false)
    private String describe;

}
