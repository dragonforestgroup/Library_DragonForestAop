package com.dragonforest.android.library.aop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dragonforest.android.library.aop.annotation.Logger;
import com.dragonforest.android.library.aop.annotation.NullParamChecker;
import com.dragonforest.android.library.aop.annotation.ThreadController;
import com.dragonforest.android.library.aop.model.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Logger(Log.DEBUG)
    private String testLog(String a,int b){
        Log.e(getClass().getSimpleName(),"执行testLog()，传入：a:"+a+",b:"+b);
        return a+b;
    }

    @ThreadController(threadMode = ThreadMode.MODE_ASYNC)
    @Logger(Log.INFO)
    @NullParamChecker
    private void testThread(String a,int b){
        Log.e(getClass().getSimpleName(),"执行testThread()，传入：a:"+a+",b:"+b+"，线程："+Thread.currentThread().getName());
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_testlog:
                testLog("hanlonglinliong",123);
                break;
            case R.id.btn_testthread:
                testThread(null,456);
                break;
            default:
                break;
        }
    }
}
