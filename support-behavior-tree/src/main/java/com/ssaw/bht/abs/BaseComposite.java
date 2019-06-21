package com.ssaw.bht.abs;

import com.ssaw.bht.infs.IBehaviour;
import com.ssaw.bht.infs.IComposite;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HuSen
 * create on 2019/6/21 15:37
 */
@Getter
@Setter
public abstract class BaseComposite extends BaseBehavior implements IComposite {
    private List<IBehaviour> children = new ArrayList<>();

    @Override
    public void addChild(IBehaviour child) {
        children.add(child);
    }

    @Override
    public void removeChild(IBehaviour child) {
        children.remove(child);
    }

    @Override
    public void clearChild() {
        children.clear();
    }
}
