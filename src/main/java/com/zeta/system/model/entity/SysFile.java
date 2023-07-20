package com.zeta.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.zetaframework.base.entity.Entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 系统文件
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "系统文件")
@TableName("sys_file")
public class SysFile extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /** 业务类型 */
    @ApiModelProperty(value = "业务类型", required = false)
    @TableField(value = "biz_type")
    private String bizType;

    /** 桶 */
    @ApiModelProperty(value = "桶", required = true)
    @NotBlank(message = "桶不能为空")
    @Size(max = 255, message = "桶长度不能超过255")
    @TableField(value = "bucket")
    private String bucket;

    /** 存储类型 */
    @ApiModelProperty(value = "存储类型", required = true)
    @NotBlank(message = "存储类型不能为空")
    @Size(max = 255, message = "存储类型长度不能超过255")
    @TableField(value = "storage_type")
    private String storageType;

    /** 文件相对地址 */
    @ApiModelProperty(value = "文件相对地址", required = true)
    @NotBlank(message = "文件相对地址不能为空")
    @Size(max = 255, message = "文件相对地址长度不能超过255")
    @TableField(value = "path")
    private String path;

    /** 文件访问地址 */
    @ApiModelProperty(value = "文件访问地址", required = false)
    @TableField(value = "url")
    private String url;

    /** 唯一文件名 */
    @ApiModelProperty(value = "唯一文件名", required = true)
    @NotBlank(message = "唯一文件名不能为空")
    @Size(max = 255, message = "唯一文件名长度不能超过255")
    @TableField(value = "unique_file_name")
    private String uniqueFileName;

    /** 原始文件名 */
    @ApiModelProperty(value = "原始文件名", required = false)
    @TableField(value = "original_file_name")
    private String originalFileName;

    /** 文件类型 */
    @ApiModelProperty(value = "文件类型", required = false)
    @TableField(value = "file_type")
    private String fileType;

    /** 内容类型 */
    @ApiModelProperty(value = "内容类型", required = false)
    @TableField(value = "content_type")
    private String contentType;

    /** 后缀 */
    @ApiModelProperty(value = "后缀", required = false)
    @TableField(value = "suffix")
    private String suffix;

    /** 文件大小 */
    @ApiModelProperty(value = "文件大小", required = false)
    @TableField(value = "size")
    private Long size;
}
