package com.zeta.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.zetaframework.base.entity.SuperEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 操作日志
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "操作日志")
@TableName("sys_opt_log")
public class SysOptLog extends SuperEntity<Long> {

    private static final long serialVersionUID = 1L;

    /** 操作类型 */
    @ApiModelProperty(value = "操作类型", required = true)
    @NotBlank(message = "操作类型不能为空")
    @Size(max = 10, message = "操作类型长度不能超过10")
    @TableField(value = "type")
    private String type;

    /** 操作描述 */
    @ApiModelProperty(value = "操作描述", required = false)
    @TableField(value = "description")
    private String description;

    /** 请求地址 */
    @ApiModelProperty(value = "请求地址", required = true)
    @NotBlank(message = "请求地址不能为空")
    @Size(max = 255, message = "请求地址长度不能超过255")
    @TableField(value = "url")
    private String url;

    /** 请求方式 */
    @ApiModelProperty(value = "请求方式", required = true)
    @NotBlank(message = "请求方式不能为空")
    @Size(max = 10, message = "请求方式长度不能超过10")
    @TableField(value = "http_method")
    private String httpMethod;

    /** 类路径 */
    @ApiModelProperty(value = "类路径", required = true)
    @NotBlank(message = "类路径不能为空")
    @Size(max = 255, message = "类路径长度不能超过255")
    @TableField(value = "class_path")
    private String classPath;

    /** 请求参数 */
    @ApiModelProperty(value = "请求参数", required = false)
    @TableField(value = "params")
    private String params;

    /** 返回值 */
    @ApiModelProperty(value = "返回值", required = false)
    @TableField(value = "result")
    private String result;

    /** 异常描述 */
    @ApiModelProperty(value = "异常描述", required = false)
    @TableField(value = "exception")
    private String exception;

    /** 消耗时间 单位毫秒 */
    @ApiModelProperty(value = "消耗时间 单位毫秒", required = true)
    @NotNull(message = "消耗时间不能为空")
    @TableField(value = "spend_time")
    private Integer spendTime;

    /** 操作系统 */
    @ApiModelProperty(value = "操作系统", required = false)
    @TableField(value = "os")
    private String os;

    /** 设备名称 */
    @ApiModelProperty(value = "设备名称", required = false)
    @TableField(value = "device")
    private String device;

    /** 浏览器类型 */
    @ApiModelProperty(value = "浏览器类型", required = false)
    @TableField(value = "browser")
    private String browser;

    /** ip地址 */
    @ApiModelProperty(value = "ip地址", required = false)
    @TableField(value = "ip")
    private String ip;

    /** ip所在地区 */
    @ApiModelProperty(value = "ip所在地区", required = false)
    @TableField(value = "ip_region")
    private String ipRegion;

    /** 操作人 */
    @ApiModelProperty(value = "操作人", required = false)
    @TableField(exist = false)
    private String userName;
}
