package org.zetaframework.core.log.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.zetaframework.core.log.model.LogDTO;

import java.util.function.Consumer;

/**
 * 系统日志事件监听
 *
 * 使用说明：
 * 1. 在业务包中，@Bean配置一个LogListener
 * 2. 保存系统日志的方式交给具体的业务去实现
 * @author gcc
 */
public class LogListener {
    private Consumer<LogDTO> consumer;

    /**
     * 构造方法
     *
     * 使用方式：
     * <pre>
     *     new LogListener((LogDTO data) -> {
     *          // Do something
     *     })
     * </pre>
     */
    public LogListener(Consumer<LogDTO> consumer) {
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
    @EventListener(LogEvent.class)
    public void saveSysLoginLog(LogEvent event) {
        LogDTO logDTO = (LogDTO) event.getSource();
        consumer.accept(logDTO);
    }

    public Consumer<LogDTO> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<LogDTO> consumer) {
        this.consumer = consumer;
    }
}
