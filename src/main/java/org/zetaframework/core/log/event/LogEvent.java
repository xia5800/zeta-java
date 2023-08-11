package org.zetaframework.core.log.event;

import org.springframework.context.ApplicationEvent;
import org.zetaframework.core.log.model.LogDTO;

/**
 * <h1>系统日志 事件</h1>
 * <br>
 * 说明：
 * 在{@link LogListener}中处理本事件
 * @author gcc
 */
public class LogEvent extends ApplicationEvent {

    public LogEvent(LogDTO source) {
        super(source);
    }

}
