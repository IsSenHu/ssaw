package com.ssaw.netty.echo.room.vo;

import lombok.Data;

import java.util.Set;

/**
 * @author HuSen
 * create on 2019/6/25 10:57
 */
@Data
public class UserVO {
    private String username;
    private Set<String> friends;
}
