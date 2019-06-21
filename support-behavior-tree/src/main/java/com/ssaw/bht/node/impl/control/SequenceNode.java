package com.ssaw.bht.node.impl.control;

import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.node.Node;
import com.ssaw.bht.node.abs.BaseControlNode;

import java.util.List;

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
        return null;
    }

    @Override
    protected List<Node> select(List<Node> children) {
        return null;
    }
}
