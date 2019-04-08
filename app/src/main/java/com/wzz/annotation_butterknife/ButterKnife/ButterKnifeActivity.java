package com.wzz.annotation_butterknife.ButterKnife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wzz.annotation_butterknife.R;
import com.wzz.butterknife_annotation_lib.BindView;


/**
 * 手写EventBus框架的测试Activity
 * EventBus原理： 反射 + 注解
 */
public class ButterKnifeActivity extends AppCompatActivity {

    // 使用自定义的ButterKnife框架
    @BindView(R.id.btn)
    public Button bt ;

    @BindView(R.id.btn2)
    public Button bt2 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_butterknife);

        MyButterKnife.bind( this );

    }

    public void click1(View view){
        bt.setText("手写ButterKnife测试1");
    }

    public void click2(View view){
        bt2.setText("手写ButterKnife测试2");
    }

}
