package com.dragonforest.android.library.aop.aspect;

import android.util.Log;

import com.dragonforest.android.library.aop.annotation.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclarePrecedence;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 日志记录切面
 *
 * @author 韩龙林
 * @date 2019/6/4 10:40
 */

@Aspect

public class LoggerAspect {

    private final static String LOG_TITLE = "DragonForest帮你打印日志:";

    /**
     * 记录日志，需要记录的信息
     * <br>
     * 1.线程名
     * <br>
     * 2.类名+方法名
     * <br>
     * 3.参数类型+参数值
     * <br>
     * 4.返回类型+返回值
     * <br>
     * 5.消耗时间
     * <br>
     *
     * @param joinPoint
     * @return
     */
    @Around("execution(@com.dragonforest.android.library.aop.annotation.Logger * *(..))")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        //==================搜集信息=========================
        // 1.获得开始和结束时间
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        long endTime = System.currentTimeMillis();

        // 2.获得方法名和类名
        String className = joinPoint.getTarget().getClass().getName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();

        // 3.获得参数类型和值
        Class[] parameterTypes = signature.getParameterTypes();
        String[] parameterNames = signature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();
        StringBuilder paramsBuilder = new StringBuilder();
        for (int i = 0; i < parameterNames.length; i++) {
            paramsBuilder.append(parameterTypes[i].getSimpleName());
            paramsBuilder.append(" ");
            paramsBuilder.append(parameterNames[i]);
            paramsBuilder.append(" = ");
            paramsBuilder.append(parameterValues[i]);
            paramsBuilder.append(" , ");
        }
        String paramsStr = paramsBuilder.toString();

        // 4.获得返回值的类型和值
        Class returnType = signature.getReturnType();

        // 5.获取线程名
        String threadName = Thread.currentThread().getName().toString();

        //===================拼接字符串=====================

        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(LOG_TITLE).append("\n");
        contentBuilder.append("╔══════════════════════════════════════════════════════════════\n");
        contentBuilder.append("║ 函数：" + className + "." + methodName).append("\n");
        contentBuilder.append("╟──────────────────────────────────────────────────────────────").append("\n");
        contentBuilder.append("║ 当前线程：" + Thread.currentThread().getName()).append("\n");
        contentBuilder.append("╟──────────────────────────────────────────────────────────────").append("\n");
        contentBuilder.append("║ 参数：" + paramsStr).append("\n");
        contentBuilder.append("╟──────────────────────────────────────────────────────────────").append("\n");
        contentBuilder.append("║ 返回：" + method.getReturnType().getSimpleName()).append(" result").append(" = ").append(result).append("\n");
        contentBuilder.append("╟──────────────────────────────────────────────────────────────").append("\n");
        contentBuilder.append("║ 耗时：" + (endTime - startTime)).append(" ms").append("\n");
        contentBuilder.append("╚══════════════════════════════════════════════════════════════\n");

        String contentStr = contentBuilder.toString();

        // =================根据级别输出日志=====================
        Logger annotation = method.getAnnotation(Logger.class);
        int level = annotation.value();
        switch (level) {
            case Log.VERBOSE:
                Log.v(getClass().getSimpleName() + "\n", contentStr);
                break;
            case Log.DEBUG:
                Log.d(getClass().getSimpleName() + "\n", contentStr);
                break;
            case Log.INFO:
                Log.i(getClass().getSimpleName() + "\n", contentStr);
                break;
            case Log.WARN:
                Log.w(getClass().getSimpleName() + "\n", contentStr);
                break;
            case Log.ERROR:
                Log.e(getClass().getSimpleName() + "\n", contentStr);
                break;
        }
        return result;
    }
}
