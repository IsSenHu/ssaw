package com.ssaw.commons.exceptions;

import org.springframework.validation.FieldError;
import java.util.List;

/**
 * 参数异常
 * @author HuSen.
 * @date 2018/11/28 16:32.
 */
public class ParamException extends RuntimeException {
    private List<FieldError> errors;

    public ParamException(List<FieldError> errors) {
        this.errors = errors;
    }

    public ParamException(String message, List<FieldError> errors) {
        super(message);
        this.errors = errors;
    }

    public List<FieldError> getErrors() {
        return errors;
    }
}
