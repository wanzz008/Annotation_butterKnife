package com.wzz.annotation_butterknife.basic;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * https://blog.csdn.net/weixin_39919527/article/details/80383292
 *
 *自定义注解  使用@interface关键字定义的一个注解。
 * ①.成员类型是受限制的，合法的类型包括基本的数据类型以及String，Class，Annotation,Enumeration等。
 * ②.如果注解只有一个成员，则成员名必须取名为value()，在使用时可以忽略成员名和赋值号（=）。
 * ③.注解类可以没有成员，没有成员的注解称为标识注解。
 */
/**
 * @Target:表示该注解用于什么位置,可选的参数是ElementType枚举中的成员:
 * @Retention:表示需要在什么级别保存该注解信息(生命周期),可选的参数是RetentionPolicy枚举中的成员(注意,只有声明为RUNTIME,才可以通过反射机制读取注解的信息.)
 *
 */

// 元注解（meta-annotation，注解的注解），如@Target和@Retention

@Target({ElementType.METHOD,ElementType.TYPE})  // @Target是这个注解的作用域 这个写的是作用在类和方法上

@Retention(RetentionPolicy.RUNTIME) // @Retention是它的生命周期:SOURCE（只在源码显示，编译时丢弃）,CLASS（编译时记录到class中，运行时忽略）,RUNTIME（运行时存在，可以通过反射读取）

@Inherited  //@Inherited是一个标识性的元注解，它允许子注解继承它。

@Documented // @Documented，生成javadoc时会包含注解。

public @interface Description { // 此注解有三个元素

    // 有默认值的元素可以选择性赋值.如果注解只有一个元素且该元素的名称是value的话,在使用注解的时候可以省略"value="直接写需要的值即可

    String desc();

    String author();

    int age() default 18;

}