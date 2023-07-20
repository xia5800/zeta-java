package com.zeta.system.model.dto.sysOptLog;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志详情,数据表格用
 *
 * 说明：
 * 少了请求参数、返回值、异常信息字段。
 * 这几个字段没必要在分页查询的时候传输给前端
 *
 * @author gcc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "操作日志详情,数据表格用")
public class SysOptLogTableDTO implements Serializable {

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

    /** 消耗时间 单位毫秒 */
    @ApiModelProperty(value = "消耗时间 单位毫秒")
    private Integer spendTime;

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
