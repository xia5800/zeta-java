package com.zeta.system.model.dto.sysRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.zetaframework.core.validation.group.Update;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 修改 角色
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
@ApiModel(description = "修改角色")
public class SysRoleUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = Update.class)
    private Long id;

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
