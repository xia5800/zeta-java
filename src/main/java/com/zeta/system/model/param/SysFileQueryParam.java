package com.zeta.system.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统文件 查询参数
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
@ApiModel(description = "系统文件查询参数")
public class SysFileQueryParam implements Serializable {

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

    /** 业务类型 */
    @ApiModelProperty(value = "业务类型")
    private String bizType;

    /** 桶 */
    @ApiModelProperty(value = "桶")
    private String bucket;

    /** 存储类型 */
    @ApiModelProperty(value = "存储类型")
    private String storageType;

    /** 文件相对地址 */
    @ApiModelProperty(value = "文件相对地址")
    private String path;

    /** 文件访问地址 */
    @ApiModelProperty(value = "文件访问地址")
    private String url;

    /** 唯一文件名 */
    @ApiModelProperty(value = "唯一文件名")
    private String uniqueFileName;

    /** 原始文件名 */
    @ApiModelProperty(value = "原始文件名")
    private String originalFileName;

    /** 文件类型 */
    @ApiModelProperty(value = "文件类型")
    private String fileType;

    /** 内容类型 */
    @ApiModelProperty(value = "内容类型")
    private String contentType;

    /** 后缀 */
    @ApiModelProperty(value = "后缀")
    private String suffix;

    /** 文件大小 */
    @ApiModelProperty(value = "文件大小")
    private Long size;
}
