package org.zetaframework.core.log.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.zetaframework.core.log.model.SysLogDTO;

import java.util.function.Consumer;

/**
 * 系统日志事件监听
 *
 * 使用说明：
 * 1. 在业务包中，@Bean配置一个SysLogListener
 * 2. 保存系统日志的方式交给具体的业务去实现
 * @author gcc
 */
public class SysLogListener {
    private Consumer<SysLogDTO> consumer;

    /**
     * 构造方法
     *
     * 使用方式：
     * <pre>
     *     new SysLogListener((SysLogDTO data) -> {
     *          // Do something
     *     })
     * </pre>
     * @param consumer
     */
    public SysLogListener(Consumer<SysLogDTO> consumer) {
        this.consumer = consumer;
    }

    /**
     * 保存系统日志
     *
     * 说明：
     * 该方法不实现，交给具体业务去实现
     * @param event 登录日志事件
     */
    @Async
    @EventListener(SysLogEvent.class)
    public void saveSysLoginLog(SysLogEvent event) {
        SysLogDTO sysLogDTO = (SysLogDTO) event.getSource();
        consumer.accept(sysLogDTO);
    }

    public Consumer<SysLogDTO> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<SysLogDTO> consumer) {
        this.consumer = consumer;
    }
}
