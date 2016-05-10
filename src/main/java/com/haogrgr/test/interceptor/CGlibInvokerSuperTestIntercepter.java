package com.haogrgr.test.interceptor;

import java.lang.reflect.Field;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CGlibInvokerSuperTestIntercepter {

	@Pointcut("execution(* com.haogrgr.test.event.CGlibInvokerSuperTestService.*(..))")
	public void logPointcut() {}

	@Around(value = "logPointcut()")
	public void v(ProceedingJoinPoint pjp) {
		System.err.println("CGlibInvokerSuperTestIntercepter " + pjp);

		try {
			//这个属性是用来最后调用目标对象方法的包装类, 这里具体的类型是子类CglibMethodInvocation
			Field declaredField = pjp.getClass().getDeclaredField("methodInvocation");
			System.out.println(declaredField);
			declaredField.setAccessible(true);
			ReflectiveMethodInvocation object = (ReflectiveMethodInvocation) declaredField.get(pjp);

			//CglibMethodInvocation是对cglib代理目标对象调用的一个封装, methodProxy是cglib的类, 提供方法的调用
			Field declaredField2 = object.getClass().getDeclaredField("methodProxy");
			declaredField2.setAccessible(true);
			MethodProxy xx = (MethodProxy) declaredField2.get(object);

			//invokeSuper表示调用父类, 因为cglib是继承实现的代理, 所以这里是调用被代理对象的方法
			//这里这么调用后, 会发现, 拦截方法内部的this调用也走拦截了.
			xx.invokeSuper(object.getProxy(), pjp.getArgs());

			//和上面对应, 直接调用target的方法(直接调用fastInvoke).
			//xx.invoke(object.getProxy(), pjp.getArgs());

			//而, 如果target对象被调用的方法内部又通过this.xxx()调用了方法, 则会调用到调用类的xxx方法, 进而又被拦截一次.
			//所以, spring的config类是通过invokeSuper调用来实现@Bean注解的方法被this调用也能拦截, 避免多次调用.
			//然而, Spring的配置类有一个说法叫light模式(如没有标注为@Config(有多个条件的...)), 这个模式就会产生多次调用的问题.
			//而, 一般的AOP之所以this调用会不走拦截, 是因为内部实现不是invokeSuper, 而是直接反射调用target方法(jdk proxy), 或者直接调用target方法(cglib fastinvoke)

			//思考, 为什么AOP可以实现this调用拦截, 而spring却选择不实现呢?
			//有可能是因为, 可能this调用的方法并不需要拦截, 但是通过invokeSuper的实现, 一定会走拦截, 
			//某些场景可能会有问题, 比如拦截请求记录, 通过AOP拦截来记录某一方法外部请求数量, 当这个方法中有this调用, 那么也会走AOP拦截, 如果AOP没有处理好的话, 那么计数可能会错误. 
			//可能会破坏@Pointcut语义, 比如我本意是这个方法不走拦截, 但是因为其他被拦截的方法内部this调用了这个方法, 那么就会导致原本不应该被拦截的方法走了拦截逻辑. 

		} catch (Throwable e) {
			e.printStackTrace();
		}

		//		try {
		//			pjp.proceed();
		//		} catch (Throwable e) {
		//			e.printStackTrace();
		//		}
	}

}
