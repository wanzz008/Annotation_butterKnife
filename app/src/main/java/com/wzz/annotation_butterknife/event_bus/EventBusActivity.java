package com.wzz.annotation_butterknife.event_bus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wzz.annotation_butterknife.R;
import com.wzz.eventbus_lib.EventBus;
import com.wzz.eventbus_lib.Subscribe;
import com.wzz.eventbus_lib.ThreadMode;

/**
 * 手写EventBus框架的测试Activity
 * EventBus原理： 反射 + 注解
 */
public class EventBusActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbus);

        EventBus.getDefault().reister( this );
    }

    /**
     * 订阅者方法  带有注解的此方法，通过EventBus里的反射进行调用，如果发送和接收类型不对，会在invoke()方法里抛出异常
     */
    @Subscribe( threadMode = ThreadMode.MAIN )
    public void getMessage( String message ){

        Log.d("wzz------", "getMessage: " + Thread.currentThread().getName() );
        Toast.makeText(this, message , Toast.LENGTH_SHORT ).show();

    }

    /**
     * 通过EventBus.getDefault().post(" ")发送的消息，会遍历所有的此类中的带有注解的方法，并通过反射调用
     * @param view
     */
    public void send(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("wzz------", "post: " + Thread.currentThread().getName() );
                EventBus.getDefault().post("我是EventBus");
            }
        }).start();

    }

    /**
     * 解除绑定
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister( this );
    }

}
