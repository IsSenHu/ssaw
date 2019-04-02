package com.ssaw.rocketmq.proxy;

import com.ssaw.rocketmq.annotation.EnableRocketMq;
import com.ssaw.rocketmq.annotation.RocketMqProducerComponentScan;
import com.ssaw.rocketmq.util.RocketMqUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author HuSen
 * @date 2019/4/2 10:59
 */
@Slf4j
@Configuration
@ConditionalOnBean(annotation = EnableRocketMq.class)
public class RocketMqBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        Map<String, Object> configWithAnnotation = applicationContext.getBeansWithAnnotation(RocketMqProducerComponentScan.class);
        if (configWithAnnotation.size() == 0) {
            return;
        }
        Object o = configWithAnnotation.values().iterator().next();
        RocketMqProducerComponentScan componentScan = AnnotationUtils.findAnnotation(o.getClass(), RocketMqProducerComponentScan.class);
        if (Objects.isNull(componentScan)) {
            return;
        }
        String basicPackages = componentScan.basicPackages();
        Class basicPackagesClass = componentScan.basicPackagesClass();
        if (StringUtils.isBlank(basicPackages) && basicPackagesClass == void.class) {
            return;
        }
        List<Class<?>> producerClasses = new ArrayList<>();
        String packageName = StringUtils.isNotBlank(basicPackages) ? basicPackages : basicPackagesClass.getPackage().getName();
        if (StringUtils.isNotBlank(packageName)) {
            ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
            CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourceLoader);
            try {
                Resource[] resources = resolver.getResources("classpath*:".concat(packageName.replace(".", "/").concat("/**/*.class")));
                for (Resource resource : resources) {
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    String className = reader.getClassMetadata().getClassName();
                    if (basicPackagesClass != void.class && basicPackagesClass.getName().equals(className)) {
                        continue;
                    }
                    Class<?> forName = Class.forName(className);
                    log.info("find rocket mq producer:{}", forName);
                    producerClasses.add(forName);
                }
            } catch (Exception e) {
                log.error("load rocket mq producer fail:", e);
            }
        }
        if (CollectionUtils.isEmpty(producerClasses)) {
            return;
        }
        for (Class<?> producerClass : producerClasses) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(producerClass);
            GenericBeanDefinition definition = (GenericBeanDefinition) beanDefinitionBuilder.getRawBeanDefinition();
            definition.getPropertyValues().add("interfaceClass", definition.getBeanClassName());
            definition.setBeanClass(RocketMqProxyFactory.class);
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            beanDefinitionRegistry.registerBeanDefinition(registryName(producerClass.getSimpleName()), definition);
        }
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {}

    private static String registryName(String name) {
        return name.substring(0, 1).toLowerCase().concat(name.substring(1));
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        RocketMqUtil.setApplicationContext(applicationContext);
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}