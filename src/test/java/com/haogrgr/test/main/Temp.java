package com.haogrgr.test.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Temp {

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-root.xml");
		context.close();
	}

}
