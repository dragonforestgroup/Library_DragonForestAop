package com.dragonforest.android.library.aop;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dragonforest.android.library.aop.annotation.Logger;
import com.dragonforest.android.library.aop.annotation.NullParamChecker;
import com.dragonforest.android.library.aop.annotation.ThreadController;
import com.dragonforest.android.library.aop.annotation.permission.NeedPermission;
import com.dragonforest.android.library.aop.annotation.permission.PermissionDenied;
import com.dragonforest.android.library.aop.model.ThreadMode;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Logger(Log.ERROR)
    public String testLog(int a, int b) {
        Log.e("TAG", "点击testLog");
        return "nihao" + a + b;
    }

    @ThreadController(threadMode = ThreadMode.MODE_ASYNC)
    public void testThread() {
        Log.e("TAG", "点击testThread,线程：" + Thread.currentThread().getName());
        String result = "hanlonglin";
        testThread2(result);
    }

    @ThreadController(threadMode = ThreadMode.MODE_MAIN)
    public void testThread2(String s) {
        Log.e("TAG", "点击testThread2," + s + ",线程：" + Thread.currentThread().getName());
    }

    @NullParamChecker
    public void testNullCheck(String a, int b) {
        Log.e("TAG", "点击testNullCheck,线程：" + a + b);
    }

    @NullParamChecker
    public void testNullCheck2() {
        Log.e("TAG", "点击testNullCheck2,线程：");
    }

    @NeedPermission(requestCode = 101, value = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    })
    private void testPermission() {
        Log.e("TAG", "点击testPermission,线程：");
    }

    @PermissionDenied
    public void onPermissionDenied(int requestCode, String[] pers) {
        Log.e("", "onPermissionDenied() ,requestCode is:" + requestCode + ",pers.size()" + pers.length);
        Toast.makeText(this, "权限被拒绝：code:" + requestCode + ",size:" + pers.length, Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        findViewById(R.id.btn_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        testLog(1, 2);
                    }
                }).start();
            }
        });

        findViewById(R.id.btn_thread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        testThread();
//                    }
//                }).start();
                testThread();
            }
        });
        findViewById(R.id.btn_nullcheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testNullCheck2();
            }
        });
        findViewById(R.id.btn_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testPermission();
            }
        });
    }

}
