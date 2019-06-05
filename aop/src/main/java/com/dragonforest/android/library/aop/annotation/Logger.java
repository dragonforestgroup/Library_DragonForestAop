package com.dragonforest.android.library.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 打印方法日志
 * 需要设置日志级别
 *
 * @author 韩龙林
 * @date 2019/6/4 10:33
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface Logger {
    int value();
}
