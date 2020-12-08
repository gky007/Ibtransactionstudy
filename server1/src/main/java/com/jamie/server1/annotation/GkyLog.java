package com.jamie.server1.annotation;

import java.lang.annotation.*;

/**
 * @author jamie
 * @version 1.0.0
 * @date 2020/12/8 16:52
 * @description
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GkyLog {
    /** 要执行的操作类型比如：add操作 **/
    public String operationType() default "";

    /** 要执行的具体操作比如：添加用户 **/
    public String operationName() default "";
}
