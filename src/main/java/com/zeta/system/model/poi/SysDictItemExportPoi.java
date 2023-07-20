package com.zeta.system.model.poi;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 字典项Excel导出数据
 *
 * @author gcc
 */
@Data
public class SysDictItemExportPoi {

    /** id */
    @Excel(name = "id", width = 20.0)
    private Long id;

    /** 字典id */
    @Excel(name = "字典id", mergeVertical = true, width = 20.0)
    private Long dictId;

    /** 字典名称 */
    @Excel(name = "字典名称", mergeVertical = true, width = 15.0)
    private String dictName;

    /** 字典项 */
    @Excel(name = "字典项", width = 15.0)
    private String name;

    /** 字典项值 */
    @Excel(name = "字典项值", width = 15.0)
    private String value;

    /** 描述 */
    @Excel(name = "描述", width = 30.0)
    private String describe;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sortValue;

}
