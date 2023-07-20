package com.zeta.system.model.poi;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.date.DatePattern;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户Excel导出数据
 *
 * @author gcc
 */
@Data
public class SysUserExportPoi {

    /** id */
    @Excel(name = "id", width = 20.0)
    private Long id;

    /** 用户名 */
    @Excel(name = "用户名", width = 15.0)
    private String username;

    /** 账号 */
    @Excel(name = "账号", width = 15.0)
    private String account;

    /** 密码 */
    @Excel(name = "密码", desensitizationRule="3_4", width = 50.0)
    private String password;

    /** 邮箱 */
    @Excel(name = "邮箱", width = 15.0)
    private String email;

    /** 手机号 */
    @Excel(name = "手机号", width = 15.0)
    private String mobile;

    /** 性别 */
    @Excel(name = "性别", replace = {"男_1", "女_2", "_null"}, addressList = true)
    private Integer sex;

    /** 生日 */
    @Excel(name = "生日", format = DatePattern.NORM_DATE_PATTERN, width = 20.0)
    private LocalDate birthday;

    /** 状态 */
    @Excel(name = "状态", replace = {"正常_0", "封禁_1", "_null"})
    private Integer state;

    /** 用户角色 */
    @Excel(name = "用户角色", width = 20.0)
    private List<String> roles;

    /** 注册时间 */
    @Excel(name = "注册时间", format = DatePattern.NORM_DATETIME_PATTERN, width = 20.0)
    private LocalDateTime createTime;

}
