package org.zetaframework.core.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.zetaframework.core.async.properties.AsyncProperties;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步线程配置
 *
 * @author gcc
 */
@Configuration
@EnableAsync
@EnableConfigurationProperties(AsyncProperties.class)
public class AsyncConfiguration implements AsyncConfigurer {

    private final AsyncProperties asyncProperties;
    public AsyncConfiguration(AsyncProperties asyncProperties) {
        this.asyncProperties = asyncProperties;
    }

    /**
     * 配置自定义线程池
     *
     * 配置之后@Async注解会默认使用这个线程池
     * @return Executor
     */
    @Bean("taskExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(asyncProperties.getCorePoolSize());
        // 最大线程数
        executor.setMaxPoolSize(asyncProperties.getMaxPoolSize());
        // 队列大小
        executor.setQueueCapacity(asyncProperties.getQueueCapacity());
        // 线程最大空闲时间
        executor.setKeepAliveSeconds(asyncProperties.getKeepAliveSeconds());
        // 线程名前缀
        executor.setThreadNamePrefix(asyncProperties.getThreadNamePrefix());
        // 拒绝策略
        /*
         * ThreadPoolExecutor.AbortPolicy 丢弃任务并抛出RejectedExecutionException异常(默认)。
         * ThreadPoolExecutor.DiscardPolicy 丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy 丢弃队列最前面的任务，然后重新尝试执行任务
         * ThreadPoolExecutor.CallerRunsPolicy 由调用线程处理该任务
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化线程池
        executor.initialize();
        return executor;
    }

    /**
     * 配置异步线程未捕获异常处理器
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
