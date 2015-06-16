package com.haogrgr.test.util;

import java.math.BigDecimal;

/**
 * 数字工具类
 * 
 * @author desheng.tu
 * @date 2015年6月12日 下午2:17:23 
 *
 */
public class NumUtils {

	public static void main(String[] args) {
		add(BigDecimal.ZERO, null);
	}
	
	/**
	 * 返回a + b
	 */
	public static BigDecimal add(BigDecimal a, BigDecimal b){
		return a.add(b);
	}
	
	/**
	 * 返回a + b
	 */
	public static BigDecimal add(BigDecimal a, long b){
		return a.add(BigDecimal.valueOf(b));
	}
	
}
