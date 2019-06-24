package com.ssaw.bht.node.impl.control;

import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.node.Node;
import com.ssaw.bht.node.abs.BaseControlNode;
import com.ssaw.bht.precondition.PreConditionType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * create on 2019/6/21 17:54
 */
public class SequenceNode extends BaseControlNode {
    public SequenceNode(String id) {
        super(id);
    }

    @Override
    protected EStatus exec(List<Node> toBe) {
        for (Node node : toBe) {
            EStatus status = node.exec();
            if (status == EStatus.Running || status == EStatus.Failure) {
                return status;
            } else if (status != EStatus.Success) {
                return EStatus.Failure;
            }
        }
        return EStatus.Success;
    }

    @Override
    protected List<Node> select(List<Node> children) {
        return children.stream().filter(n -> n.pre() == PreConditionType.ENABLE).collect(Collectors.toList());
    }

    @Override
    public PreConditionType pre() {
        return PreConditionType.ENABLE;
    }
}
