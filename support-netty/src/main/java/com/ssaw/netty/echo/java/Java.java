package com.ssaw.netty.echo.java;

import java.util.concurrent.*;

/**
 * @author HuSen
 * create on 2019/6/25 17:48
 */
public class Java {
    public static void main(String[] args) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5, (ThreadFactory) Thread::new);
        executor.schedule(() -> System.out.println("ok"), 5, TimeUnit.SECONDS);
        executor.shutdown();
    }
}
