package com.ssaw.rocketmq.annotation;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @date 2019/3/28 9:41
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableRocketMq {}
