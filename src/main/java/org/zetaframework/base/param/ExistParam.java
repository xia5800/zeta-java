package org.zetaframework.base.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.reflection.property.PropertyNamer;

import javax.validation.constraints.NotEmpty;

/**
 * 验证存在参数
 *
 * @author gcc
 */
public class ExistParam<Entity, Id> {

    /** 检查的字段名 */
    @ApiModelProperty(value = "检查的字段", required = true)
    @NotEmpty(message = "检查的字段不能为空")
    private String field;

    /** 检查的字段值 */
    @ApiModelProperty(value = "检查字段的值", required = true)
    @NotEmpty(message = "检查的字段值不能为空")
    private String value;

    /** 主键字段的值  修改时用到 */
    @ApiModelProperty(value = "主键字段的值，修改时用到", required = false)
    private Id id;


    private ExistParam() {
    }

    /**
     * 验证存在参数 构造方法
     *
     * @param field 检查的字段名
     * @param value 检查的字段值
     * @param id 主键字段的值,修改时用到
     */
    public ExistParam(String field, String value, Id id) {
        this.field = field;
        this.value = value;
        this.id = id;
    }

    /**
     * 验证存在参数 构造方法
     *
     * @param field 检查的字段名
     * @param value 检查的字段值
     */
    public ExistParam(String field, String value) {
        this.field = field;
        this.value = value;
        this.id = null;
    }

    /**
     * 验证存在参数 构造方法
     * 支持SysUser::getUsername这种写法
     *
     * @param field 检查的字段名
     * @param value 检查的字段值
     * @param id 主键字段的值,修改时用到
     */
    public ExistParam(SFunction<Entity, ?> field, String value, Id id) {
        this.field = field.getClass().getName();
        this.value = value;
        this.id = id;
    }

    /**
     * 验证存在参数 构造方法
     * 支持SysUser::getUsername这种写法
     *
     * @param field 检查的字段名
     * @param value 检查的字段值
     */
    public ExistParam(SFunction<Entity, ?> field, String value) {
        // 参考：[AbstractLambdaWrapper]的columnToString()方法
        LambdaMeta meta = LambdaUtils.extract(field);
        this.field = PropertyNamer.methodToProperty(meta.getImplMethodName());
        this.value = value;
        this.id = null;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    /**
     * 验证是否存在
     *
     * 说明：
     * 这个方法本质上是构造查询条件并利用 service.count()方法来进行查询，判断是否有返回值。
     * 返回值 > 0说明存在，否则说明不存在。
     *
     * 使用方式：
     * <pre>{@code
     *     // 语法：
     *     new ExistParam<实体类,主键字段类型>(检查的字段名, 检查的字段值, 主键字段的值).isExist(service, 主键字段名);
     *     new ExistParam<SysUser, Long>("account", "admin", 1L).isExist(userService, "id");
     *     new ExistParam<SysUser, Long>(SysUser::getUsername, "admin", 1L).isExist(userService, "id");
     *
     *     // 新增用户的时候，判断账号是否已存在
     *     ExistParam param = new ExistParam<SysUser, Long>("account", "admin");
     *     if (param.isExist(userService)) { return fail("账号已存在"); }
     *
     *     // 修改用户的时候，判断账号是否已存在
     *     ExistParam param = new ExistParam<SysUser, Long>("account", "admin", 1L);
     *     if (param.isExist(userService, "id")) { return fail("账号已存在"); }
     *     等价于
     *     if (param.isExist(userService, SysUser::getId)) { return fail("账号已存在"); }
     *     若主键字段名是id，还可以省略掉idField参数
     *     if (param.isExist(userService)) { return fail("账号已存在"); }
     * }</pre>
     *
     * @param service IService<Entity>   service
     * @return Boolean
     */
    public Boolean isExist(IService<Entity> service) {
        return isExist(service, "id", true);
    }

    /**
     * 验证是否存在
     *
     * 说明：
     * 这个方法本质上是构造查询条件并利用 service.count()方法来进行查询，判断是否有返回值。
     * 返回值 > 0说明存在，否则说明不存在。
     *
     * 使用方式：
     * <pre>{@code
     *     // 语法：
     *     new ExistParam<实体类,主键字段类型>(检查的字段名, 检查的字段值, 主键字段的值).isExist(service, 主键字段名);
     *     new ExistParam<SysUser, Long>("account", "admin", 1L).isExist(userService, "id");
     *     new ExistParam<SysUser, Long>(SysUser::getUsername, "admin", 1L).isExist(userService, "id");
     *
     *     // 新增用户的时候，判断账号是否已存在
     *     ExistParam param = new ExistParam<SysUser, Long>("account", "admin");
     *     if (param.isExist(userService)) { return fail("账号已存在"); }
     *
     *     // 修改用户的时候，判断账号是否已存在
     *     ExistParam param = new ExistParam<SysUser, Long>("account", "admin", 1L);
     *     if (param.isExist(userService, "id")) { return fail("账号已存在"); }
     *     等价于
     *     if (param.isExist(userService, SysUser::getId)) { return fail("账号已存在"); }
     *     若主键字段名是id，还可以省略掉idField参数
     *     if (param.isExist(userService)) { return fail("账号已存在"); }
     * }</pre>
     *
     * @param service IService<Entity>   service
     * @param idField String            主键字段, 例如：id, userId, user_id
     * @return Boolean
     */
    public Boolean isExist(IService<Entity> service, SFunction<Entity, ?> idField) {
        return isExist(service, idField.getClass().getName(), false);
    }

    /**
     * 验证是否存在
     *
     * 说明：
     * 这个方法本质上是构造查询条件并利用 service.count()方法来进行查询，判断是否有返回值。
     * 返回值 > 0说明存在，否则说明不存在。
     *
     * 使用方式：
     * <pre>{@code
     *     // 语法：
     *     new ExistParam<实体类,主键字段类型>(检查的字段名, 检查的字段值, 主键字段的值).isExist(service, 主键字段名);
     *     new ExistParam<SysUser, Long>("account", "admin", 1L).isExist(userService, "id");
     *     new ExistParam<SysUser, Long>(SysUser::username, "admin", 1L).isExist(userService, "id");
     *
     *     // 新增用户的时候，判断账号是否已存在
     *     ExistParam param = new ExistParam<SysUser, Long>("account", "admin");
     *     if (param.isExist(userService)) { return fail("账号已存在"); }
     *
     *     // 修改用户的时候，判断账号是否已存在
     *     ExistParam param = new ExistParam<SysUser, Long>("account", "admin", 1L);
     *     if (param.isExist(userService, "id")) { return fail("账号已存在"); }
     *     等价于
     *     if (param.isExist(userService, SysUser::id)) { return fail("账号已存在"); }
     *     若主键字段名是id，还可以省略掉idField参数
     *     if (param.isExist(userService)) { return fail("账号已存在"); }
     * }</pre>
     *
     * @param service IService<Entity>   service
     * @param idField String            主键字段, 例如：id, userId, user_id
     * @return Boolean
     */
    public Boolean isExist(IService<Entity> service, String idField) {
        return isExist(service, idField, true);
    }


    /**
     * 验证是否存在
     *
     * 说明：
     * 这个方法本质上是构造查询条件并利用 service.count()方法来进行查询，判断是否有返回值。
     * 返回值 > 0说明存在，否则说明不存在。
     *
     * 使用方式：
     * <pre>{@code
     *     // 语法：
     *     new ExistParam<实体类,主键字段类型>(检查的字段名, 检查的字段值, 主键字段的值).isExist(service, 主键字段名);
     *     new ExistParam<SysUser, Long>("account", "admin", 1L).isExist(userService, "id");
     *     new ExistParam<SysUser, Long>(SysUser::username, "admin", 1L).isExist(userService, "id");
     *
     *     // 新增用户的时候，判断账号是否已存在
     *     ExistParam param = new ExistParam<SysUser, Long>("account", "admin");
     *     if (param.isExist(userService)) { return fail("账号已存在"); }
     *
     *     // 修改用户的时候，判断账号是否已存在
     *     ExistParam param = new ExistParam<SysUser, Long>("account", "admin", 1L);
     *     if (param.isExist(userService, "id")) { return fail("账号已存在"); }
     *     等价于
     *     if (param.isExist(userService, SysUser::id)) { return fail("账号已存在"); }
     *     若主键字段名是id，还可以省略掉idField参数
     *     if (param.isExist(userService)) { return fail("账号已存在"); }
     * }</pre>
     *
     * @param service IService<Entity>   service
     * @param idField String            主键字段, 例如：id, userId, user_id
     * @param isToUnderlineCase Boolean  检查的字段的字段是否驼峰转下划线
     * @return Boolean
     */
    public Boolean isExist(IService<Entity> service, String idField, Boolean isToUnderlineCase) {
        if (StrUtil.hasBlank(this.field, this.value)) {
            return false;
        }

        // 处理字段名，是否驼峰转下划线
        String fieldName = isToUnderlineCase ? StrUtil.toUnderlineCase(field) : field;

        // 构造查询条件
        QueryWrapper<Entity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(fieldName, value);
        if (id != null) {
            // 如果是修改
            queryWrapper.ne(idField, id);
        }

        // 验证字段是否存在
        return service.count(queryWrapper) > 0;
    }

}
