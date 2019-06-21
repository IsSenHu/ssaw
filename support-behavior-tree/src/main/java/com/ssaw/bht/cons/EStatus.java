package com.ssaw.bht.cons;

/**
 * @author HuSen
 * create on 2019/6/21 15:11
 */
public enum EStatus {
    // 初始状态
    Initial,
    // 成功
    Success,
    // 失败
    Failure,
    // 运行
    Running,
    // 终止
    Aborted;

    public boolean is(EStatus status) {
        return this.equals(status);
    }
}
