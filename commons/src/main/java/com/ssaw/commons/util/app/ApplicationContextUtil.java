package com.ssaw.commons.util.app;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * @author HuSen.
 * @date 2019/1/7 14:12.
 */
public class ApplicationContextUtil {

    private static ApplicationContext context;

    public static void setContext(ApplicationContext context) {
        ApplicationContextUtil.context = context;
    }

    /**
     * 根据类型获取Bean
     * @param tClass 类型
     * @param <T> 泛型T
     * @return 指定类型Bean
     * @throws BeansException BeansException
     */
    public static <T> T getBean(Class<T> tClass) throws BeansException {
        return context.getBean(tClass);
    }
}
