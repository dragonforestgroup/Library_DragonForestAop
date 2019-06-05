package com.dragonforest.android.library.aop.aspect;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.dragonforest.android.library.aop.annotation.permission.NeedPermission;
import com.dragonforest.android.library.aop.annotation.permission.PermissionActivity;
import com.dragonforest.android.library.aop.annotation.permission.PermissionCanceled;
import com.dragonforest.android.library.aop.annotation.permission.PermissionDenied;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 权限检查
 *
 * @author 韩龙林
 * @date 2019/6/4 14:19
 */

@Aspect
public class PermissionAspect {

    @Around("execution(@com.dragonforest.android.library.aop.annotation.permission.NeedPermission * *(..))")
    public void doAround(final ProceedingJoinPoint joinPoint) {
        // 判断当前版本
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // 小于6.0的系统 不需要请求权限
            Log.e(getClass().getSimpleName(), "系统版本小于6.0，不需要请求权限");
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return;
        }

        final Object aThis = joinPoint.getThis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();
        NeedPermission annotation = method.getAnnotation(NeedPermission.class);
        String[] permissions = annotation.value();
        int requestCode = annotation.requestCode();

        Context context = null;
        if (aThis instanceof Activity) {
            context = (Context) aThis;
        } else if (aThis instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) aThis).getActivity();
        } else if (aThis instanceof android.app.Fragment) {
            context = ((android.app.Fragment) aThis).getActivity();
        } else if (aThis instanceof Context) {
            context = (Context) aThis;
        }
        if (context == null) {
            return;
        }

        // TODO: 2019/6/4 在这里也可以先检查权限...

        PermissionActivity.requestPermission(context, permissions, requestCode, new PermissionActivity.IPermission() {
            @Override
            public void onPermissionGrant(int requestCode, String[] grantedPers) {
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void onPermissionDenied(int requestCode, String[] deniedPers) {
                Log.e(getClass().getSimpleName(),"onPermissionDenied（）");
                Method[] methods = aThis.getClass().getMethods();
                for (Method method1 : methods) {
                    boolean annotationPresent = method1.isAnnotationPresent(PermissionDenied.class);
                    if (annotationPresent) {
                        Log.e(getClass().getSimpleName(),"deny callback method is :"+method1.getName());
                        try {
                            method1.invoke(aThis, requestCode, deniedPers);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(getClass().getSimpleName(),"You should provide a method like func(int requestCode,String []permissions) below @PermissionDenied!");
                        }
                        break;
                    }
                }
            }

            @Override
            public void onPermissionCanceled(int requestCode, String[] canceledPers) {
                Method[] methods = aThis.getClass().getMethods();
                for (Method method1 : methods) {
                    boolean annotationPresent = method1.isAnnotationPresent(PermissionCanceled.class);
                    if (annotationPresent) {
                        try {
                            method1.invoke(aThis, requestCode, canceledPers);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(getClass().getSimpleName(),"You should provide a method like func(int requestCode,String []permissions) below @PermissionCanceled!");
                        }
                        break;
                    }
                }
            }
        });
    }
}
