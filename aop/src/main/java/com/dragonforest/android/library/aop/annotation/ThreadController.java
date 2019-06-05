package com.dragonforest.android.library.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 线程切换注解
 *
 * @author 韩龙林
 * @date 2019/6/4 12:55
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface ThreadController {

   int threadMode();
}
