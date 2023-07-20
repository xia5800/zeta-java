package org.zetaframework.base.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 获取导入Excel模板 参数
 *
 * @author gcc
 */
@ApiModel(description = "获取导入Excel模板参数")
public class ImportExcelTemplateParam {

    /** excel模板文件名 */
    @ApiModelProperty(value = "excel模板文件名,不带后缀", required = true)
    @NotBlank(message = "excel模板文件名不能为空")
    private String fileName;

    /** 表格标题 */
    @ApiModelProperty(value = "表格标题", required = false)
    private String title;

    /** sheet名称 */
    @ApiModelProperty(value = "sheet名称", required = false)
    private String sheetName;

    /** excel文件类型 */
    @ApiModelProperty(value = "excel模板类型", allowableValues = "HSSF、XSSF", example = "XSSF", required = true)
    @NotBlank(message = "excel模板类型不能为空")
    private String type;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
