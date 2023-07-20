package com.zeta.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.zetaframework.base.entity.SuperEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 登录日志
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "登录日志")
@TableName("sys_login_log")
public class SysLoginLog extends SuperEntity<Long> {

    private static final long serialVersionUID = 1L;

    /** 状态 */
    @ApiModelProperty(value = "状态", required = true)
    @NotBlank(message = "状态不能为空")
    @Size(max = 10, message = "状态长度不能超过10")
    @TableField(value = "state")
    private String state;

    /** 账号 */
    @ApiModelProperty(value = "账号", required = true)
    @NotBlank(message = "账号不能为空")
    @Size(max = 64, message = "账号长度不能超过64")
    @TableField(value = "account")
    private String account;

    /** 备注 */
    @ApiModelProperty(value = "备注", required = false)
    @TableField(value = "comments")
    private String comments;

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
}
