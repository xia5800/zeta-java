package com.zeta.system.controller;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.zeta.system.model.dto.sysDict.SysDictSaveDTO;
import com.zeta.system.model.dto.sysDict.SysDictUpdateDTO;
import com.zeta.system.model.entity.SysDict;
import com.zeta.system.model.param.SysDictQueryParam;
import com.zeta.system.model.poi.SysDictExportPoi;
import com.zeta.system.model.poi.SysDictImportPoi;
import com.zeta.system.poi.SysDictExcelVerifyHandler;
import com.zeta.system.service.ISysDictService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zetaframework.base.controller.SuperController;
import org.zetaframework.base.controller.extra.PoiController;
import org.zetaframework.base.param.ExistParam;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.saToken.annotation.PreAuth;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典 前端控制器
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
@RequiredArgsConstructor
@Api(tags = "字典")
@PreAuth(replace = "sys:dict")
@RestController
@RequestMapping("/api/system/dict")
public class SysDictController extends SuperController<ISysDictService, Long, SysDict, SysDictQueryParam, SysDictSaveDTO, SysDictUpdateDTO>
    implements PoiController<SysDictImportPoi, SysDictExportPoi, SysDict, SysDictQueryParam>
{

    private final SysDictExcelVerifyHandler sysDictExcelVerifyHandler;


    /**
     * 自定义新增
     *
     * @param saveDTO SysDictSaveDTO 保存对象
     * @return ApiResult<Boolean>
     */
    @Override
    public ApiResult<Boolean> handlerSave(SysDictSaveDTO saveDTO) {
        // 判断是否存在
        if (new ExistParam<SysDict, Long>(SysDict::getCode, saveDTO.getCode()).isExist(service)) {
            return fail("编码已存在");
        }
        return super.handlerSave(saveDTO);
    }

    /**
     * 自定义修改
     *
     * @param updateDTO UpdateDTO 修改对象
     * @return ApiResult<Boolean>
     */
    @Override
    public ApiResult<Boolean> handlerUpdate(SysDictUpdateDTO updateDTO) {
        // 判断是否存在
        if (new ExistParam<SysDict, Long>(SysDict::getCode, updateDTO.getCode(), updateDTO.getId()).isExist(service)) {
            return fail("编码已存在");
        }
        return super.handlerUpdate(updateDTO);
    }

    /**
     * 导入参数增强
     * <p>
     * 说明：
     * 你可以在这里对ImportParams配置进行一些补充
     * 例如设置excel验证规则、校验组、校验处理接口等
     * @param importParams 导入参数
     */
    @Override
    public void enhanceImportParams(ImportParams importParams) {
        // 开启：校验上传的Excel数据
        importParams.setNeedVerify(true);
        // 校验处理接口：字典编码重复校验
        importParams.setVerifyHandler(sysDictExcelVerifyHandler);
    }

    /**
     * 处理导入数据
     * <p>
     * 说明：
     * 你需要手动实现导入逻辑
     *
     * @param list 导入数据。不可为空
     */
    @Override
    public ApiResult<Boolean> handlerImport(List<SysDictImportPoi> list) {
        // List<ImportPoi> -> List<Entity>
        List<SysDict> batchList = list.stream()
                .map(it -> BeanUtil.toBean(it, SysDict.class))
                .collect(Collectors.toList());

        return success(service.saveBatch(batchList));
    }

    /**
     * 获取待导出的数据
     *
     * @param queryParam 查询参数 不可为空
     * @return List<ExportBean>
     */
    @Override
    public List<SysDictExportPoi> findExportList(SysDictQueryParam queryParam) {
        // 条件查询Entity数据
        List<SysDict> list = super.handlerBatchQuery(queryParam);
        if (list.isEmpty()) return Collections.emptyList();

        // List<Entity> -> List<ExportBean>
        return list.stream()
                .map(it -> BeanUtil.toBean(it, SysDictExportPoi.class))
                .collect(Collectors.toList());
    }

}

