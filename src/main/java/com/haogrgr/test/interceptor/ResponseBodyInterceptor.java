package com.haogrgr.test.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ResponseBodyInterceptor {

	@Pointcut("execution(* com.haogrgr.test.controller..*.*(..)) && @annotation(org.springframework.web.bind.annotation.ResponseBody)")
	public void logPointcut() {
	}

	@AfterReturning(value = "logPointcut()", returning = "ret")
	public void after(JoinPoint joinPoint, Object ret) {
		System.out.println(ret);
	}
	
}
