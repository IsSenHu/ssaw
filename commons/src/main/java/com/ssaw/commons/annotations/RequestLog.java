package com.ssaw.commons.annotations;

import java.lang.annotation.*;

/**
 * 自定义注解，实现自动打印请求参数
 * @author HuSen.
 * @date 2018/11/28 17:23.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequestLog {
    /** 方法描述 */
    String desc() default "";
}
