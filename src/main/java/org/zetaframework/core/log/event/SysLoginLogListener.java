package org.zetaframework.core.log.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.zetaframework.core.log.model.SysLoginLogDTO;

import java.util.function.Consumer;

/**
 * 登录日志事件监听
 *
 * 使用说明：
 * 1. 在业务包中，@Bean配置一个SysLoginLogListener
 * 2. 保存登录日志的方式交给具体的业务去实现
 * @author gcc
 */
public class SysLoginLogListener {
    private Consumer<SysLoginLogDTO> consumer;

    /**
     * 构造方法
     *
     * 使用方式：
     * <pre>
     *     new SysLoginLogListener((SysLoginLogDTO data) -> {
     *          // Do something
     *     })
     * </pre>
     * @param consumer
     */
    public SysLoginLogListener(Consumer<SysLoginLogDTO> consumer) {
        this.consumer = consumer;
    }

    /**
     * 保存登录日志
     *
     * 说明：
     * 该方法不实现，交给具体业务去实现
     * @param event 登录日志事件
     */
    @Async
    @EventListener(SysLoginEvent.class)
    public void saveSysLoginLog(SysLoginEvent event) {
        SysLoginLogDTO loginLogDTO = (SysLoginLogDTO) event.getSource();
        consumer.accept(loginLogDTO);
    }

    public Consumer<SysLoginLogDTO> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<SysLoginLogDTO> consumer) {
        this.consumer = consumer;
    }
}
