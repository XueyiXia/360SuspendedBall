package com.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private UiCustom mUiCustom;

    private Button mBtnStart;

    private int i = 0;


    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(i<=100){
                mUiCustom.setProgress(i);//延时1秒改变加速球进度
                sendEmptyMessageDelayed(1,1000);
                i+=10;
            }
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidget();
    }

    private void initWidget(){
        mUiCustom=(UiCustom)super.findViewById(R.id.viewAccBall);
        mBtnStart=(Button)super.findViewById(R.id.btnCustomAccBall);
        mBtnStart.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCustomAccBall:
                i = 0;
                mHandler.sendEmptyMessageDelayed(1,1000);//开启动画
                break;
        }
    }
}
