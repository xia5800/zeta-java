package com.zeta.system.controller;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.zeta.system.model.dto.sysDictItem.SysDictItemDTO;
import com.zeta.system.model.dto.sysDictItem.SysDictItemSaveDTO;
import com.zeta.system.model.dto.sysDictItem.SysDictItemUpdateDTO;
import com.zeta.system.model.entity.SysDict;
import com.zeta.system.model.entity.SysDictItem;
import com.zeta.system.model.param.SysDictItemQueryParam;
import com.zeta.system.model.poi.SysDictItemExportPoi;
import com.zeta.system.model.poi.SysDictItemImportPoi;
import com.zeta.system.service.ISysDictItemService;
import com.zeta.system.service.ISysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zetaframework.base.controller.SuperController;
import org.zetaframework.base.controller.extra.PoiController;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.satoken.annotation.PreAuth;
import org.zetaframework.core.satoken.annotation.PreCheckPermission;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典项 前端控制器
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
@RequiredArgsConstructor
@Api(tags = "字典项")
@PreAuth(replace = "sys:dictItem")
@RestController
@RequestMapping("/api/system/dictItem")
public class SysDictItemController extends SuperController<ISysDictItemService, Long, SysDictItem, SysDictItemQueryParam, SysDictItemSaveDTO, SysDictItemUpdateDTO>
    implements PoiController<SysDictItemImportPoi, SysDictItemExportPoi, SysDictItem, SysDictItemQueryParam>
{
    private final ISysDictService dictService;

    /**
     * 根据字典编码查询字典项
     *
     * @param codes List<String>
     */
    @PreCheckPermission(value = "{}:view")
    @ApiOperation(value = "根据字典编码查询字典项")
    @PostMapping("/codeList")
    public ApiResult<Map<String, List<SysDictItemDTO>>> codeList(@RequestBody @ApiParam("字典code") List<String> codes) {
        Assert.notEmpty(codes, "字典code不能为空");
        return success(service.listByCodes(codes));
    }

    /**
     * 导入参数增强
     * <p>
     * 说明：
     * 你可以在这里对ImportParams配置进行一些补充
     * 例如设置excel验证规则、校验组、校验处理接口等
     *
     * @param importParams 导入参数 不可为空
     */
    @Override
    public void enhanceImportParams(ImportParams importParams) {
        // 开启：校验上传的Excel数据
        importParams.setNeedVerify(true);
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
    public ApiResult<Boolean> handlerImport(List<SysDictItemImportPoi> list) {
        // List<ImportPoi> -> List<Entity>
        List<SysDictItem> batchList = list.stream()
                .map(it -> BeanUtil.toBean(it, SysDictItem.class))
                .collect(Collectors.toList());

        return success(service.saveBatch(batchList));
    }

    /**
     * 获取待导出的数据
     *
     * @param param 查询参数 不可为空
     * @return List<ExportBean>
     */
    @Override
    public List<SysDictItemExportPoi> findExportList(SysDictItemQueryParam param) {
        // 条件查询Entity数据
        List<SysDictItem> list = super.handlerBatchQuery(param);
        if (list.isEmpty()) return Collections.emptyList();

        // 字典数据缓存
        Map<Long, SysDict> dictCacheMap = new HashMap<>();

        // List<Entity> -> List<ExportBean>
        List<SysDictItemExportPoi> exportPoiList = list.stream().map(it -> {
            SysDictItemExportPoi exportPoi = BeanUtil.toBean(it, SysDictItemExportPoi.class);

            // 通过id查询字典数据并缓存。说明：保证每个字典只查一次数据库
            SysDict dict = dictCacheMap.get(it.getDictId());
            if (dict == null) {
                dict = dictService.getById(it.getDictId());
                dictCacheMap.put(it.getDictId(), dict);
            }

            // 设置字典名
            exportPoi.setDictName(dict != null ? dict.getName() : "");
            return exportPoi;
        }).collect(Collectors.toList());

        dictCacheMap.clear();
        return exportPoiList;
    }
}

