package com.ssaw.bht.node.abs;

import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.node.ControlNode;
import com.ssaw.bht.node.Node;

import java.util.List;

/**
 * @author HuSen
 * create on 2019/6/21 16:48
 */
public abstract class BaseControlNode extends BaseNode implements ControlNode {

    public BaseControlNode(String id) {
        super(id);
    }

    @Override
    public EStatus exec() {
        List<Node> toBe = select(children());
        return exec(toBe);
    }

    protected abstract EStatus exec(List<Node> toBe);

    protected abstract List<Node> select(List<Node> children);
}
