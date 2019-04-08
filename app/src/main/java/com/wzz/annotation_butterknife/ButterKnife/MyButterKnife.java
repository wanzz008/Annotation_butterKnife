package com.wzz.annotation_butterknife.ButterKnife;

import android.app.Activity;

public class MyButterKnife {

    public static void bind(Activity activity){

        //得到每个activity相对应的生成的那个文件的名字
        String name = activity.getClass().getName() + "$ViewBinder";
        try {
            Class<?> clazz = Class.forName(name);
            ViewBinder viewBinder = (ViewBinder) clazz.newInstance();
            viewBinder.bind( activity );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
