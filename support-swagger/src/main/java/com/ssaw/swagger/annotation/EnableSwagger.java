package com.ssaw.swagger.annotation;

import com.ssaw.swagger.config.Swagger2;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

/**
 * @author HuSen
 * @date 2019/2/22 16:11
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
@Documented
@Inherited
@Import({Swagger2.class})
public @interface EnableSwagger { }
