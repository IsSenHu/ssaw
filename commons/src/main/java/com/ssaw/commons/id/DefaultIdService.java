package com.ssaw.commons.id;

/**
 * @author HuSen
 * @date 2019/4/18 17:37
 */
public class DefaultIdService {

    private SnowFlake snowFlake;

    public DefaultIdService(SnowFlake snowFlake) {
        this.snowFlake = snowFlake;
    }

    public Long genId() {
        return snowFlake.nextId();
    }
}