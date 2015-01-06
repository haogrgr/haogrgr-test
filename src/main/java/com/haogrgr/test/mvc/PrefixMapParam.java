package com.haogrgr.test.mvc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用方法,
 * 1.注册PrefixMapMethodArgumentResolver到<mvc:argument-resolvers>中
 * 2.Controller参数类型为MapWapper且打上本注解 eg:(@PrefixMapParam("prop") MapWapper prop)
 * 3.前台表单字段名使用 PrefixMapParam.value() + PrefixMapParam.split() + 参数名  eg: (prop.param1.param2) ==> MapWapper{param1=MapWapper{param2:value}}
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PrefixMapParam {
	String value() default "";
	String split() default ".";
}
