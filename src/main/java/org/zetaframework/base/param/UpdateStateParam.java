package org.zetaframework.base.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 修改状态参数
 *
 * @author gcc
 */
@ApiModel(description = "修改状态参数")
public class UpdateStateParam<Id, State> {

    /** id */
    @ApiModelProperty(value = "id", required = true)
    private Id id;

    /** 状态 */
    @ApiModelProperty(value = "state", required = true)
    private State state;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
