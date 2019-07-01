package com.ssaw.gateway.http.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2019/7/1 15:39
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Resp<T> implements Serializable {
    private static final long serialVersionUID = 83313529921574929L;

    private Integer code;
    private String message;
    private Long time;
    private T data;

    public static <T> Resp<T> of(Integer code, String message, Long time, T data) {
        return new Resp<>(code, message, time, data);
    }
}
