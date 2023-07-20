package com.zeta.system.model.dto.sysDict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 新增 字典
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
@ApiModel(description = "新增字典")
public class SysDictSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 名称 */
    @ApiModelProperty(value = "名称", required = true)
    @NotEmpty(message = "名称不能为空")
    @Size(max = 32, message = "名称长度不能超过32")
    private String name;

    /** 编码 */
    @ApiModelProperty(value = "编码", required = true)
    @NotEmpty(message = "编码不能为空")
    @Size(max = 32, message = "编码长度不能超过32")
    private String code;

    /** 描述 */
    @ApiModelProperty(value = "描述", required = false)
    private String describe;

    /** 排序 */
    @ApiModelProperty(value = "排序", required = false)
    private Integer sortValue;

}
