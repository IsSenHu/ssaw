package com.ssaw.bht.infs;

import java.util.List;

/**
 * 组合结点
 *
 * @author HuSen
 * create on 2019/6/21 15:17
 */
public interface IComposite extends IBehaviour {

    /**
     * 移除子节点
     *
     * @param child 子节点
     */
    void removeChild(IBehaviour child);

    /**
     * 清除子节点
     */
    void clearChild();

    /**
     * 获取所有子节点
     *
     * @return 所有子节点
     */
    List<IBehaviour> getChildren();

    /**
     * 设置所有子节点
     *
     * @param behaviours 所有的子节点
     */
    void setChildren(List<IBehaviour> behaviours);
}
