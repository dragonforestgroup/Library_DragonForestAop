package com.dragonforest.android.library.aop.aspect;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 检查空值参数的注解
 * 用于必须保证参数都不为空才能执行的方法
 *
 * @author 韩龙林
 * @date 2019/6/4 13:38
 */
@Aspect
public class NullParamCheckerAspect {

    /**
     * 检查参数null值
     *
     * @param joinPoint
     * @return
     */
    @Around("execution(@com.dragonforest.android.library.aop.annotation.NullParamChecker * *(..)) ")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        Object result = null;
        // 检查参数是否都不为空
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class[] parameterTypes = signature.getParameterTypes();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i <args.length ; i++) {
            if(args[i]==null){
                // 存在空值 检查不通过
                // TODO: 2019/6/4 当参数有空值时 如何处理还需完善 
                String msg="发现"+method.getName()+" 方法的参数："+parameterTypes[i].getSimpleName()+" "+parameterNames[i]+"是空值，方法执行被阻止";
                Log.e(getClass().getSimpleName(),msg);
                return result;
            }
        }
        // 检查通过
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }
}
