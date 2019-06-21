package com.ssaw.bht.node.abs;

import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.node.ActionNode;

/**
 * @author HuSen
 * create on 2019/6/21 16:46
 */
public abstract class BaseActionNode extends BaseNode implements ActionNode {

    public BaseActionNode(String id) {
        super(id);
    }

    @Override
    public EStatus exec() {
        return null;
    }
}
