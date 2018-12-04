package com.ssaw.commons.annotations;

import java.lang.annotation.*;

/**
 * 自定义注解，如果参数异常自动抛出参数异常
 * @author HuSen.
 * @date 2018/11/28 17:25.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Validating {
}
