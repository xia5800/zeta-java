package org.zetaframework.base.controller.extra;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.zetaframework.base.controller.BaseController;
import org.zetaframework.base.param.ExportExcelParam;
import org.zetaframework.core.exception.ArgumentException;
import org.zetaframework.core.log.annotation.SysLog;
import org.zetaframework.core.saToken.annotation.PreCheckPermission;
import org.zetaframework.core.saToken.annotation.PreMode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Excel导出Controller
 *
 * @param <ExportBean>  Excel导出实体
 * @param <Entity>      实体
 * @param <QueryParam>  查询参数
 *
 * @author gcc
 */
public interface ExportController<ExportBean, Entity, QueryParam> extends BaseController<Entity> {

    /**
     * 获取导出实体的类型
     */
    default Class<ExportBean> getExportExcelClass() {
        Optional<Type> first = Arrays.stream(this.getClass().getGenericInterfaces())
                // 筛选出类上面的ExportController接口
                .filter(it -> Objects.equals(((ParameterizedType) it).getRawType().getTypeName(), ExportController.class.getTypeName()))
                .findFirst();
        if (first.isEmpty()) return null;

        Type type = first.get();
        // 获取当前接口的第一个泛型的值(下标从0开始)。 即ExportBean的值
        Type argument = ((ParameterizedType) type).getActualTypeArguments()[0];
        return (Class<ExportBean>) argument;
    }


    /**
     * 导出Excel
     *
     * @param params   导出Excel参数
     * @param request  请求
     * @param response 响应
     */
    @PreCheckPermission(value = {"{}:export", "{}:view"}, mode = PreMode.OR)
    @ApiOperationSupport(order = 85, author = "AutoGenerate")
    @ApiOperation(value = "导出Excel", notes = "<pre>\n" +
    "导出参数示例：\n" +
    "\n" +
    "{\n" +
    "    \"fileName\": \"用户列表\",   // 【必传】excel的文件名\n" +
    "    \"queryParam\": { },       // 【必传】queryParam，数据查询条件可以为空，为空返回所有数据\n" +
    "    \"sheetName\": \"\",        // 【非必传】sheet名\n" +
    "    \"title\": \"\",            // 【非必传】第一页sheet表格的表头\n" +
    "    \"type\": \"XSSF\"          // 【必传】可选值：HSSF（excel97-2003版本，扩展名.xls）、XSSF（excel2007+版本，扩展名.xlsx）\n" +
    "}" +
    "</pre>")
    @SysLog(response = false)
    @PostMapping(value = "/export", produces = "application/octet-stream")
    default void exportExcel(@RequestBody @Validated ExportExcelParam<QueryParam> params, HttpServletRequest request, HttpServletResponse response) {
        QueryParam queryParam = params.getQueryParam();
        if (ObjectUtil.isNull(queryParam)) {
            throw new ArgumentException("查询条件不能为空");
        }

        // 获取导出参数
        ExportParams exportParams = getExportParams(params);
        // 获取待导出的数据
        List<ExportBean> list = findExportList(queryParam);

        // 构造excel导出视图所需要的参数
        Map<String, Object> map = new HashMap<>();
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, getExportExcelClass());
        map.put(NormalExcelConstants.PARAMS, exportParams);
        map.put(NormalExcelConstants.FILE_NAME, params.getFileName());

        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }


    /**
     * 获取导出参数
     *
     * @param params 导出Excel参数
     * @return ExportParams
     */
    default ExportParams getExportParams(ExportExcelParam<QueryParam> params) {
        // 处理excel文件类型
        ExcelType type = ExcelType.HSSF.name().equals(params.getType()) ? ExcelType.HSSF : ExcelType.XSSF;
        String title = StrUtil.isBlank(params.getTitle()) ? null : params.getTitle();
        String sheetName = StrUtil.isNotBlank(params.getSheetName()) ? params.getSheetName() : "sheet";

        // 构造ExportParams
        ExportParams exportParams = new ExportParams(title, sheetName, type);
        // ExportParams设置补充
        enhanceExportParams(exportParams);
        return exportParams;
    }

    /**
     * 导出参数增强
     *
     * 说明：
     * 你可以在这里对ExportParams配置进行一些补充
     * 例如设置表格第二行的名称、冻结一些列、表头颜色等
     * @param exportParams 导出参数 不可为空
     */
    default void enhanceExportParams(ExportParams exportParams) { }

    /**
     * 获取待导出的数据
     *
     * @param param 查询参数 不可为空
     * @return List<ExportBean>
     */
    default List<ExportBean> findExportList(QueryParam param) {
        // 构造查询条件
        Entity entity = BeanUtil.toBean(param, getEntityClass());

        // 条件查询Entity数据
        List<Entity> list = getBaseService().list(new QueryWrapper<>(entity));
        if (CollUtil.isEmpty(list)) return Collections.emptyList();

        // Entity -> ExportBean
        return list.stream()
                .map(it -> BeanUtil.toBean(it, getExportExcelClass()))
                .collect(Collectors.toList());
    }

}
