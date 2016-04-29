package com.haogrgr.test.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringPlaceholder {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-root.xml");

		String text = context.getBeanFactory().resolveEmbeddedValue("${db.url}");
		System.err.println(text);

		context.close();
	}

}
