package com.zeta.common.config;

import com.zeta.system.service.ISysLoginLogService;
import com.zeta.system.service.ISysOptLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zetaframework.core.log.event.LogListener;
import org.zetaframework.core.log.event.LoginLogListener;

/**
 * 日志事件配置
 *
 * @author gcc
 */
@RequiredArgsConstructor
@Configuration
public class LogEventConfiguration {

    private final ISysOptLogService sysOptLogService;
    private final ISysLoginLogService sysLoginLogService;

    /**
     * 配置系统日志事件监听 实现
     *
     * 说明：
     * 考虑到不是所有项目都有记录用户操作日志的需求。故将日志记录功能剥离出来放到zetaframework核心包中。
     * 开发者可以在配置文件中将"zeta.log.enable"的值改成false。这样就关闭用户操作日志记录功能了
     * @return {@link LogListener}
     */
    @Bean
    public LogListener sysLogListener() {
        return new LogListener(sysOptLogService::save);
    }

    /**
     * 配置登录日志事件监听 实现
     *
     * 说明:
     * zetaframework核心包中，定义了登录日志事件{@link LogListener}和相应的事件监听{@link LoginLogListener}。
     * 开发者只需要发布对应的登录事件，并实现具体的登录日志存储业务即可。
     */
    @Bean
    public LoginLogListener sysLoginLogListener() {
        return new LoginLogListener(sysLoginLogService::save);
    }

}
