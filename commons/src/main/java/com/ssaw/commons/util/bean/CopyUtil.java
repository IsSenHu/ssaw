package com.ssaw.commons.util.bean;

import com.google.common.collect.Table;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @author HuSen
 * @date 2019/2/28 17:17
 */
@SuppressWarnings("ALL")
public class CopyUtil {
    private CopyUtil() {
    }

    /**
     * 复制source的属性到target的同名同类型属性上
     */
    public static <T> T copyProperties(Object source, T target) {
        return copyProperties(source, target, false);
    }

    /**
     * 复制source的属性到target的同名同类型属性上
     *
     * @param ignoreProperties 不需要复制的属性名
     */
    public static <T> T copyProperties(Object source, T target, String... ignoreProperties) {
        return copyProperties(source, target, false, ignoreProperties);
    }

    /**
     * 复制source的非null属性到target的同名同类型属性上
     */
    public static <T> T copyNotNullProperies(Object source, T target) {
        return copyProperties(source, target, true);
    }

    /**
     * 复制source的非null属性到target的同名同类型属性上
     * @param source 源
     * @param target 目标
     * @param ignoreProperties 忽略的属性名
     * @param <T> 目标类型
     * @return 处理后的 target
     */
    public static <T> T copyNotNullProperties(Object source, T target, String... ignoreProperties) {
        return copyProperties(source, target, true, ignoreProperties);
    }

    private static <T> T copyProperties(Object source, T target, boolean ignoreNullValue, String... ignoreProperties) {
        return copyProperties(source, target, ignoreNullValue, null, ignoreProperties);
    }


    /**
     * java.lang.String...)} 删除了真实类的参数 添加一个判断:是否复制null值 返回复制后的target,这是为了简化lambda写法
     *
     * @param ignoreNullValue 是否忽略null值复制,true表示忽略source中的null值属性
     * @param converterTable 属性转换表,为null的情况下不使用
     * @param ignoreProperties 要忽略的属性,这些属性不会被写入到目标对象
     */
    private static <T> T copyProperties(Object source,
                                        T target,
                                        boolean ignoreNullValue,
                                        Table<Class, Class, Converter> converterTable,
                                        String... ignoreProperties) {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();

        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : Collections.emptyList());
        for (PropertyDescriptor targetPd : targetPds) {
            if (ignoreList.contains(targetPd.getName())) {
                continue;
            }
            Method writeMethod = resolveBridgeMethod(targetPd.getWriteMethod());
            if (writeMethod == null) {
                continue;
            }
            PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
            if (sourcePd == null) {
                continue;
            }
            Method readMethod = resolveBridgeMethod(sourcePd.getReadMethod());
            if (readMethod == null) {
                continue;
            }
            Class sourcePropertyType = readMethod.getReturnType();
            Class targetPropertyType = writeMethod.getParameterTypes()[0];
            boolean useTable = converterTable != null && !converterTable.isEmpty();
            //如果需要使用转换表,并且对应类型的转换表存在,则
            if (ClassUtils.isAssignable(targetPropertyType, sourcePropertyType)) {
                try {
                    Object value = readMethod.invoke(source);
                    // 新逻辑
                    if (value == null && ignoreNullValue) {
                        continue;
                    }
                    writeMethod.invoke(target, value);
                } catch (Throwable ex) {
                    throw new FatalBeanException(
                            "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                }
            } else if (useTable && converterTable.contains(sourcePropertyType, targetPropertyType)) {
                try {
                    Object value = readMethod.invoke(source);
                    // 新逻辑
                    if (value == null && ignoreNullValue) {
                        continue;
                    }
                    Converter converter = converterTable.get(sourcePropertyType, targetPropertyType);
                    // noinspection unchecked
                    Object destValue = converter.convert(value);
                    writeMethod.invoke(target, destValue);
                } catch (Throwable ex) {
                    throw new FatalBeanException(
                            "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                }
            }
        }
        return target;
    }

    private static WeakHashMap<Method, Method> bridgeMethodCache = new WeakHashMap<>();

    /**
     * 用于解决泛型属性取到的属性类型有误的问题
     */
    private static Method resolveBridgeMethod(Method sourceMethod) {
        if (sourceMethod == null) {
            return null;
        }
        if (sourceMethod.isBridge()) {
            return bridgeMethodCache.computeIfAbsent(sourceMethod, k -> BridgeMethodResolver.findBridgedMethod(sourceMethod));
        }
        return sourceMethod;
    }
}