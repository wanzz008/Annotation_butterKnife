package com.wzz.eventbus_lib;

import java.lang.reflect.Method;

/***
 * 对方法的封装对象 用来封装符合我们订阅要求的方法
 */
public class MethodManager {

    // 方法的传入类型
    private Class<?> type ;
    //方法上的注解参数
    private ThreadMode mThreadMode;
    //方法本身
    private Method mMethod ;

    public MethodManager(Class<?> type, ThreadMode threadMode, Method method) {
        this.type = type;
        mThreadMode = threadMode;
        mMethod = method;
    }



    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public ThreadMode getThreadMode() {
        return mThreadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        mThreadMode = threadMode;
    }

    public Method getMethod() {
        return mMethod;
    }

    public void setMethod(Method method) {
        mMethod = method;
    }
}
