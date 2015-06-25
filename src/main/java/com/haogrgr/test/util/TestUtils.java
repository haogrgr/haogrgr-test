package com.haogrgr.test.util;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestUtils {

	public static void main(String[] args) {
		TestUtils.beanCopyCode(PageInfo.class, "info", "xxx");
	}

	private static ApplicationContext context = null;

	public synchronized static ApplicationContext initSpring() {
		return initSpring("classpath*:spring-root.xml");
	}

	public synchronized static ApplicationContext initSpring(String path) {
		if (context == null) {
			context = new ClassPathXmlApplicationContext(path);
		}
		return context;
	}

	public static <T> T getBean(String name, Class<T> clazz) {
		if (context != null) {
			return context.getBean(name, clazz);
		}
		else {
			throw new NullPointerException("context为空");
		}
	}

	public static Object getBean(String name) {
		if (context != null) {
			return context.getBean(name);
		}
		else {
			throw new NullPointerException("context为空");
		}
	}

	public static <T> T getBean(Class<T> clazz) {
		if (context != null) {
			return context.getBean(clazz);
		}
		else {
			throw new NullPointerException("context为空");
		}
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static String getTimeStr() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return df.format(new Date());
	}

	/**
	 * 修改系统时间
	 * @param date 日期 eg: 2011-10-10
	 * @param time 时间 eg: 13:45:63
	 * @return 命令执行回显
	 */
	public static String setSystemTime(String date, String time) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date datetime = df.parse(date + " " + time);

			System.out.println(new Date());
			String exec = exec("time " + time) + " " + exec("date " + date);
			System.out.println(new Date());

			long diff = System.currentTimeMillis() - datetime.getTime();

			System.out.println(diff);
			if (Math.abs(diff) > 1000) {
				throw new RuntimeException("修改后时间和指定的时间相差太大!");
			}

			return exec;
		}
		catch (ParseException e) {
			throw new RuntimeException("修改系统时间失败, 日期或时间格式错误!", e);
		}
	}

	public static String exec(String cmd) {
		try {
			Process exec = Runtime.getRuntime().exec("cmd /c " + cmd);

			InputStreamReader r = new InputStreamReader(exec.getInputStream(), "GBK");
			BufferedReader br = new BufferedReader(r);

			String result = "";
			String line = br.readLine();
			if (line != null) {
				result = result + line;
				line = br.readLine();
			}

			return result;
		}
		catch (Exception e) {
			throw new RuntimeException("执行" + cmd + "命令失败!", e);
		}
	}

	public static void beanCopyCode(Class<?> srcClz, String srcName, String destName) {
		StringBuilder sb = new StringBuilder();

		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(srcClz);
		for (PropertyDescriptor pd : pds) {
			if (pd.getWriteMethod() != null && pd.getReadMethod() != null) {
				sb.append(srcName).append(".").append(pd.getWriteMethod().getName())
						.append("(" + destName + ".").append(pd.getReadMethod().getName())
						.append("())").append(";\n");
			}
		}

		System.out.println(sb);
	}

	public static String fixSpec(String str, int len) {
		if (str != null && str.length() < len) {
			for (int i = 0, slen = str.length(); i < len - slen; i++) {
				str = str + " ";
			}
		}
		return str;
	}

}
