package com.ssaw.zookeeper.lock;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author HuSen
 * @date 2019/4/11 10:06
 */
@Data
@AllArgsConstructor
public class State {
    private boolean locked;
    private String nodeId;
}