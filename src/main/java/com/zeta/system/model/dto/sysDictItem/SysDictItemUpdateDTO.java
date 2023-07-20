package com.zeta.system.model.dto.sysDictItem;

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
 * 修改 字典项
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
@ApiModel(description = "修改字典项")
public class SysDictItemUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = Update.class)
    private Long id;

    /** 字典id */
    @ApiModelProperty(value = "字典id", required = true)
    @NotNull(message = "字典id不能为空")
    private Long dictId;

    /** 字典项 */
    @ApiModelProperty(value = "字典项", required = true)
    @NotEmpty(message = "字典项不能为空")
    @Size(max = 32, message = "字典项长度不能超过32")
    private String name;

    /** 值 */
    @ApiModelProperty(value = "值", required = true)
    @NotEmpty(message = "值不能为空")
    @Size(max = 32, message = "值长度不能超过32")
    private String value;

    /** 描述 */
    @ApiModelProperty(value = "描述", required = false)
    private String describe;

    /** 排序 */
    @ApiModelProperty(value = "排序", required = false)
    private Integer sortValue;

}
