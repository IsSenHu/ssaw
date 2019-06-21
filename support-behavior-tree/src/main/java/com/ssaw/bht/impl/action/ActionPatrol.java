package com.ssaw.bht.impl.action;


import com.ssaw.bht.abs.BaseAction;
import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.infs.IBehaviour;

public class ActionPatrol extends BaseAction {

  @Override
  public EStatus update() {
    System.out.println("ActionPatrol 巡逻");
    return EStatus.Success;
  }

  @Override
  public void addChild(IBehaviour child) {}
}
