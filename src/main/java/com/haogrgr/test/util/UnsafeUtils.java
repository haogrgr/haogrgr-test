package com.haogrgr.test.util;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * 简单的unsafe工具类~~~超简单的那种~~~ 后期会提供更多的包装, 以免后来Unsafe没有了, 要到处改代码~~~netty就自己封装了一套.
 * 
 * @author desheng.tu
 * @date 2015年8月3日 下午3:36:17
 *
 */
@SuppressWarnings("restriction")
public class UnsafeUtils {

	/**
	 * 通过反射获取Unsafe实例, 突破权限检查, 内部其实就是返回一个静态实例, 所以获取到的实例可以到处用, 线程安全.
	 */
	public static Unsafe getUnsafe() {
		Unsafe unsafe = null;
		try {
			Field field = Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			unsafe = (Unsafe) field.get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return unsafe;
	}

	/**
	 * 获取字段偏移, 用于后续操作
	 * @param unsafe unsafe实例
	 * @param clazz 字段所在类
	 * @param field 对应字段
	 */
	public static long offset(Unsafe unsafe, Class<?> clazz, String field) {
		try {
			return unsafe.objectFieldOffset(clazz.getDeclaredField(field));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
