package com.dragonforest.android.library.aop.annotation.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限请求取消注解
 * 被修饰的方法必须有参数结构（int requestCode,String[] permissions）
 *
 * @author 韩龙林
 * @date 2019/6/4 14:16
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionCanceled {
}
