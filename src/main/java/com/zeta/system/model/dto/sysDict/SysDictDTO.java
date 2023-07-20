package com.zeta.system.model.dto.sysDict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典 详情
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
@ApiModel(description = "字典详情")
public class SysDictDTO implements Serializable {

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

    /** 名称 */
    @ApiModelProperty(value = "名称")
    private String name;

    /** 编码 */
    @ApiModelProperty(value = "编码")
    private String code;

    /** 描述 */
    @ApiModelProperty(value = "描述")
    private String describe;

    /** 排序 */
    @ApiModelProperty(value = "排序")
    private Integer sortValue;

}
