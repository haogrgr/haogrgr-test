package com.haogrgr.test.interceptor;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.haogrgr.test.exception.BizException;

@Aspect
@Component
public class LogInterceptor {

    private static Logger logger = Logger.getLogger(LogInterceptor.class);
    
    @Pointcut("execution(* com.haogrgr.test.service..*.*(..))")
    public void pointcut(){}
    
    @AfterThrowing(value = "pointcut()", throwing="t")
    public void doLogging(JoinPoint joinPoint, Throwable t) {
        if(t instanceof BizException){
        	BizException be = (BizException) t;
            logger.error("\nlog aop : " + getDebugInfo(joinPoint) + "\ndebug : " + be.getMsg() + "\nmsg : " + be.getMessage());
        }else{
            logger.error("log aop : " + getDebugInfo(joinPoint), t);
        }
    }
    
    private static String getDebugInfo(JoinPoint joinPoint){
        String result = joinPoint.toLongString() + " args : ";
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            result = result + (arg == null ? "null" : arg.toString()) + ", ";
        }
        return result;
    }
    
}
