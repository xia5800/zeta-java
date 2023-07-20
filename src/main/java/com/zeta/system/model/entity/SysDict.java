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
import javax.validation.constraints.Size;

/**
 * 字典
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "字典")
@TableName("sys_dict")
public class SysDict extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /** 名称 */
    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "名称不能为空")
    @Size(max = 32, message = "名称长度不能超过32")
    @TableField(value = "name")
    private String name;

    /** 编码 */
    @ApiModelProperty(value = "编码", required = true)
    @NotBlank(message = "编码不能为空")
    @Size(max = 32, message = "编码长度不能超过32")
    @TableField(value = "code")
    private String code;

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

    public SysDict(Long id, String name, String code, String describe, Integer sortValue) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.describe = describe;
        this.sortValue = sortValue;
    }
}
