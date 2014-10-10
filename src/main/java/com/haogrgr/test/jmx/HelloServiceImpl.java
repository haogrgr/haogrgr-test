package com.haogrgr.test.jmx;

public class HelloServiceImpl implements HelloServiceImplMBean {

	@Override
	public String sayHello(String name) {
		return "hello : " + name;
	}

}
