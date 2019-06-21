package com.ssaw.bht.infs;

/**
 * 条件基类
 * 具体执行动作
 *
 * @author HuSen
 * create on 2019/6/21 15:19
 */
public interface ICondition extends IBehaviour {

    /**
     * 是否拒绝
     *
     * @return 是否拒绝
     */
    boolean isNegation();

    /**
     * 设置是否拒绝
     *
     * @param negation 是否拒绝
     */
    void setNegation(boolean negation);
}
