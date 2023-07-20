package org.zetaframework.base.entity;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;

/**
 * Excel导入数据接收对象 父类
 *
 * @author gcc
 */
public class ImportPoi implements IExcelModel, IExcelDataModel {

    /** 导入的错误信息 */
    private String importErrorMsg;

    /** 导入的行号 */
    private Integer importRowNum;

    public String getImportErrorMsg() {
        return importErrorMsg;
    }

    public void setImportErrorMsg(String importErrorMsg) {
        this.importErrorMsg = importErrorMsg;
    }

    public Integer getImportRowNum() {
        return importRowNum;
    }

    public void setImportRowNum(Integer importRowNum) {
        this.importRowNum = importRowNum;
    }

    /**
     * 获取错误数据
     *
     * @return
     */
    @Override
    public String getErrorMsg() {
        return this.importErrorMsg;
    }

    /**
     * 设置错误信息
     *
     * @param errorMsg
     */
    @Override
    public void setErrorMsg(String errorMsg) {
        this.importErrorMsg = errorMsg;
    }

    /**
     * 获取行号
     *
     * @return
     */
    @Override
    public Integer getRowNum() {
        return this.importRowNum;
    }

    /**
     * 设置行号
     *
     * @param rowNum
     */
    @Override
    public void setRowNum(Integer rowNum) {
        this.importRowNum = rowNum;
    }
}
