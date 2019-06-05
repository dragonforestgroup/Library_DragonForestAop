package com.dragonforest.android.library.aop.annotation.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求动态权限注解
 *
 * @author 韩龙林
 * @date 2019/6/4 14:14
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedPermission {
    int requestCode();
    String[] value();
}
