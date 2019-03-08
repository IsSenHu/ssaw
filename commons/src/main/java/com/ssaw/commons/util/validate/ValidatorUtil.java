package com.ssaw.commons.util.validate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 实体数据校验工具
 * @author HuSen
 * @date 2019/01/29
 */
public class ValidatorUtil {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private ValidatorUtil() {

    }

    /**
     * 实体数据校验
     * @param obj 实体
     * @return 校验错误信息
     */
    public static <T> Map<String, StringBuilder> validate(T obj) {
        Map<String, StringBuilder> errorMap = null;
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        StringBuilder sb;
        if (set != null && set.size() > 0) {
            errorMap = new HashMap<>(set.size());
            String property;
            for (ConstraintViolation<T> cv : set) {
                property = cv.getPropertyPath().toString();
                if (errorMap.get(property) != null) {
                    errorMap.get(property).append(",").append(cv.getMessage());
                } else {
                    sb = new StringBuilder();
                    sb.append(cv.getMessage());
                    errorMap.put(property, sb);
                }
            }
        }
        return errorMap;
    }
}
