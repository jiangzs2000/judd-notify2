package com.shuyuan.judd.notify.config.ThreadPoolConfig;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author Sting
 * create 2018/11/30

 **/
@Component
@Data
public class ThreadPoolConfig {
    //核心线程数
    private int corePoolSize = 5;

    //最大线程数
    private int maxPoolSize = 10;

    //线程池维护线程所允许的空闲时间
    private int keepAliveSeconds = 60;

    //队列长度
    private int queueCapacity = 100;

    //线程名称前缀
    private String threadNamePrefix = "biz-threadTask-";

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }
}
