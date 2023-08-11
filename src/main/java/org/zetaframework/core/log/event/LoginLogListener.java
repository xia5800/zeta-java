package org.zetaframework.core.log.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.zetaframework.core.log.model.LoginLogDTO;

import java.util.function.Consumer;

/**
 * 登录日志事件监听
 *
 * 使用说明：
 * 1. 在业务包中，@Bean配置一个LoginLogListener
 * 2. 保存登录日志的方式交给具体的业务去实现
 * @author gcc
 */
public class LoginLogListener {
    private final Consumer<LoginLogDTO> consumer;

    /**
     * 构造方法
     *
     * 使用方式：
     * <pre>
     *     new LoginLogListener((LoginLogDTO data) -> {
     *          // Do something
     *     })
     * </pre>
     */
    public LoginLogListener(Consumer<LoginLogDTO> consumer) {
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
    @EventListener(LoginEvent.class)
    public void saveSysLoginLog(LoginEvent event) {
        LoginLogDTO loginLogDTO = (LoginLogDTO) event.getSource();
        consumer.accept(loginLogDTO);
    }

}
