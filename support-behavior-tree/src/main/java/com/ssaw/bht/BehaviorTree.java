package com.ssaw.bht;


import com.ssaw.bht.infs.IBehaviour;

/**
 * @author HuSen
 */
public class BehaviorTree {
  private IBehaviour root;

  BehaviorTree(IBehaviour root) {
    this.root = root;
  }

  public void tick() {
    root.tick();
  }

  public boolean haveRoot() {
    return root != null;
  }

  public void setRoot(IBehaviour inNode) {
    root = inNode;
  }

  public void release() {
    root.release();
  }

}