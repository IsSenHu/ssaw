package com.ssaw.bht.node;


import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.precondition.PreConditionType;

import java.util.List;

/**
 * @author HuSen
 * create on 2019/6/21 15:02
 */
public interface Node {
    String id();

    EStatus getStatus();

    void setStatus(EStatus status);

    void addChild(Node node);

    Node getChild(String id);

    EStatus run();

    EStatus exec();

    /**
     * 当节点调用前
     */
    void onInitialize();

    /**
     * 节点调用后执行
     *
     * @param status 状态
     */
    void onTerminate(EStatus status);

    List<Node> children();

    PreConditionType pre();
}
