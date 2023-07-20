package org.zetaframework.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 包括id、create_time、create_by、update_by、update_time字段的表继承的基础实体
 *
 * @author gcc
 */
@Getter
@Setter
@ToString(callSuper = true)
public class Entity<T> extends SuperEntity<T> {

    public static final String UPDATE_TIME = "updateTime";
    public static final String UPDATE_TIME_COLUMN = "update_time";
    public static final String UPDATED_BY = "updatedBy";
    public static final String UPDATED_BY_COLUMN = "updated_by";

    private static final long serialVersionUID = 1L;

    /** 最后修改时间 */
    @ApiModelProperty(value = "最后修改时间")
    @TableField(value = UPDATE_TIME_COLUMN, fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;

    /** 最后修改人ID */
    @ApiModelProperty(value = "最后修改人ID")
    @TableField(value = UPDATED_BY_COLUMN, fill = FieldFill.INSERT_UPDATE)
    protected T updatedBy;

    public Entity() {
    }

    public Entity(LocalDateTime updateTime, T updatedBy) {
        this.updateTime = updateTime;
        this.updatedBy = updatedBy;
    }

    public Entity(T id, LocalDateTime createTime, T createdBy, LocalDateTime updateTime, T updatedBy) {
        super(id, createTime, createdBy);
        this.updateTime = updateTime;
        this.updatedBy = updatedBy;
    }

}
