package com.ssaw.bht.main;

import com.ssaw.bht.node.Node;
import com.ssaw.bht.node.TreeBuilder;
import com.ssaw.bht.node.impl.action.ANode;
import com.ssaw.bht.node.impl.action.BNode;
import com.ssaw.bht.node.impl.action.CNode;
import com.ssaw.bht.node.impl.action.DNode;
import com.ssaw.bht.node.impl.control.RandomSelectNode;
import com.ssaw.bht.node.impl.control.SelectorNode;

/**
 * @author HuSen
 * create on 2019/6/21 14:58
 */
public class Main {
    public static void main(String[] args) {
        TreeBuilder builder = new TreeBuilder(new RandomSelectNode("1"));
        Node node = builder
                .addChild(new SelectorNode("1-1"))
                    .addChild(new ANode("1-1-1"))
                    .addChild(new BNode("1-1-2"))
                .addChild(new RandomSelectNode("1-2"))
                    .addChild(new ANode("1-2-1"))
                    .addChild(new CNode("1-2-2"))
                .addChild(new SelectorNode("1-3"))
                    .addChild(new DNode("1-3-1"))
                .end();
        node.run();
    }
}
