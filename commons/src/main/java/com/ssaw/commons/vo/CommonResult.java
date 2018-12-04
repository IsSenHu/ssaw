package com.ssaw.commons.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen.
 * @date 2018/11/27 19:27.
 */
@Data
public class CommonResult<T> implements Serializable {
    /**
     * 状态码
     */
    private int code;
    /**
     * 消息
     */
    private String message;
    /**
     * 数据
     */
    private T data;
    /**
     * 时间戳
     */
    private long ts;

    /**
     * 创建CommonResult
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     * @param <T> 数据类型
     * @return CommonResult
     */
    public static <T> CommonResult<T> createResult(Integer code, String message, T data) {
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.setCode(code);
        commonResult.setMessage(message);
        commonResult.setData(data);
        commonResult.setTs(System.currentTimeMillis());
        return commonResult;
    }
}
