package com.ssaw.commons.exceptions;

import java.util.Map;

/**
 * 参数异常
 * @author HuSen.
 * @date 2018/11/28 16:32.
 */
public class ParamException extends RuntimeException {
    private Map<String, StringBuilder> errors;

    public ParamException(Map<String, StringBuilder> errors) {
        this.errors = errors;
    }

    public Map<String, StringBuilder> getErrors() {
        return errors;
    }
}
