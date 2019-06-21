package com.ssaw.bht.infs;

import com.ssaw.bht.cons.EStatus;

/**
 * Behavior接口是所有行为树节点的核心，且我规定所有节点的构造和析构方法都必须是protected，以防止在栈上创建对象，
 * 所有的节点对象通过Create()静态方法在堆上创建，通过Release()方法销毁
 * 由于Behavior是个抽象接口，故没有提供Create()方法，本接口满足如下契约
 * 在Update方法被首次调用前，调用一次OnInitialize函数，负责初始化等操作
 * Update（）方法在行为树每次更新时调用且仅调用一次。
 * 当行为不再处于运行状态时，调用一次OnTerminate（），并根据返回状态不同执行不同的逻辑
 * @author HuSen
 * create on 2019/6/21 15:09
 */
public interface IBehaviour {

    /**
     * 设置调用顺序，onInitialize--update--onTerminate
     *
     * @return 状态
     */
    EStatus tick();

    /**
     * 当节点调用前
     */
    void onInitialize();

    /**
     * 节点操作的具体实现
     *
     * @return 状态
     */
    EStatus update();

    /**
     * 节点调用后执行
     *
     * @param status 状态
     */
    void onTerminate(EStatus status);

    /**
     * 释放对象所占资源
     */
    void release();

    /**
     * 添加子节点
     *
     * @param child 子节点
     */
    void addChild(IBehaviour child);

    /**
     * 设置节点状态
     *
     * @param status 状态
     */
    void setStatus(EStatus status);

    /**
     * 获取节点状态
     *
     * @return 状态
     */
    EStatus getStatus();

    /**
     * 重置
     */
    void reset();

    /**
     * 退出
     */
    void abort();
}
