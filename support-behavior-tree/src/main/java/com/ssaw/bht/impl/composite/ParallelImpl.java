package com.ssaw.bht.impl.composite;

import com.ssaw.bht.abs.BaseComposite;
import com.ssaw.bht.cons.EPolicy;
import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.infs.IBehaviour;
import com.ssaw.bht.infs.composite.IParallel;

public class ParallelImpl extends BaseComposite implements IParallel {

  private EPolicy successPolicy;
  private EPolicy failPolicy;

  public ParallelImpl(EPolicy successPolicy, EPolicy failPolicy) {
    this.successPolicy = successPolicy;
    this.failPolicy = failPolicy;
  }

  @Override
  public EStatus update() {
    int successCount = 0, failureCount = 0;
    int childrenSize = getChildren().size();
    for (IBehaviour iBehaviour : getChildren()) {
      //如果行为已经终止则不再执行该行为
      if (!(iBehaviour.getStatus().equals(EStatus.Success) || iBehaviour.getStatus().equals(EStatus.Failure))) {
        iBehaviour.tick();
      }

      if (iBehaviour.getStatus().equals(EStatus.Success)) {
        ++successCount;
        if (successPolicy.equals(EPolicy.RequireOne)) {
          iBehaviour.reset();
          return EStatus.Success;
        }
      }

      if (iBehaviour.getStatus().equals(EStatus.Failure)) {
        ++failureCount;
        if (failPolicy.equals(EPolicy.RequireOne)) {
          iBehaviour.reset();
          return EStatus.Failure;
        }
      }
    }

    if (failPolicy.equals(EPolicy.RequireAll) && failureCount == childrenSize) {
      for (IBehaviour iBehaviour : getChildren()) {
        iBehaviour.reset();
      }
      return EStatus.Failure;
    }
    if (successPolicy.equals(EPolicy.RequireAll) && successCount == childrenSize) {
      for (IBehaviour iBehaviour : getChildren()) {
        iBehaviour.reset();
      }
      return EStatus.Success;
    }

    return EStatus.Running;
  }

  @Override
  public void onTerminate(EStatus status) {
    for (IBehaviour iBehaviour : getChildren()) {
      if(iBehaviour.getStatus().equals(EStatus.Running)){
        iBehaviour.abort();
      }
      iBehaviour.reset();
    }
  }
}
