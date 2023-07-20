package com.zeta.system.model.poi;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 字典Excel导出数据
 *
 * @author gcc
 */
@Data
public class SysDictExportPoi {

    /** id */
    @Excel(name = "id", width = 20.0)
    private Long id;

    /** 名称 */
    @Excel(name = "字典名", width = 15.0)
    private String name;

    /** 编码 */
    @Excel(name = "字典编码", width = 15.0)
    private String code;

    /** 描述 */
    @Excel(name = "描述", width = 30.0)
    private String describe;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sortValue;

}
