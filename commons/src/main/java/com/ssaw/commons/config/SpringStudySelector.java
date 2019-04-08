package com.ssaw.commons.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * @author HuSen
 * @date 2019/4/8 14:07
 */
public class SpringStudySelector implements ImportSelector, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public @NonNull String[] selectImports (@NonNull AnnotationMetadata annotationMetadata) {
        annotationMetadata.getAnnotationTypes().forEach(System.out::println);
        System.out.println(beanFactory);
        return new String[]{AppConfig.class.getName()};
    }
}