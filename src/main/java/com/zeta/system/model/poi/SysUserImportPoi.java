package com.zeta.system.model.poi;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.date.DatePattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zetaframework.base.entity.ImportPoi;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 用户Excel导入数据
 *
 * @author gcc
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserImportPoi extends ImportPoi {

    /** 用户名 */
    @Excel(name = "用户名", width = 15.0)
    @NotBlank(message = "不能为空")
    private String username;

    /** 账号 */
    @Excel(name = "账号", width = 15.0)
    @NotBlank(message = "不能为空")
    private String account;

    /** 密码 */
    @Excel(name = "密码", width = 50.0)
    @NotBlank(message = "不能为空")
    private String password;

    /** 邮箱 */
    @Excel(name = "邮箱", width = 15.0)
    private String email;

    /** 手机号 */
    @Excel(name = "手机号", width = 15.0)
    @Size(max = 11, message = "长度不能超过11")
    private String mobile;

    /** 性别 */
    @Excel(name = "性别", replace = {"男_1", "女_2", "_0"}, addressList = true)
    private Integer sex;

    /** 生日 */
    @Excel(name = "生日", format = DatePattern.NORM_DATE_PATTERN, width = 20.0)
    private LocalDate birthday;

    /** 用户角色 说明：多个角色之间逗号隔开 */
    @Excel(name = "用户角色", replace = {"普通用户_普通用户", "管理员_管理员", "_null"}, addressList = true, width = 20.0)
    private String roleNames;
    
}
