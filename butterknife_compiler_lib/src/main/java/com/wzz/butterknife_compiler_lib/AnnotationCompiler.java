package com.wzz.butterknife_compiler_lib;

import com.google.auto.service.AutoService;
import com.wzz.butterknife_annotation_lib.BindView;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

/**
 * 替我们写findviewbyid的代码
 * 此类需要你调用
 * 你继承了注解处理器的父类   以及使用了AutoService这个注解的话  虚拟机会自动获取到这个类 然后去调用process这个方法
 */
//使用注解最主要的部分在于对注解的处理,那么就会涉及到注解处理器.注解处理器就是通过反射机制获取被检查方法上的注解信息,然后根据注解元素的值进行特定的处理.
// 注解处理器 需要继承
@AutoService(Processor.class) //  2、注册注解处理器（先添加依赖包） // 3、在工程build中添加classpath
public class AnnotationCompiler extends AbstractProcessor {

    //    package com.dongnao.molenknife;
    //    import com.dongnao.molenknife.ViewBinder;
    //    public class MainActivity$ViewBinder implements ViewBinder<com.dongnao.molenknife.MainActivity>{
    //        public void bind(com.dongnao.molenknife.MainActivity target){
    //            target.textView =(android.widget.TextView)target.findViewById(2131165320);
    //            target.bt_1 =(android.widget.Button)target.findViewById(2131165218);
    //        }
    //    }

    Filer filter ;

    /**
     * 初始化文件生成对象
     * @param processingEnv
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filter = processingEnv.getFiler() ;
    }

    /** 声明我们的注解要处理的注解 */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BindView.class.getCanonicalName());
        return types;
    }

    /** 声明我们的注解处理器支持的源版本 */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    /**
     * 替我们写代码的方法 【核心方法】
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        //        public class className{      TypeElement
        //        private int aaa;         VariableElement
        //        private int nbb;         VariableElement
        //        private void method(){}  ExecutableElement
        //        public void method2(){}  ExecutableElement

        //从源文件中   拿到所有的使用了BindView注解的节点
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindView.class);
        //用来结构化数据
        Map<String, List<VariableElement>> map  = new HashMap<>();
        //遍历所有的属性节点  也就是控件
        for (Element element : elements) {
            VariableElement variableElement = (VariableElement) element;
            //获取到类名
            String activityName = getActivityName(variableElement);
            List<VariableElement> list = map.get(activityName);
            if(list ==null){
                list = new ArrayList<>();
                map.put(activityName,list);
            }
            list.add(variableElement);
        }

        //开始写文件
        Iterator<String> iterator = map.keySet().iterator();
        //遍历iterator 通过key去获取map中的值
        while (iterator.hasNext()){
            //获取到类名
            String activityName = iterator.next();
            //通过类名获取到集合
            List<VariableElement> list = map.get(activityName);
            //获取包名
            String packageName = getPackageName(list.get(0));
            //创建一个新名字
            String newActivityName = activityName+"$ViewBinder";
            Writer writer = null;
            try {
                //创建了一个java文件
                JavaFileObject javaFileObject = filter.createSourceFile(packageName+"."+newActivityName);
                writer = javaFileObject.openWriter();
                //第一行写包名
                writer.write("package "+packageName+";\n");
                //第二行  导入类
                writer.write("import "+packageName+".ViewBinder;\n");
                //第三行  开始写定义类的语句
                writer.write("public class "+newActivityName+" implements ViewBinder<"+packageName+"."+activityName+">{\n");
                //开始写第四行  方法
                writer.write("public void bind("+packageName+"."+activityName+" target){\n");
                //写所用的findViewById
                for (VariableElement variableElement : list) {
                    //拿到节点的类型（控件）
                    TypeMirror typeMirror = variableElement.asType();
                    //拿到节点的名字（控件）
                    String variableName = variableElement.getSimpleName().toString();
                    //拿到节点的ID（控件）
                    BindView bindView = variableElement.getAnnotation(BindView.class);
                    int id = bindView.value();
                    writer.write("target."+variableName+" =("+typeMirror+")target.findViewById("+id+");\n");
                }
                writer.write("}\n}\n");

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
        }



        return false;
    }

    /**
     * 通过控件节点获取到类名
     */
    public String getActivityName(VariableElement variableElement){
        // 获取到它的上一个节点，也就是成员变量的上个节点 ，也就是类节点
        TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
        // 通过类节点 获取到类名
        String activityname = typeElement.getSimpleName().toString();
        return activityname;
    }

    /**
     * 获取包名
     * @param variableElement
     * @return
     */
    public String getPackageName(VariableElement variableElement){
        TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
        String name = processingEnv.getElementUtils().getPackageOf(typeElement).toString();
        return name ;
    }
}
