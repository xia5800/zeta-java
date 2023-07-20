package org.zetaframework.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 带状态字段的 实体类
 * 包括id、create_time、create_by、update_by、update_time、state字段的表继承的基础实体
 *
 * 说明：
 * 用于前端修改数据状态
 * @author gcc
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class StateEntity<T, U> extends Entity<T> {

    /** 状态 */
    @ApiModelProperty(value = "状态")
    @TableField(value = "state")
    protected U state;

}
