package org.zetaframework.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.zetaframework.core.validation.group.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 包括id、create_time、create_by字段的表继承的基础实体
 *
 * @author gcc
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SuperEntity<T> implements Serializable {

    public static final String FIELD_ID = "id";
    public static final String CREATE_TIME = "createTime";
    public static final String CREATE_TIME_COLUMN = "create_time";
    public static final String CREATED_BY = "createdBy";
    public static final String CREATED_BY_COLUMN = "created_by";

    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(value = FIELD_ID, type = IdType.INPUT)
    @ApiModelProperty(value = "主键")
    @NotNull(message = "id不能为空", groups = Update.class)
    protected T id;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @TableField(value = CREATE_TIME_COLUMN, fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    /** 创建人ID */
    @ApiModelProperty(value = "创建人ID")
    @TableField(value = CREATED_BY_COLUMN, fill = FieldFill.INSERT)
    protected T createdBy;

}
