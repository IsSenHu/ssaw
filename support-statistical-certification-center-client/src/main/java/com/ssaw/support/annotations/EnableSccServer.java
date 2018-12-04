package com.ssaw.support.annotations;

import com.ssaw.support.properties.StatisticalCertificationCenterClientProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 将当前服务变为鉴权中心要使用的资源服务
 * @author HuSen.
 * @date 2018/11/28 19:06.
 */
/*
 * Target说明了Annotation所修饰的对象范围:
 * Annotation可被用于 packages、types（类、接口、枚举、Annotation类型）.
 * 类型成员（方法、构造方法、成员变量、枚举值）.
 * 方法参数和本地变量（如循环变量、catch参数）.
 * 1.CONSTRUCTOR:用于描述构造器.
 * 2.FIELD:用于描述域.
 * 3.LOCAL_VARIABLE:用于描述局部变量.
 * 4.METHOD:用于描述方法.
 * 5.PACKAGE:用于描述包.
 * 6.PARAMETER:用于描述参数.
 * 7.TYPE:用于描述类、接口(包括注解类型) 或enum声明！
 * */
@Target(ElementType.TYPE)
/*
 * Retention保留 该注解会被保留到哪个阶段
 * 1.RetentionPolicy.SOURCE —— 这种类型的Annotations只在源代码级别保留,编译时就会被忽略.
 * 2.RetentionPolicy.CLASS —— 这种类型的Annotations编译时被保留,在class文件中存在,但JVM将会忽略.
 * 3.RetentionPolicy.RUNTIME —— 这种类型的Annotations将被JVM保留,所以他们能在运行时被JVM或其他使用反射机制的代码所读取和使用.
 */
@Retention(RetentionPolicy.RUNTIME)
/*
 * Documented 注解表明这个注解应该被 javadoc工具记录. 默认情况下,javadoc是不包括注解的.
 * 但如果声明注解时指定了 @Documented,则它会被 javadoc 之类的工具处理, 所以注解类型信息也会被包括在生成的文档中.
 * */
@Documented
/*
 * 加了该注解的类，子类会继承该注解
 * */
@Inherited
@Import(StatisticalCertificationCenterClientProperties.class)
public @interface EnableSccServer {
}
