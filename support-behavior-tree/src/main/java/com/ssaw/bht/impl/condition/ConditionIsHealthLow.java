package com.ssaw.bht.impl.condition;


import com.ssaw.bht.abs.BaseCondition;
import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.infs.IBehaviour;

/**
 * @author HuSen
 */
public class ConditionIsHealthLow extends BaseCondition {

  private static final int THIRTY = 30;

  @Override
  public EStatus update() {
    int random = getRandom();
    if (random < THIRTY) {
      System.out.println("Health is low");
      return !isNegation() ? EStatus.Success : EStatus.Failure;
    } else {
      System.out.println("Health is Not low");
      return !isNegation() ? EStatus.Failure : EStatus.Success;
    }

  }

  @Override
  public void addChild(IBehaviour child) {}
}
