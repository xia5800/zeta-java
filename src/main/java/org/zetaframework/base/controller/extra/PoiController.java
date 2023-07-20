package org.zetaframework.base.controller.extra;

import org.zetaframework.base.entity.ImportPoi;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Excel导入导出 Controller
 *
 *
 * @param <ImportBean>  Excel导入实体  必须继承{@link ImportPoi}类
 * @param <ExportBean>  Excel导出实体
 * @param <Entity>      实体
 * @param <QueryParam>  查询参数
 *
 * @author gcc
 */
public interface PoiController<ImportBean extends ImportPoi, ExportBean, Entity, QueryParam> extends
        ImportController<ImportBean, Entity>,
        ExportController<ExportBean, Entity, QueryParam>
{

    /**
     * 获取导入实体的类型
     */
    @Override
    default Class<ImportBean> getImportExcelClass() {
        Optional<Type> first = Arrays.stream(this.getClass().getGenericInterfaces())
                // 筛选出类上面的PoiController接口
                .filter(it -> Objects.equals(((ParameterizedType) it).getRawType().getTypeName(), PoiController.class.getTypeName()))
                .findFirst();
        if (first.isEmpty()) return null;

        Type type = first.get();
        // 获取当前接口的第一个泛型的值(下标从0开始)。 即ImportBean的值
        Type argument = ((ParameterizedType) type).getActualTypeArguments()[0];
        return (Class<ImportBean>) argument;
    }

    /**
     * 获取导出实体的类型
     */
    @Override
    default Class<ExportBean> getExportExcelClass() {
        Optional<Type> first = Arrays.stream(this.getClass().getGenericInterfaces())
                // 筛选出类上面的PoiController接口
                .filter(it -> Objects.equals(((ParameterizedType) it).getRawType().getTypeName(), PoiController.class.getTypeName()))
                .findFirst();
        if (first.isEmpty()) return null;

        Type type = first.get();
        // 获取当前接口的第二个泛型的值(下标从0开始)。 即ExportBean的值
        Type argument = ((ParameterizedType) type).getActualTypeArguments()[1];
        return (Class<ExportBean>) argument;
    }

}
