package org.zetaframework.base.controller.extra;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.view.PoiBaseView;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.zetaframework.base.controller.BaseController;
import org.zetaframework.base.entity.ImportPoi;
import org.zetaframework.base.param.ImportExcelTemplateParam;
import org.zetaframework.base.result.ApiResult;
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
 * Excel导入Controller
 *
 * @param <ImportBean>  Excel导入实体  必须继承{@link ImportPoi}类
 * @param <Entity>      实体
 *
 * @author gcc
 */
public interface ImportController<ImportBean extends ImportPoi, Entity> extends BaseController<Entity> {

    /**
     * 获取导入实体的类型
     */
    default Class<ImportBean> getImportExcelClass() {
        Optional<Type> first = Arrays.stream(this.getClass().getGenericInterfaces())
                // 筛选出类上面的ImportController接口
                .filter(it -> Objects.equals(((ParameterizedType) it).getRawType().getTypeName(), ImportController.class.getTypeName()))
                .findFirst();
        if (first.isEmpty()) return null;

        Type type = first.get();
        // 获取当前接口的第一个泛型的值(下标从0开始)。 即ImportBean的值
        Type argument = ((ParameterizedType) type).getActualTypeArguments()[0];
        return (Class<ImportBean>) argument;
    }

    /**
     * 获取导入模板
     *
     * 说明：
     * 1.默认导出ImportBean对象生成的Excel模板
     * 2.如果想要导出resource目录下的Excel模板，请重写该方法
     *
     * @param param  获取导入Excel模板 参数
     * @param request 请求
     * @param response 响应
     */
    @PreCheckPermission(value = {"{}:import", "{}:view"}, mode = PreMode.OR)
    @ApiOperationSupport(order = 80, author = "AutoGenerate")
    @ApiOperation(value = "获取导入模板", notes = "<pre>\n" +
    "获取导入模板接口传参示例：\n" +
    "GET /api/xxxx/template?filename=用户列表&type=XSSF&sheetName=&title=\n" +
    "\n" +
    "参数说明：\n" +
    "fileName: \"用户列表\",  // 【必传】excel的文件名\n" +
    "sheetName: \"\",        // 【非必传】sheet名\n" +
    "title: \"\",            // 【非必传】第一页sheet表格的表头\n" +
    "type: \"XSSF\"          // 【必传】可选值：HSSF（excel97-2003版本，扩展名.xls）、XSSF（excel2007+版本，扩展名.xlsx）\n" +
    "</pre>")
    @SysLog(response = false)
    @GetMapping(value = "/template", produces = "application/octet-stream")
    default void getImportTemplate(ImportExcelTemplateParam param, HttpServletRequest request, HttpServletResponse response) {
        // 处理excel文件类型
        ExcelType type = ExcelType.HSSF.name().equals(param.getType()) ? ExcelType.HSSF : ExcelType.XSSF;
        String title = StrUtil.isBlank(param.getTitle()) ? null : param.getTitle();
        String sheetName = StrUtil.isNotBlank(param.getSheetName()) ? param.getSheetName() : "sheet";

        // 构造ExportParams
        ExportParams exportParams = new ExportParams(title, sheetName, type);

        // 获取导入模板基础数据
        List<ImportBean> list = getImportTemplateData();

        // 构造excel导出视图所需要的参数
        Map<String, Object> map = new HashMap<>();
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, getImportExcelClass());
        map.put(NormalExcelConstants.PARAMS, exportParams);
        map.put(NormalExcelConstants.FILE_NAME, param.getFileName());

        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    /**
     * 获取导入模板基础数据
     *
     * 说明：
     * 1.前端下载导入模板，如果需要导入模板里面有一些基础数据，可以重写本方法
     * 2.本方法默认在导入模板里面添加一条空的数据行
     */
    default List<ImportBean> getImportTemplateData() {
        // 反射创建一个ImportBean对象
        List<ImportBean> result = new ArrayList<>();
        try {
            ImportBean entity = getImportExcelClass().getDeclaredConstructor().newInstance();
            result.add(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 导入Excel
     *
     * @param file   导入的excel文件
     * @param request  请求
     */
    @PreCheckPermission(value = {"{}:import", "{}:save"}, mode = PreMode.OR)
    @ApiOperationSupport(order = 81, author = "AutoGenerate")
    @ApiOperation(value = "导入Excel", notes = "<pre>\n" +
    "【注意】请求类型为form-data\n" +
    "导入参数示例：\n" +
    "------------------------------------------\n" +
    "file：               【必传】文件对象\n" +
    "verifyFailReturn：   【非必传】true, false。默认true, 如果Excel数据校验失败“是否不处理校验通过的数据”而直接返回通知前端校验失败。\n" +
    "titleRows：          【非必传】默认0, 表格标题行数, 见文章：https://blog.csdn.net/weixin_43009990/article/details/106609660\n" +
    "headRows：           【非必传】默认1, 表头行数\n" +
    "startRows：          【非必传】默认0, 字段真正值和列标题之间的距离\n" +
    "startSheetIndex：    【非必传】默认0, 开始读取的sheet位置\n" +
    "sheetNum：           【非必传】默认1, 上传表格需要读取的sheet数量\n" +
    "needVerify：         【非必传】true, false。默认false, 是否需要校验上传的Excel\n" +
    "lastOfInvalidRow：   【非必传】默认0, 最后的无效行数\n" +
    "readRows：           【非必传】默认0, 手动控制读取的行数\n" +
    "</pre>")
    @SysLog
    @PostMapping(value = "/import")
    @Transactional(rollbackFor = Exception.class)
    default ApiResult<Boolean> importExcel(@RequestParam MultipartFile file, HttpServletRequest request) {
        // 判断文件类型是否是excel文件
        String typeName = "";
        try {
            typeName = FileTypeUtil.getType(file.getInputStream(), file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"xls".equals(typeName) && !"xlsx".equals(typeName)) {
            return fail("不允许的文件类型");
        }

        // 如果需要对导入的excel数据进行校验，如果校验失败“是否不处理校验通过的数据”而直接返回通知前端校验失败  说明：仅isNeedVerify有值时生效
        String flag = request.getParameter("verifyFailReturn");
        boolean verifyFailReturn = StrUtil.isBlank(flag) ? true : Convert.toBool(flag, true);

        // 获取导入参数
        ImportParams importParams = getImportParams(request);

        // 获取导入的数据
        List<ImportBean> list = null;
        try {
            if (importParams.isNeedVerify()) {
                // 解析并校验导入的excel数据
                ExcelImportResult<ImportBean> result = ExcelImportUtil.importExcelMore(file.getInputStream(), getImportExcelClass(), importParams);

                // 有校验不通过的数据是否直接返回
                if (result.isVerifyFail() && verifyFailReturn) {

                    String message = result.getFailList().
                            stream()
                            .map(it -> StrUtil.format("第{}行校验错误：{}", it.getRowNum(), it.getErrorMsg()))
                            .collect(Collectors.joining("\n"));
                    return fail(message, null);
                }
                list = result.getList();
            } else {
                // 解析导入的excel数据
                list = ExcelImportUtil.importExcel(file.getInputStream(), getImportExcelClass(), importParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CollUtil.isEmpty(list)) return fail("导入数据为空!");

        // 处理导入数据
        return handlerImport(list);
    }


    /**
     * 构造导入参数
     *
     * @param request HttpServletRequest
     * @return ImportParams 导入参数
     */
    default ImportParams getImportParams(HttpServletRequest request) {
        ImportParams importParams = new ImportParams();

        // 获取所有请求参数名
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            // 获取请求参数值，如果值为空不处理
            String name = names.nextElement();
            String value = request.getParameter(name);
            if (StrUtil.isEmpty(value)) continue;

            // 如果参数名和importParams参数名匹配，则设置值
            switch (name) {
                case "titleRows":
                    // 表格标题行数,默认0 见文章：https://blog.csdn.net/weixin_43009990/article/details/106609660
                    importParams.setTitleRows(Convert.toInt(value, 0));
                    break;
                case "headRows":
                    // 表头行数,默认1
                    importParams.setHeadRows(Convert.toInt(value, 1));
                    break;
                case "startRows":
                    // 字段真正值和列标题之间的距离 默认0
                    importParams.setStartRows(Convert.toInt(value, 0));
                    break;
                case "startSheetIndex":
                    // 开始读取的sheet位置,默认为0
                    importParams.setStartSheetIndex(Convert.toInt(value, 0));
                    break;
                case "sheetNum":
                    // 上传表格需要读取的sheet数量,默认为1
                    importParams.setSheetNum(Convert.toInt(value, 1));
                    break;
                case "needVerify":
                    // 是否需要校验上传的Excel,默认为false
                    importParams.setNeedVerify(Convert.toBool(value, false));
                    break;
                case "lastOfInvalidRow":
                    // 最后的无效行数
                    importParams.setLastOfInvalidRow(Convert.toInt(value, 0));
                    break;
                // 手动控制读取的行数
                case "readRows":
                    importParams.setReadRows(Convert.toInt(value, 0));
                    break;
            }
        }

        // ImportParams设置补充
        enhanceImportParams(importParams);
        return importParams;
    }

    /**
     * 导入参数增强
     *
     * 说明：
     * 你可以在这里对ImportParams配置进行一些补充
     * 例如设置excel验证规则、校验组、校验处理接口等
     *
     * @param importParams 导入参数 不可为空
     */
    default void enhanceImportParams(ImportParams importParams) { }

    /**
     * 处理导入数据
     *
     * 说明：
     * 你需要手动实现导入逻辑
     *
     * @param list 导入数据。不可为空
     */
    default ApiResult<Boolean> handlerImport(List<ImportBean> list) {
        return fail("请在子类Controller重写导入方法，实现导入逻辑");
    }

}
