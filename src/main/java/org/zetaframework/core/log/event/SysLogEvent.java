package org.zetaframework.core.log.event;

import org.springframework.context.ApplicationEvent;
import org.zetaframework.core.log.model.SysLogDTO;

/**
 * <h1>系统日志 事件</h1>
 * <br>
 * 说明：
 * 在{@link SysLogListener}中处理本事件
 * @author gcc
 */
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(SysLogDTO source) {
        super(source);
    }

}
