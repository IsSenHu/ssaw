package com.ssaw.rocketmq.annotation;

import java.lang.annotation.*;

/**
 * 消息的键
 *
 * @author HuSen
 * @date 2019/3/28 9:41
 */
@Documented
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageKey {
}
