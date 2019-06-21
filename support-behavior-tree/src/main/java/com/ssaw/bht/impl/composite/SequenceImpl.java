package com.ssaw.bht.impl.composite;

import com.ssaw.bht.abs.BaseComposite;
import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.infs.IBehaviour;
import com.ssaw.bht.infs.composite.ISequence;

import java.util.Iterator;

/**
 * @author HuSen
 */
public class SequenceImpl extends BaseComposite implements ISequence {

  @Override
  public EStatus update() {
    Iterator<IBehaviour> iterator = getChildren().iterator();
    if (iterator.hasNext()) {
      while (true) {
        IBehaviour currChild = iterator.next();
        EStatus s = currChild.tick();
        //如果执行成功了就继续执行，否则返回
        if (s != EStatus.Success) {
          return s;
        }
        if (!iterator.hasNext()) {
          return EStatus.Success;
        }
      }
    }
    // 循环意外终止
    return EStatus.Initial;
  }
}
