package com.haogrgr.test.util;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AppContextUtil {

	private static Logger logger = Logger.getLogger(AppContextUtil.class);

	private static ApplicationContext context = null;

	@EventListener
	public void setApplicationContext(ContextRefreshedEvent eve) {
		context = eve.getApplicationContext();
	}

	/**
	 * 通过beanId和class获取spring中注册的bean
	 * 
	 * @throws Exception ApplicationContext未注入
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		if (context != null) {
			return context.getBean(name, clazz);
		}
		else {
			logger.error("context 未初始化");
			throw new RuntimeException("context 未初始化");
		}
	}

	/**
	 * 通过beanId获取spring中注册的bean
	 * 
	 * @throws Exception ApplicationContext未注入
	 */
	public static Object getBean(String name) {
		if (context != null) {
			return context.getBean(name);
		}
		else {
			logger.error("context 未初始化");
			throw new RuntimeException("context 未初始化");
		}
	}

	/**
	 * 通过class获取spring中注册的bean
	 * 
	 * @throws Exception ApplicationContext未注入
	 */
	public static <T> T getBean(Class<T> clazz) {
		if (context != null) {
			return context.getBean(clazz);
		}
		else {
			logger.error("context 未初始化");
			throw new RuntimeException("context 未初始化");
		}
	}

	/**
	 * 获取spring的上下文对象
	 */
	public static ApplicationContext getContext() {
		return context;
	}

}
