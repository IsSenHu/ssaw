package com.ssaw.bht.abs;

import com.ssaw.bht.infs.IBehaviour;
import com.ssaw.bht.infs.IDecorator;

/**
 * @author HuSen
 * create on 2019/6/21 15:42
 */
public abstract class BaseDecorator extends BaseBehavior implements IDecorator {
    protected IBehaviour child;

    @Override
    public void addChild(IBehaviour child) {
        this.child = child;
    }
}
