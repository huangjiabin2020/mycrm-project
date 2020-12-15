package com.binge.common.async;

import com.binge.common.spring.GetBean;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author JiaBin Huang
 * @date 2020/10/24
 **/
public class AsyncManager {
    //    所有任务默认延时3s再执行
    private static long delay = 0;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = GetBean.getBean(ScheduledThreadPoolExecutor.class);

    /**
     * 单例模式，私有化构造方法
     */
    private AsyncManager() {
    }

    /**
     * 饿汉式
     */
    private static AsyncManager asyncManager = new AsyncManager();

    /**
     * 获取单例
     *
     * @return
     */
    public static AsyncManager getInstance() {
        return asyncManager;
    }


    /**
     * 执行异步任务，自己new Runnable肯定不优雅，使用工厂模式生产RUnnable进行执行
     *
     * @param runnable
     */
    public void executeTask(Runnable runnable) {
        scheduledThreadPoolExecutor.schedule(runnable, delay, TimeUnit.SECONDS);
    }

    /**
     * 关闭线程池
     */
    public void close() {
        if (!scheduledThreadPoolExecutor.isShutdown()) {
            scheduledThreadPoolExecutor.shutdownNow();
        }
    }

}
