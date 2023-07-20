package com.zeta.system.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 操作日志 查询参数
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "操作日志查询参数")
public class SysOptLogQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 操作类型 */
    @ApiModelProperty(value = "操作类型")
    private String type;

    /** 操作人 */
    @ApiModelProperty(value = "操作人")
    private String userName;

    /** 操作描述 */
    @ApiModelProperty(value = "操作描述")
    private String description;

    /** 请求地址 */
    @ApiModelProperty(value = "请求地址")
    private String url;

    /** 请求方式 */
    @ApiModelProperty(value = "请求方式")
    private String httpMethod;

    /** 类路径 */
    @ApiModelProperty(value = "类路径")
    private String classPath;

    /** 操作系统 */
    @ApiModelProperty(value = "操作系统")
    private String os;

    /** 设备名称 */
    @ApiModelProperty(value = "设备名称")
    private String device;

    /** 浏览器类型 */
    @ApiModelProperty(value = "浏览器类型")
    private String browser;

    /** ip地址 */
    @ApiModelProperty(value = "ip地址")
    private String ip;

    /** ip所在地区 */
    @ApiModelProperty(value = "ip所在地区")
    private String ipRegion;
}
