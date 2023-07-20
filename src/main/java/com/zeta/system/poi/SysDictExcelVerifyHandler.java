package com.zeta.system.poi;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.util.StrUtil;
import com.zeta.system.model.entity.SysDict;
import com.zeta.system.model.poi.SysDictImportPoi;
import com.zeta.system.service.ISysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.zetaframework.base.param.ExistParam;

/**
 * 字典Excel导入校验接口实现类
 *
 * @author gcc
 */
@RequiredArgsConstructor
@Component
public class SysDictExcelVerifyHandler implements IExcelVerifyHandler<SysDictImportPoi> {

    private final ISysDictService service;

    /**
     * 导入校验方法
     *
     * @param obj 导入对象
     * @return ExcelVerifyHandlerResult
     */
    @Override
    public ExcelVerifyHandlerResult verifyHandler(SysDictImportPoi obj) {
        String message = "";
        boolean flag = true;

        if (StrUtil.isBlank(obj.getCode())) {
            message = "字典编码不能为空";
            flag = false;
        } else {
            // 判断是否存在
            if (new ExistParam<SysDict, Long>(SysDict::getCode, obj.getCode()).isExist(service)) {
                message = "编码已存在";
                flag = false;
            }
        }

        return new ExcelVerifyHandlerResult(flag, message);
    }
}
