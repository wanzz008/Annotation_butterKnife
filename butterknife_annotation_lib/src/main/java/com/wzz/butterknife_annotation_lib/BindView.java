package com.wzz.butterknife_annotation_lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//使用注解最主要的部分在于对注解的处理,那么就会涉及到注解处理器.注解处理器就是通过反射机制获取被检查方法上的注解信息,然后根据注解元素的值进行特定的处理.

@Target(ElementType.FIELD) // 注解的作用域是成员变量
@Retention(RetentionPolicy.SOURCE) // 声明注解是在编译的时候被注解处理器处理  也就是说它的生命到编译完之后就结束
public @interface BindView {

    int value();

}
