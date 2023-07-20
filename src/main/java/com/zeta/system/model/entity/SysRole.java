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
 * 角色
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "角色")
@TableName("sys_role")
public class SysRole extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /** 角色名 */
    @ApiModelProperty(value = "角色名", required = true)
    @NotBlank(message = "角色名不能为空")
    @Size(max = 32, message = "角色名长度不能超过32")
    @TableField(value = "name")
    private String name;

    /** 角色编码 */
    @ApiModelProperty(value = "角色编码", required = true)
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 32, message = "角色编码长度不能超过32")
    @TableField(value = "code")
    private String code;

    /** 描述 */
    @ApiModelProperty(value = "描述", required = false)
    @TableField(value = "describe_")
    private String describe;

    /** 逻辑删除字段 */
    @JsonIgnore
    @ApiModelProperty(value = "逻辑删除字段", required = false)
    @TableLogic
    private Boolean deleted;

    /** 内置 */
    @ApiModelProperty(value = "内置", required = true)
    @TableField(value = "readonly_")
    private Boolean readonly;


    public SysRole(Long id, String name, String code, String describe) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.describe = describe;
    }

    public SysRole(Long id, String name, String code, String describe, Boolean readonly) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.describe = describe;
        this.readonly = readonly;
    }
}
