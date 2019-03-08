package com.ssaw.commons.util.poi;

import java.lang.annotation.*;

/**
 * Created by HuSen on 2018/11/9 9:46.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Documented
// 可以继承
@Inherited
public @interface ExcelColumn {
    String value() default "";
    String format() default "";
}