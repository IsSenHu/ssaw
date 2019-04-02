package com.ssaw.rocketmq.annotation;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @date 2019/4/2 12:41
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RocketMqProducerComponentScan {

    /**
     * 扫包类
     * */
    Class basicPackagesClass() default void.class;

    /**
     * 扫包路径
     * */
    String basicPackages() default "";
}
