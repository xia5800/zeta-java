package com.zeta.system.model.dto.sysDictItem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 字典项 详情
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
@ApiModel(description = "字典项详情")
public class SysDictItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 字典id */
    @ApiModelProperty(value = "字典id")
    private Long dictId;

    /** 字典项 */
    @ApiModelProperty(value = "字典项")
    private String name;

    /** 值 */
    @ApiModelProperty(value = "值")
    private String value;

    /** 描述 */
    @ApiModelProperty(value = "描述")
    private String describe;

    /** 排序 */
    @ApiModelProperty(value = "排序")
    private Integer sortValue;

    /** 字典code */
    @ApiModelProperty(value = "字典code")
    private String dictCode;
}
