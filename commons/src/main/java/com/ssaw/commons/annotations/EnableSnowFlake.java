package com.ssaw.commons.annotations;

import com.ssaw.commons.config.EnableSnowFlakeAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @date 2019/4/18 17:34
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Inherited
@Import(EnableSnowFlakeAutoConfiguration.class)
public @interface EnableSnowFlake {
}
