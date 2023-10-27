package com.datacenter.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author 44295 谭帮华 <a href="mailto:banghua.tan@1hai.cn">Contact me.</a>
 * @version 1.0
 * date 2022/10/09 13:56
 * desc : 线程池工具类
 */
@Slf4j
public class ThreadPoolsUtil {
    /**
     * 每个任务，都有自己单独的线程池
     */
    private static final Map<String, ExecutorService> EXECUTORS = new ConcurrentHashMap<>();
    /**
     * 默认线程池大小
     */
    private static final Integer DEFAULT_POOL_SIZE = 2 << 1;

    private ThreadPoolsUtil() {

    }

    /**
     * 初始化一个线程池
     *
     * @param poolName 池名称
     * @param poolSize 池大小
     * @return ExecutorService 线程池服务
     */
    private static ExecutorService init(String poolName, int poolSize) {
        final int poolSizeDealt = poolSize < DEFAULT_POOL_SIZE ? DEFAULT_POOL_SIZE : poolSize;
        // poolSizeDealt * 1.25
        int maxPoolSizeDealt = poolSizeDealt + (poolSizeDealt >> 2);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(poolSizeDealt, maxPoolSizeDealt,
                1L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadFactoryBuilder().setNameFormat("Pool-" + poolName).setDaemon(false).build(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        threadPoolExecutor.allowCoreThreadTimeOut(true);
        // JVM钩子
        Runtime.getRuntime().addShutdownHook(
                new Thread(new FutureTask<Void>(() -> {
                    threadPoolExecutor.shutdown();
                    log.info(String.format("钩子[%s]正在关闭线程池[%s]", Thread.currentThread().getName(), poolName));
                    return null;
                }
                ))
        );
        return threadPoolExecutor;
    }

    /**
     * 获取线程池
     *
     * @param poolName 池名称
     * @return ExecutorService 线程池服务
     */
    public static ExecutorService getOrInitExecutors(String poolName) {
        return ThreadPoolsUtil.getOrInitExecutors(poolName, DEFAULT_POOL_SIZE);
    }

    /**
     * 获取线程池
     *
     * @param poolName 池名称
     * @param poolSize 池大小
     * @return ExecutorService
     */
    public static ExecutorService getOrInitExecutors(String poolName, int poolSize) {
        return EXECUTORS.computeIfAbsent(poolName, key -> init(key, poolSize));
    }


    /**
     * 释放线程资源
     *
     * @param poolName 池名称
     */
    public static void releaseExecutors(String poolName) {
        ExecutorService executorService = EXECUTORS.remove(poolName);
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}