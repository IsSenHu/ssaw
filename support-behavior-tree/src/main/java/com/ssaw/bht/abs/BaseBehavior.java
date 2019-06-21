package com.ssaw.bht.abs;

import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.infs.IBehaviour;
import lombok.Getter;
import lombok.Setter;

/**
 * @author HuSen
 * create on 2019/6/21 15:26
 */
@Getter
@Setter
public abstract class BaseBehavior implements IBehaviour {
    private EStatus status;
    BaseBehavior() {
        setStatus(EStatus.Initial);
    }

    @Override
    public EStatus tick() {
        // update方法被首次调用前执行OnInitlize方法，每次行为树更新时调用一次update方法
        if (status != EStatus.Running) {
            onInitialize();
        }
        status = update();
        // 当刚刚更新的行为不再运行时调用OnTerminate方法
        if (status != EStatus.Running) {
            onTerminate(status);
        }
        return status;
    }

    @Override
    public void reset() {
        setStatus(EStatus.Initial);
    }

    @Override
    public void release() {}

    @Override
    public void onInitialize() {}

    @Override
    public void onTerminate(EStatus status) {}

    @Override
    public void abort() {
        onTerminate(EStatus.Aborted);
        setStatus(EStatus.Aborted);
    }
}
