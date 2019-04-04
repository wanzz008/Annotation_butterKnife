package com.wzz.annotation_butterknife.basic;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 1、boolean isExist = c.isAnnotationPresent(Description.class);
 * 判断类或方法上是否有此注解
 * 2、Description d  = c.getAnnotation(Description.class);
 * 拿到注解实例，解析类上面的注解
 */
public class AnnotationTest {

    public static void main(String[] args) throws ClassNotFoundException {

        parseTypeAnnotation();
        parseTypeAnnotation1();
        parseMethodAnnotation(Child.class);
        parseConstructAnnotation(Child.class);

    }

    /**
     * 解析类名上的一个注解
     *
     * @throws ClassNotFoundException
     */
    public static void parseTypeAnnotation() throws ClassNotFoundException {

        // 使用类加载器加载类
        Class<?> clz = Class.forName("com.wzz.annotation_butterknife.basic.Child");

        // 找到类上面的注解 来判断这个类是否存在Description这样的一个注解
        boolean isExist = clz.isAnnotationPresent(Description.class);

        if (isExist) {
            // 拿到注解实例，解析类上面的注解
            Description annotation = clz.getAnnotation(Description.class);

            if (annotation != null) {
                System.out.println("desc =" + annotation.desc() + "  , author=" + annotation.author() + " , age=" + annotation.age());
            }
        }
        System.out.println("----------------------");
    }

    /**
     * 解析类名上的所有注解
     * 通过找到所有注解的方法
     *
     * @throws ClassNotFoundException
     */
    public static void parseTypeAnnotation1() throws ClassNotFoundException {

        Class clazz = Class.forName("com.wzz.annotation_butterknife.basic.Child");
        // 获取此类上的所有注解
        Annotation[] annotations = clazz.getAnnotations();

        for (Annotation annotation : annotations) {

            if (annotation instanceof Description) {

                Description description = (Description) annotation;

                System.out.println("className = " + clazz.getName() + " , desc =" + description.desc() + "  , author=" + description.author() + " , age=" + description.age());

            }
        }

        System.out.println("----------------------");


    }


    /**
     * 解析所有方法上的注解
     */
    public static void parseMethodAnnotation(Class clz) {

        //1、获取所有的方法
        Method[] methods = clz.getDeclaredMethods();

        for (Method method : methods) {

            boolean hasAnnotation = method.isAnnotationPresent(Description.class);

            if (hasAnnotation) {

                Description annotation = method.getAnnotation(Description.class);
                System.out.println("methodName = " + method.getName() + " , desc =" + annotation.desc() + "  , author=" + annotation.author() + " , age=" + annotation.age());
            }
        }

        //2、另一种解析方法 (选其中之一即可)
        System.out.println("----------------------");
        for (Method m : methods) {
            //拿到方法上的所有的注解
            Annotation[] as = m.getAnnotations();
            for (Annotation a : as) {
                //用二元操作符判断a是否是Description的实例
                if (a instanceof Description) {
                    Description d = (Description) a;
                    System.out.println(d.desc());
                }

            }

        }
        System.out.println("----------------------");
    }

    /**
     * 解析构造方法上的注解
     */
    public static void parseConstructAnnotation(Class clz) {


        Constructor[] constructors = clz.getConstructors();

        for (Constructor constructor : constructors) {

            System.out.println("constructorName = " + constructor.getName());


            boolean hasAnnotation = constructor.isAnnotationPresent(Description.class);

            if (hasAnnotation) {

                Description annotation = (Description) constructor.getAnnotation(Description.class);

                System.out.println("constructorName = " + constructor.getName() + " , desc =" + annotation.desc() + "  , author=" + annotation.author() + " , age=" + annotation.age());

            }
        }
    }


}
