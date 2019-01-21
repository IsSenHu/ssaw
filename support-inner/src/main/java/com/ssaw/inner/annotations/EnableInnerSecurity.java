package com.ssaw.inner.annotations;

import com.ssaw.inner.config.InnerSecurityConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author HuSen.
 * @date 2019/1/7 16:53.
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({InnerSecurityConfig.class})
public @interface EnableInnerSecurity {}
