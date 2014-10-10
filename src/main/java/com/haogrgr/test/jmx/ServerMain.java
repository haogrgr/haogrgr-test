package com.haogrgr.test.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * 如果要启动远程管理,需要加上启动参数 
 * -Dcom.sun.management.jmxremote.port=9999
 * -Dcom.sun.management.jmxremote.authenticate=false
 * -Dcom.sun.management.jmxremote.ssl=false
 */
public class ServerMain {

	@SuppressWarnings("restriction")
	public static void main(String[] args) throws Exception {
		//如果启动时,加了以下参数,下面的五行代码就不需要了, JVM会自动帮我们执行Agent.premain方法
		StringBuilder param = new StringBuilder();
		param.append("com.sun.management.jmxremote.port=9999").append(",");
		param.append("com.sun.management.jmxremote.authenticate=false").append(",");
		param.append("com.sun.management.jmxremote.ssl=false").append(",");
		sun.management.Agent.premain(param.toString());

		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

		// 等价于 ObjectName("com.haogrgr.test.jmx:key=value")
		ObjectName name = new ObjectName("com.haogrgr.test.jmx", "key", "value");
		HelloServiceImpl hello = new HelloServiceImpl();

		mbs.registerMBean(hello, name);

		System.err.println("server start !");
		Thread.sleep(Integer.MAX_VALUE);
	}

}
