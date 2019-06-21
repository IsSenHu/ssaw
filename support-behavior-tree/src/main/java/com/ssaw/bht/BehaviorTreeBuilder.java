package com.ssaw.bht;

import com.ssaw.bht.infs.IBehaviour;

import java.util.Stack;

/**
 * @author HuSen
 */
public class BehaviorTreeBuilder {
  private Stack<IBehaviour> behaviourStack = new Stack<>();
  private IBehaviour treeRoot = null;

  public BehaviorTreeBuilder addBehaviour(IBehaviour behaviour) {
    // assert(NewBehavior);
    // 如果没有根节点设置新节点为根节点
    // 否则设置新节点为堆栈顶部节点的子节点
    if (treeRoot == null) {
      treeRoot = behaviour;
    } else {
      behaviourStack.peek().addChild(behaviour);
    }

    // 将新节点压入堆栈
    behaviourStack.push(behaviour);
    return this;
  }

  public BehaviorTreeBuilder back() {
    behaviourStack.pop();
    return this;
  }

  public BehaviorTree end() {
    while (!behaviourStack.empty()) {
      behaviourStack.pop();
    }
    return new BehaviorTree(treeRoot);
  }
}