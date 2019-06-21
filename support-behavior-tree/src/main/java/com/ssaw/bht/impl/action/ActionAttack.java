package com.ssaw.bht.impl.action;

import com.ssaw.bht.abs.BaseAction;
import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.infs.IBehaviour;

/**
 * @author HuSen
 */
public class ActionAttack extends BaseAction {

  @Override
  public EStatus update() {
    System.out.println("ActionAttack 攻击");
    return EStatus.Success;
  }

  @Override
  public void addChild(IBehaviour child) {}
}
