package io.money.boot.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author lishijie-me
 * {@code @date} 2025/11/9 星期日 16:30:01
 * {@code @description} AsyncConfig
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    // 异步线程池配置
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(10);
        // 设置最大线程数
        executor.setMaxPoolSize(20);
        // 设置队列容量
        executor.setQueueCapacity(100);
        // 设置线程池前缀名
        executor.setThreadNamePrefix("Async-");
        // 初始化线程池
        executor.initialize();
        return executor;
    }
}
