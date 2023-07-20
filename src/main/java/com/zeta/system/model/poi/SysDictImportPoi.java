package com.zeta.system.model.poi;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zetaframework.base.entity.ImportPoi;

import javax.validation.constraints.NotBlank;

/**
 * 字典Excel导入数据
 *
 * @author gcc
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictImportPoi extends ImportPoi {

    /** 名称 */
    @Excel(name = "字典名", width = 15.0)
    @NotBlank(message = "不能为空")
    private String name;

    /** 编码 */
    @Excel(name = "字典编码", width = 15.0)
    @NotBlank(message = "不能为空")
    private String code;

    /** 描述 */
    @Excel(name = "描述", width = 30.0)
    private String describe;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sortValue;

}
