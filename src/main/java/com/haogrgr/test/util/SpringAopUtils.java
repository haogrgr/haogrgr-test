package com.haogrgr.test.util;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;

/**
 * 使用Spring AOP相关的类来手动建立代理.
 *
 * @date 2015年3月31日 下午5:14:51
 * @author https://github.com/stillotherguy/javaopensource/blob/master/src/main/java/com/rabbit/spring/aop/Main.java
 */
public class SpringAopUtils {

	public static void main(String[] args) {
		//需要被代理的类
		TobeProxy bean = new TobeProxy();

		//组合Pointcut(拦截点)和Advisor(附加逻辑)
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new SimpleDynamicPointcut(), new SimpleAdvice());

		//代理工厂
		ProxyFactory factory = new ProxyFactory();
		factory.addAdvisor(advisor);
		factory.setTarget(bean);

		TobeProxy proxy = (TobeProxy) factory.getProxy();
		proxy.foo(1);
		proxy.foo(100);//符合拦截条件,被拦截
		proxy.bar();

	}

	public static class TobeProxy {

		public void foo(int x) {
			System.out.println("foo : " + x);
		}

		public void bar() {
			System.out.println("bar");
		}

	}

	public static class SimpleAdvice implements MethodInterceptor {

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			System.out.println("before invocation ... ");
			Object result = invocation.proceed();
			System.out.println("after invocation ... ");
			return result;
		}

	}

	/**
	 * 动态的Pointcut, 比较费性能, 因为每次调用都要基于参数值判断是否应用Advice, 无法缓存
	 * 
	 * 对于的, 有静态的Pointcut, StaticMethodMatcherPointcut类
	 *
	 * @date 2015年3月31日 下午4:42:40
	 * @author desheng.tu
	 */
	public static class SimpleDynamicPointcut extends DynamicMethodMatcherPointcut {

		@Override
		public boolean matches(Method method, Class<?> targetClass, Object... args) {
			if ("foo".equals(method.getName()) && ((int) args[0]) == 100) {
				return true;
			}
			return false;
		}

		@Override
		public ClassFilter getClassFilter() {
			return new ClassFilter() {
				@Override
				public boolean matches(Class<?> clazz) {
					return clazz.isAssignableFrom(TobeProxy.class);
				}
			};
		}

	}
}
