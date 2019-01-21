package com.ssaw.shutdown;

/**
 * @author HuSen.
 * @date 2019/1/18 17:36.
 */
public interface ShutdownLatchMBean {

    /**
     * 停止应用的方法
     * @return 实例名
     */
    String shutdown();


    /**
     * 等待
     * @throws Exception 异常
     */
    void await() throws Exception;
}
