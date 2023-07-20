package org.zetaframework.core.log.event;

import org.springframework.context.ApplicationEvent;
import org.zetaframework.core.log.model.SysLoginLogDTO;

/**
 * <h1>登录日志 事件</h1>
 * <br>
 * 说明:
 * 在{@link SysLoginLogListener}中处理本事件
 * @author gcc
 */
public class SysLoginEvent extends ApplicationEvent {

    public SysLoginEvent(SysLoginLogDTO source) {
        super(source);
    }
    
}
