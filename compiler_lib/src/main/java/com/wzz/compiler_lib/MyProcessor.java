package com.wzz.compiler_lib;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;


//重要函数解说
//
//        init(ProcessingEnvironment env): 每一个注解处理器类都必须有一个空的构造函数。然而，这里有一个特殊的init()方法，它会被注解处理工具调用，并输入ProcessingEnviroment参数。ProcessingEnviroment提供很多有用的工具类Elements,Types和Filer。
//
//        public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env)这相当于每个处理器的主函数main()。在这里写扫描、评估和处理注解的代码，以及生成Java文件。输入参数RoundEnviroment，可以让查询出包含特定注解的被注解元素。
//
//        getSupportedAnnotationTypes();这里必须指定，这个注解处理器是注册给哪个注解的。注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称。换句话说，在这里定义你的注解处理器注册到哪些注解上。 
//
//        getSupportedSourceVersion();用来指定你使用的Java版本。
//
//        这个类就是编译期注解编译器会根据它来创建Java文件，里面提示的几个必须加上的地方要注意，忘记了就创建不了文件。
//        ---------------------
//        作者：Alex老夫子
//        来源：CSDN
//        原文：https://blog.csdn.net/msn465780/article/details/78888668
//        版权声明：本文为博主原创文章，转载请附上博文链接！

/**
 * @AutoService(Processor.class)
 * 这个注解不要忘了，否则无法生成Java文件 (此类无需主动调用，加上@AutoService即可)
 */
@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor{

    /**
     * 文件相关的辅助类
     */
    private Filer mFiler;
    /**
     * 元素相关的辅助类
     */
    private Elements mElementUtils;
    /**
     * 日志相关的辅助类
     */
    private Messager mMessager;

    /**
     * 每一个注解处理器类都必须有一个空的构造函数。
     * 然而，这里有一个特殊的init()方法，它会被注解处理工具调用，
     * 并输入ProcessingEnviroment参数。
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        // 1、不使用三方工具类，利用原始的方法进行文件写入：
        Writer writer = null;
        try {
            //创建了一个java文件
            JavaFileObject javaFileObject = mFiler.createSourceFile("com.wzz.test"+"."+"TestDemo");
            writer = javaFileObject.openWriter();
            //第一行写包名
            writer.write("package "+"xixi"+";\n");

//            writer.write("}\n}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // -----------------------------------------------------------------

        // 2、利用com.squareup:javapoet三方库来生成java文件

        // 类名和包名
        TypeSpec finderClass = TypeSpec.classBuilder("MyGeneratedClass")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.constructorBuilder().build()) // 构造方法
//                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.INJECTOR, TypeName.get(mClassElement.asType())))
//                .addMethod(methodBuilder.build())
                .build();

        // 创建Java文件
        JavaFile javaFile = JavaFile.builder("com.example.annotation_processor", finderClass)
                .build();

        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }



        return true;
    }

    /**这个方法必须重写，否则无法生成Java文件
     * 这里必须指定，这个注解处理器是注册给哪个注解的。
     * 注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称。
     * 换句话说，在这里定义你的注解处理器注册到哪些注解上。
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Override.class.getCanonicalName());
//        types.add(OnClick.class.getCanonicalName());
        return types;
    }
}



//此为JavaLib库
//        使用：
//        1、在本lib的build中添加
//        implementation 'com.google.auto.service:auto-service:1.0-rc3'
//        compile 'com.squareup:javapoet:1.7.0'  // 用于生成Java文件的库
//
//        2、在appModule下添加：annotationProcessor project(':compiler_lib')
//
//        3、创建一个类：这个类就是编译期注解编译器会根据它来创建Java文件
//        MyProcessor extends AbstractProcessor{}  ---> [并添加 @AutoService(Processor.class)  这个注解不要忘了，否则无法生成Java文件]
//
//        4、rebuild
