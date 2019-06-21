package com.ssaw.bht.precondition;

/**
 * 判断接口
 *
 * @author HuSen
 * create on 2019/6/21 14:28
 */
@FunctionalInterface
public interface Judge {

    /**
     *  是否满足
     *
     * @param preConditions 前提
     * @return 是否满足
     */
    boolean satisfy(PreCondition... preConditions);
}
