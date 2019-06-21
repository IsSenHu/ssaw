package com.ssaw.bht.impl.condition;


import com.ssaw.bht.abs.BaseCondition;
import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.infs.IBehaviour;

/**
 * @author HuSen
 */
public class ConditionIsEnemyDead extends BaseCondition {

  public ConditionIsEnemyDead(boolean b) {
    setNegation(b);
  }

  @Override
  public EStatus update() {
    int random = getRandom();
    if (random < 60) {
      System.out.println("Enemy Is Dead");
      return !isNegation() ? EStatus.Success : EStatus.Failure;
    } else {
      System.out.println("Enemy is Not Dead");
      return !isNegation() ? EStatus.Failure : EStatus.Success;
    }

  }

  @Override
  public void addChild(IBehaviour child) {}
}
