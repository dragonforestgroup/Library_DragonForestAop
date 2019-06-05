package com.dragonforest.android.library.aop.aspect;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dragonforest.android.library.aop.annotation.ThreadController;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

import static com.dragonforest.android.library.aop.model.ThreadMode.MODE_ASYNC;
import static com.dragonforest.android.library.aop.model.ThreadMode.MODE_MAIN;

/**
 * 线程切换处理类
 *
 * @author 韩龙林
 * @date 2019/6/4 12:59
 */

@Aspect
public class ThreadControllerAspect {

    /**
     * 实现线程切换
     * 控制方法在主线程或子线程中执行
     *
     * @param joinPoint
     */
    @Around("execution(@com.dragonforest.android.library.aop.annotation.ThreadController void *(..))")
    public void doAround(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ThreadController annotation = method.getAnnotation(ThreadController.class);
        switch (annotation.threadMode()) {
            case MODE_MAIN:
                runMain(joinPoint);
                break;
            case MODE_ASYNC:
                runAsync(joinPoint);
                break;
        }
    }

    private void runMain(final ProceedingJoinPoint joinPoint) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            // 当前在主线程
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            Log.e("TAG", "当前在主线程");
        } else {
            // 当前在非主线程
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        joinPoint.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            });
            Log.e("TAG", "当前在非主线程");
        }
    }

    private void runAsync(final ProceedingJoinPoint joinPoint) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            // 当前在主线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        joinPoint.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }).start();
            Log.e("TAG", "当前在主线程");
        } else {
            // 当前在非主线程
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            Log.e("TAG", "当前在非主线程");
        }
    }
}
