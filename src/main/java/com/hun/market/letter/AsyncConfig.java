package com.hun.market.letter;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {


    @Bean(name = "baobabExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // 기본 스레드 수
        executor.setMaxPoolSize(4); // 최대 스레드 수
        executor.setQueueCapacity(500); // 큐의 크기
        executor.setThreadNamePrefix("baobab-Thread-");
        executor.initialize();
        return executor;
    }
}
