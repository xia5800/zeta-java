package org.zetaframework.core.async.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 异步线程配置
 *
 * @author gcc
 */
@ConfigurationProperties(prefix = AsyncProperties.PREFIX)
public class AsyncProperties {
    public static final String PREFIX = "zeta.async";

    /** 异步核心线程数，默认：10 */
    private Integer corePoolSize = 10;

    /** 异步最大线程数，默认：20 */
    private Integer maxPoolSize = 20;

    /** 队列容量，默认：1000 */
    private Integer queueCapacity = 1000;

    /** 线程存活时间，默认：300 */
    private Integer keepAliveSeconds = 300;

    /** 线程名前缀 */
    private String threadNamePrefix = "zeta-async-executor-";


    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public Integer getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(Integer keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }
}
