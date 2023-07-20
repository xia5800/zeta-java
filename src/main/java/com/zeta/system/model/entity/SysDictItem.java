package com.zeta.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.zetaframework.base.entity.Entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 字典项
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "字典项")
@TableName("sys_dict_item")
public class SysDictItem extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /** 字典id */
    @ApiModelProperty(value = "字典id", required = true)
    @NotNull(message = "字典id不能为空")
    @TableField(value = "dict_id")
    private Long dictId;

    /** 字典项 */
    @ApiModelProperty(value = "字典项", required = true)
    @NotBlank(message = "字典项不能为空")
    @Size(max = 32, message = "字典项长度不能超过32")
    @TableField(value = "name")
    private String name;

    /** 值 */
    @ApiModelProperty(value = "值", required = true)
    @NotBlank(message = "值不能为空")
    @Size(max = 32, message = "值长度不能超过32")
    @TableField(value = "value")
    private String value;

    /** 描述 */
    @ApiModelProperty(value = "描述", required = false)
    @TableField(value = "describe_")
    private String describe;

    /** 排序 */
    @ApiModelProperty(value = "排序", required = false)
    @TableField(value = "sort_value")
    private Integer sortValue;

    /** 逻辑删除字段 */
    @JsonIgnore
    @ApiModelProperty(value = "逻辑删除字段", required = true)
    @TableLogic
    private Boolean deleted;

    public SysDictItem(Long id, Long dictId, String name, String value, String describe, Integer sortValue) {
        this.id = id;
        this.dictId = dictId;
        this.name = name;
        this.value = value;
        this.describe = describe;
        this.sortValue = sortValue;
    }
}
