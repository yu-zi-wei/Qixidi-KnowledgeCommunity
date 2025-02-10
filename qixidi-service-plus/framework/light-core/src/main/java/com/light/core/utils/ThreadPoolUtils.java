package com.light.core.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 定义线程池
 */
@Configuration
public class ThreadPoolUtils {

    /**
     * 核心线程数 = cpu 核心数 + 1
     */
    private final int core = Runtime.getRuntime().availableProcessors() + 1;

    @Bean(value = "threadPoolInstance")
    public ExecutorService createThreadPoolInstance() {
        //通过guava类库的ThreadFactoryBuilder来实现线程工厂类并设置线程名称
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("thread-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(core,
                core * 2,
                60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(100),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());//超出限定，不做处理，并抛出异常
        return threadPool;
    }

}
