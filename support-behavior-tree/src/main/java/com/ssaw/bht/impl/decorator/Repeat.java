package com.ssaw.bht.impl.decorator;

import com.ssaw.bht.abs.BaseDecorator;
import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.infs.IBehaviour;

/**
 * @author HuSen
 */
public class Repeat extends BaseDecorator {

  private int count = 0;

  @Override
  public EStatus update() {
    while (true) {
      child.tick();
      switch (child.getStatus()) {
        case Running:
          return EStatus.Success;
        case Failure:
          return EStatus.Failure;
        default:
          break;
      }
      int limited = 3;
      if (++count > limited) {
        return EStatus.Success;
      }
      child.reset();
    }
  }

  @Override
  public void onInitialize() {
    count = 0;
  }

  @Override
  public void addChild(IBehaviour child) {
    super.addChild(child);
  }
}
