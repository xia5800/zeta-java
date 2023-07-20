package com.zeta.system.poi;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.util.StrUtil;
import com.zeta.system.model.entity.SysUser;
import com.zeta.system.model.poi.SysUserImportPoi;
import com.zeta.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.zetaframework.base.param.ExistParam;

/**
 * 用户Excel导入校验接口实现类
 *
 * @author gcc
 */
@RequiredArgsConstructor
@Component
public class SysUserExcelVerifyHandler implements IExcelVerifyHandler<SysUserImportPoi> {

    private final ISysUserService service;

    /**
     * 导入校验方法
     *
     * @param obj 导入对象
     * @return ExcelVerifyHandlerResult
     */
    @Override
    public ExcelVerifyHandlerResult verifyHandler(SysUserImportPoi obj) {
        String message = "";
        boolean flag = true;

        if (StrUtil.isBlank(obj.getAccount())) {
            message = "账号不能为空";
            flag = false;
        } else {
            // 判断是否存在
            if (new ExistParam<SysUser, Long>(SysUser::getAccount, obj.getAccount()).isExist(service)) {
                message = "账号已存在";
                flag = false;
            }
        }

        return new ExcelVerifyHandlerResult(flag, message);
    }
}
