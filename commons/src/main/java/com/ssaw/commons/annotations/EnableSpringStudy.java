package com.ssaw.commons.annotations;

import com.ssaw.commons.config.SpringStudySelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @date 2019/4/8 14:06
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Inherited
@Import(SpringStudySelector.class)
public @interface EnableSpringStudy {
}
