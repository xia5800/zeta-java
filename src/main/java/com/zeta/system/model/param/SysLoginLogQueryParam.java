package com.zeta.system.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录日志 查询参数
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "登录日志查询参数")
public class SysLoginLogQueryParam implements Serializable {

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

    /** 状态 */
    @ApiModelProperty(value = "状态")
    private String state;

    /** 账号 */
    @ApiModelProperty(value = "账号")
    private String account;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String comments;

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
