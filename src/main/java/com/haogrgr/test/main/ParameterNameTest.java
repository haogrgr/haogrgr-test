package com.haogrgr.test.main;

import java.lang.reflect.Method;

import org.apache.http.HttpHost;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import com.haogrgr.test.util.HttpUtils;

public class ParameterNameTest {
	
	public static void main(String[] args) throws Exception {
		ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

		Method declaredMethod = HttpUtils.class.getDeclaredMethod("getClient", HttpHost.class);
		
		String[] names = discoverer.getParameterNames(declaredMethod);

		System.out.println(names[0]);
	}
	
}
