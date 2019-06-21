package com.ssaw.bht.abs;

import com.ssaw.bht.infs.ICondition;
import lombok.Setter;

/**
 * @author HuSen
 * create on 2019/6/21 15:39
 */
@Setter
public abstract class BaseCondition extends BaseBehavior implements ICondition {
    private boolean negation;

    @Override
    public boolean isNegation() {
        return negation;
    }

    protected int getRandom() {
        double random = Math.random() * 100;
        return (int) random;
    }
}
