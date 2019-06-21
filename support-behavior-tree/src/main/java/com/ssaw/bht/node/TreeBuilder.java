package com.ssaw.bht.node;

/**
 * @author HuSen
 * create on 2019/6/21 17:45
 */
public class TreeBuilder {
    private Node node;

    public TreeBuilder(Node node) {
        this.node = node;
    }

    public TreeBuilder build() {
        return this;
    }

    public TreeBuilder addChild(Node node) {
        this.node.addChild(node);
        return this;
    }

    public Node end() {
        return this.node;
    }
}
