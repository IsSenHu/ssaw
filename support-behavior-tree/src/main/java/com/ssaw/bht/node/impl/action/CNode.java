package com.ssaw.bht.node.impl.action;

import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.node.abs.BaseActionNode;
import com.ssaw.bht.precondition.PreConditionType;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuSen
 * create on 2019/6/21 17:56
 */
@Slf4j
public class CNode extends BaseActionNode {
    public CNode(String id) {
        super(id);
    }

    @Override
    public EStatus exec() {
        log.info("C......");
        return EStatus.Success;
    }

    @Override
    public PreConditionType pre() {
        return PreConditionType.ENABLE;
    }
}
