package com.haogrgr.test.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ResponseBody
@RequestMapping(produces="application/json;charset=UTF-8")
public @interface Json {

	@AliasFor("path")
	String[] value() default {};
	
	@AliasFor("value")
	String[] path() default {};
	
}
