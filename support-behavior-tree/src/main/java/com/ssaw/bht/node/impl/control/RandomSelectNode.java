package com.ssaw.bht.node.impl.control;

import com.google.common.collect.Lists;
import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.node.Node;
import com.ssaw.bht.node.abs.BaseControlNode;
import com.ssaw.bht.precondition.PreConditionType;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * create on 2019/6/24 10:14
 */
public class RandomSelectNode extends BaseControlNode {
    public RandomSelectNode(String id) {
        super(id);
    }

    @Override
    protected EStatus exec(List<Node> toBe) {
        if (toBe != null && toBe.size() == 1) {
            return toBe.get(toBe.size() - 1).exec();
        }
        return EStatus.Failure;
    }

    @Override
    protected List<Node> select(List<Node> children) {
        children = children.stream().filter(n -> n.pre() == PreConditionType.ENABLE).collect(Collectors.toList());
        int i = RandomUtils.nextInt(0, children.size());
        return Lists.newArrayList(children.get(i));
    }

    @Override
    public PreConditionType pre() {
        return PreConditionType.ENABLE;
    }
}
