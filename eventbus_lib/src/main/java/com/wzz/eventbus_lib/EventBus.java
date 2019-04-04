package com.wzz.eventbus_lib;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 代理类
 */
public class EventBus {

    // 清单 用来存储所有的订阅方法
    private Map< Object , List<MethodManager> > map ;

    //volatile修饰的变量不允许线程内部缓存以及重新排序，即直接修改内存
    private static volatile EventBus eventBus ;

    private Handler mHandler ;
    // 从主线程切换到子线程
    private ExecutorService mExecutorService;


    private EventBus(){
        map = new HashMap<>();
        mHandler = new Handler(Looper.getMainLooper());
        mExecutorService = Executors.newCachedThreadPool();
    }

    public static EventBus getDefault(){
        if ( eventBus == null ){
            synchronized (EventBus.class){
                if (eventBus == null){
                    eventBus = new EventBus();
                }
            }
        }
        return eventBus ;
    }

    // 转发消息 发布事件 发布消息
    public void reister(Object object){

        List<MethodManager> methodManagerList = map.get(object);

        if ( methodManagerList == null ){
            methodManagerList = findAnnotationMethod( object);
            map.put( object , methodManagerList );
        }
    }

    /**
     * 在activity中找符合订阅要求的方法
     * @param object
     * @return
     */
    private List<MethodManager> findAnnotationMethod(Object object) {

        List<MethodManager> methodList = new ArrayList<>();
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        /** 将所有带有注解的方法取出来，添加到集合 */
        for (Method method : methods) {

            Subscribe annotation = method.getAnnotation(Subscribe.class);
            if ( annotation == null ){
                continue;
            }

            //获取到这个方法接收的所有参数的类型
            Class<?>[] parameterTypes = method.getParameterTypes();
            MethodManager methodManager = new MethodManager( parameterTypes[0], annotation.threadMode(), method );
            methodList.add( methodManager ) ;

        }
        return methodList;
    }


    /**
     *  发送消息后，让所有注册者接收消息
     */
    public void post(final Object message ){
        // 通过清单，拿到所有的订阅方法
        Set<Object> objects = map.keySet();
        // 遍历每一个注册者中的方法
        for (final Object object : objects) {

            List<MethodManager> methodList = map.get( object );

            if ( methodList != null ){
                for ( final MethodManager method : methodList) {

                    // 判断发送的消息的类型接收类型一致的方法，如果相同再调用
                    // isAssignableFrom()方法是判断是否为某个类的父类  || 父类.class.isAssignableFrom(子类.class) ||  isAssignableFrom()方法的调用者和参数都是Class对象，调用者为父类，参数为本身或者其子类。
                    if ( method.getType().isAssignableFrom( message.getClass() )){

                        switch ( method.getThreadMode() ){

                            // 让接收和发送处在同一线程
                            case POSTING:
                                invoke( method , object , message);
                                break;

                            // 让接收处在主线程
                            case MAIN:
                                // 判断当前是否在主线程
                                if (Looper.myLooper() == Looper.getMainLooper() ){
                                    invoke( method , object , message);
                                }else {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            invoke( method , object , message);
                                        }
                                    });
                                }
                                break;


                            // 让接收处在子线程
                            case BACKGROUND:
                                if (Looper.myLooper() == Looper.getMainLooper() ){
                                    mExecutorService.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            invoke( method , object , message);
                                        }
                                    });
                                }else {
                                    invoke( method , object , message);
                                }
                                break;
                        }

                    }
                }

            }

        }

    }

    /**
     *
     * @param methodManager  要执行的方法
     * @param object 执行方法的对象
     * @param message 方法的参数，即发送的消息内容
     */
    private void invoke( MethodManager methodManager , Object object , Object message ){

        Method method = methodManager.getMethod();

        // 通过反射：调用方法
        try {
            method.invoke( object , message ) ;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 解除绑定
     * @param object
     */
    public void unregister(Object object){
        if ( object != null && map.get( object ) != null ){
            map.remove( object ) ;
        }
    }

}
