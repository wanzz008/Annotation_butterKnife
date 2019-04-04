package com.wzz.eventbus_lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //声明注解的作用域 也就是放在什么之上
@Retention(RetentionPolicy.RUNTIME) // 注解的生命周期,运行时处理
public @interface Subscribe {

    ThreadMode threadMode() default  ThreadMode.POSTING;

}
