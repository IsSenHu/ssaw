package com.ssaw.bht.node.impl.control;

import com.google.common.collect.Lists;
import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.node.Node;
import com.ssaw.bht.node.abs.BaseControlNode;
import com.ssaw.bht.precondition.PreConditionType;

import java.util.List;

/**
 * 判断条件 选择一个满足条件的节点来执行
 *
 * @author HuSen
 * create on 2019/6/21 17:53
 */
public class SelectorNode extends BaseControlNode {

    public SelectorNode(String id) {
        super(id);
    }

    @Override
    protected EStatus exec(List<Node> toBe) {
        if (null != toBe) {
            return toBe.get(0).exec();
        }
        return EStatus.Failure;
    }

    @Override
    protected List<Node> select(List<Node> children) {
        for (Node child : children) {
            if (child.pre() == PreConditionType.ENABLE) {
                return Lists.newArrayList(child);
            }
        }
        return null;
    }

    @Override
    public PreConditionType pre() {
        return PreConditionType.ENABLE;
    }
}
