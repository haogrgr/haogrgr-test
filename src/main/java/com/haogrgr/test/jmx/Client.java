package com.haogrgr.test.jmx;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class Client {

	public static void main(String[] args) throws Exception {
		// 下面三行等价, 端口9998指JMX_RMI_SERVER_PORT 端口9999指RMI_REGISTRY_PORT
		//		 JMXServiceURL url = new JMXServiceURL("rmi", "localhost", 9998, "/jndi/rmi://localhost:9999/jmxrmi");
		//		 JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://localhost:9998/jndi/rmi://:9999/jmxrmi");
		//		 JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://localhost:9998/jndi/rmi://localhost:9999/jmxrmi");

		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://localhost/jndi/rmi://localhost:9999/jmxrmi");
		JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		MBeanServerConnection mbcs = jmxc.getMBeanServerConnection();

		ObjectName name = new ObjectName("com.haogrgr.test.jmx:key=value");
		HelloServiceImplMBean proxy = JMX.newMBeanProxy(mbcs, name, HelloServiceImplMBean.class);

		System.out.println(proxy.sayHello("haogrgr"));
	}

}
