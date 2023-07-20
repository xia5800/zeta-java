package com.zeta.system.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典项 查询参数
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
@ApiModel(description = "字典项查询参数")
public class SysDictItemQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    /** 修改人 */
    @ApiModelProperty(value = "修改人")
    private Long updatedBy;

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

}
