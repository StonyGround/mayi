package com.mayizaixian.myzx.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2015/11/9.
 */
public class ThreadPool {

    private ThreadPool() {
    }

    private volatile static ExecutorService executorService;
    private static final int threadNum = 3;
    private volatile static ExecutorService executorServiceNum;

    public static ExecutorService getInstance() {
        if (executorService == null) {
            synchronized (ExecutorService.class) {
                if (executorService == null) {
                    executorService = Executors.newSingleThreadExecutor();
                }
            }
        }
        return executorService;
    }

    public static ExecutorService getFixedInstance() {
        if (executorServiceNum == null) {
            synchronized (ExecutorService.class) {
                if (executorServiceNum == null) {
                    executorServiceNum = Executors.newFixedThreadPool(threadNum);
                }
            }
        }
        return executorServiceNum;
    }

}
