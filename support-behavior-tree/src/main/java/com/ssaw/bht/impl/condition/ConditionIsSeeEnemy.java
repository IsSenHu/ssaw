package com.ssaw.bht.impl.condition;

import com.ssaw.bht.abs.BaseCondition;
import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.infs.IBehaviour;

/**
 * @author HuSen
 */
public class ConditionIsSeeEnemy extends BaseCondition {

  private static final int FIFTY = 50;

  @Override
  public EStatus update() {
    int random = getRandom();
    if (random < FIFTY) {
      System.out.println("SeeEnemy");
      return !isNegation() ? EStatus.Success : EStatus.Failure;
    } else {
      System.out.println("Not SeeEnemy");
      return !isNegation() ? EStatus.Failure : EStatus.Success;
    }
  }

  @Override
  public void addChild(IBehaviour child) {}
}
