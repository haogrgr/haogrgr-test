package com.haogrgr.test.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * BigDecimal工具类
 * 
 * @author desheng.tu
 * @since 2015年6月12日 下午2:17:23
 *
 */
public final class ArithUtils {
	
	/** 默认除法的保留小数位, 测试发现值为19时的时间比18时多一倍多 **/
	private static final int DIV_SCALE = 16;
	
	/** 默认除法的截取模式为四舍五入 **/
	private static final RoundingMode DIV_ROUNDING_MODE = RoundingMode.HALF_UP;
	
	/**
	 * return a + b + c
	 */
	public static BigDecimal add(BigDecimal a, BigDecimal b, BigDecimal... c) {
		Objects.requireNonNull(a);
		Objects.requireNonNull(b);

		BigDecimal sum = a.add(b);
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				Objects.requireNonNull(c[i]);
				sum = sum.add(c[i]);
			}
		}
		return sum;
	}

	/**
	 * return a + b + c
	 */
	public static BigDecimal add(BigDecimal a, long b, long... c) {
		Objects.requireNonNull(a);

		BigDecimal sum = a.add(BigDecimal.valueOf(b));
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				sum = sum.add(BigDecimal.valueOf(c[i]));
			}
		}
		return sum;
	}

	/**
	 * return a + b + c
	 */
	public static BigDecimal add(BigDecimal a, double b, double... c) {
		Objects.requireNonNull(a);

		BigDecimal sum = a.add(BigDecimal.valueOf(b));
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				sum = sum.add(BigDecimal.valueOf(c[i]));
			}
		}
		return sum;
	}

	/**
	 * return a + b + c
	 */
	public static BigDecimal add(double a, double b, double... c) {
		BigDecimal sum = BigDecimal.valueOf(a).add(BigDecimal.valueOf(b));
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				sum = sum.add(BigDecimal.valueOf(c[i]));
			}
		}
		return sum;
	}

	/**
	 * return a + b + c, 如果元素为空, 则使用0代替
	 */
	public static BigDecimal addna(BigDecimal a, BigDecimal b, BigDecimal... c) {
		a = a == null ? BigDecimal.ZERO : a;
		b = b == null ? BigDecimal.ZERO : b;

		BigDecimal sum = a.add(b);
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				if (c[i] != null)
					sum = sum.add(c[i]);
			}
		}

		return sum;
	}

	/**
	 * return a -b -c
	 */
	public static BigDecimal sub(BigDecimal a, BigDecimal b, BigDecimal... c) {
		Objects.requireNonNull(a);
		Objects.requireNonNull(b);

		BigDecimal sub = a.subtract(b);
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				Objects.requireNonNull(c[i]);
				sub = sub.subtract(c[i]);
			}
		}

		return sub;
	}

	/**
	 * return a -b -c
	 */
	public static BigDecimal sub(BigDecimal a, long b, long... c) {
		Objects.requireNonNull(a);

		BigDecimal sub = a.subtract(BigDecimal.valueOf(b));
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				sub = sub.subtract(BigDecimal.valueOf(c[i]));
			}
		}

		return sub;
	}

	/**
	 * return a -b -c
	 */
	public static BigDecimal sub(BigDecimal a, double b, double... c) {
		Objects.requireNonNull(a);

		BigDecimal sub = a.subtract(BigDecimal.valueOf(b));
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				sub = sub.subtract(BigDecimal.valueOf(c[i]));
			}
		}

		return sub;
	}

	/**
	 * return a -b -c, 如果元素为空, 则使用0代替
	 */
	public static BigDecimal subna(BigDecimal a, BigDecimal b, BigDecimal... c) {
		a = a == null ? BigDecimal.ZERO : a;
		b = b == null ? BigDecimal.ZERO : b;

		BigDecimal sub = a.subtract(b);
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				if (c[i] != null)
					sub = sub.subtract(c[i]);
			}
		}

		return sub;
	}

	/**
	 * return a * b * c
	 */
	public static BigDecimal mul(BigDecimal a, BigDecimal b, BigDecimal... c) {
		Objects.requireNonNull(a);
		Objects.requireNonNull(b);

		BigDecimal mul = a.multiply(b);
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				Objects.requireNonNull(c[i]);
				mul = mul.multiply(c[i]);
			}
		}
		return mul;
	}

	/**
	 * return a * b * c
	 */
	public static BigDecimal mul(BigDecimal a, long b, long... c) {
		Objects.requireNonNull(a);

		BigDecimal mul = a.multiply(BigDecimal.valueOf(b));
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				mul = mul.multiply(BigDecimal.valueOf(c[i]));
			}
		}
		return mul;
	}

	/**
	 * return a * b * c
	 */
	public static BigDecimal mul(BigDecimal a, double b, double... c) {
		Objects.requireNonNull(a);

		BigDecimal mul = a.multiply(BigDecimal.valueOf(b));
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				mul = mul.multiply(BigDecimal.valueOf(c[i]));
			}
		}
		return mul;
	}

	/**
	 * return (a / b) / c, 返回值保留DIV_SCALE位小数
	 */
	public static BigDecimal div(BigDecimal a, BigDecimal b, BigDecimal... c) {
		Objects.requireNonNull(a);
		Objects.requireNonNull(b);
		
		BigDecimal divide = a.divide(b, DIV_SCALE, DIV_ROUNDING_MODE);
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				Objects.requireNonNull(c[i]);
				divide = divide.divide(c[i], DIV_SCALE, DIV_ROUNDING_MODE);
			}
		}
		return divide;
	}

	/**
	 * return (a / b) / c, 返回值保留DIV_SCALE位小数
	 */
	public static BigDecimal div(BigDecimal a, long b, long... c) {
		Objects.requireNonNull(a);

		BigDecimal divide = a.divide(BigDecimal.valueOf(b), DIV_SCALE, DIV_ROUNDING_MODE);
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				divide = divide.divide(BigDecimal.valueOf(c[i]), DIV_SCALE, DIV_ROUNDING_MODE);
			}
		}
		return divide;
	}

	/**
	 * return (a / b) / c, 返回值保留DIV_SCALE位小数
	 */
	public static BigDecimal div(BigDecimal a, double b, double... c) {
		Objects.requireNonNull(a);

		BigDecimal divide = a.divide(BigDecimal.valueOf(b), DIV_SCALE, DIV_ROUNDING_MODE);
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				divide = divide.divide(BigDecimal.valueOf(c[i]), DIV_SCALE, DIV_ROUNDING_MODE);
			}
		}
		return divide;
	}

	/**
	 * return a > b
	 */
	public static boolean gt(BigDecimal a, BigDecimal b) {
		Objects.requireNonNull(a);
		Objects.requireNonNull(b);
		return a.compareTo(b) == 1;
	}

	/**
	 * return a > b
	 */
	public static boolean gt(BigDecimal a, long b) {
		Objects.requireNonNull(a);
		return a.compareTo(BigDecimal.valueOf(b)) == 1;
	}

	/**
	 * return a > b
	 */
	public static boolean gt(BigDecimal a, double b) {
		Objects.requireNonNull(a);
		return a.compareTo(BigDecimal.valueOf(b)) == 1;
	}

	/**
	 * return a >= b
	 */
	public static boolean gtOrEq(BigDecimal a, BigDecimal b) {
		Objects.requireNonNull(a);
		Objects.requireNonNull(b);
		return a.compareTo(b) > -1;
	}

	/**
	 * return a >= b
	 */
	public static boolean gtOrEq(BigDecimal a, long b) {
		Objects.requireNonNull(a);
		return a.compareTo(BigDecimal.valueOf(b)) > -1;
	}

	/**
	 * return a >= b
	 */
	public static boolean gtOrEq(BigDecimal a, double b) {
		Objects.requireNonNull(a);
		return a.compareTo(BigDecimal.valueOf(b)) > -1;
	}

	/**
	 * return a < b
	 */
	public static boolean lt(BigDecimal a, BigDecimal b) {
		Objects.requireNonNull(a);
		Objects.requireNonNull(b);
		return a.compareTo(b) == -1;
	}

	/**
	 * return a < b
	 */
	public static boolean lt(BigDecimal a, long b) {
		Objects.requireNonNull(a);
		return a.compareTo(BigDecimal.valueOf(b)) == -1;
	}

	/**
	 * return a < b
	 */
	public static boolean lt(BigDecimal a, double b) {
		Objects.requireNonNull(a);
		return a.compareTo(BigDecimal.valueOf(b)) == -1;
	}

	/**
	 * return a <= b
	 */
	public static boolean ltOrEq(BigDecimal a, BigDecimal b) {
		Objects.requireNonNull(a);
		Objects.requireNonNull(b);
		return a.compareTo(b) < 1;
	}

	/**
	 * return a <= b
	 */
	public static boolean ltOrEq(BigDecimal a, long b) {
		Objects.requireNonNull(a);
		return a.compareTo(BigDecimal.valueOf(b)) < 1;
	}

	/**
	 * return a <= b
	 */
	public static boolean ltOrEq(BigDecimal a, double b) {
		Objects.requireNonNull(a);
		return a.compareTo(BigDecimal.valueOf(b)) < 1;
	}

	/**
	 * return a == b
	 */
	public static boolean eq(BigDecimal a, BigDecimal b, BigDecimal... c) {
		Objects.requireNonNull(a);
		Objects.requireNonNull(b);

		if (a.compareTo(b) != 0) {
			return false;
		}

		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				Objects.requireNonNull(c[i]);
				if (a.compareTo(c[i]) != 0)
					return false;
			}
		}

		return true;
	}

	/**
	 * return a == b
	 */
	public static boolean eq(BigDecimal a, long b) {
		Objects.requireNonNull(a);
		return a.compareTo(BigDecimal.valueOf(b)) == 0;
	}

	/**
	 * return a == b
	 */
	public static boolean eq(BigDecimal a, double b) {
		Objects.requireNonNull(a);
		return a.compareTo(BigDecimal.valueOf(b)) == 0;
	}

	/**
	 * 四舍五入方法保留2位小数位,
	 */
	public static BigDecimal halfUp(BigDecimal n) {
		return n.setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * 四舍五入方法保留2位小数位,
	 */
	public static BigDecimal halfUp(double n) {
		return BigDecimal.valueOf(n).setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * 四舍五入方法保留指定小数位
	 */
	public static BigDecimal halfUp(BigDecimal n, int scale) {
		return n.setScale(scale, RoundingMode.HALF_UP);
	}

	/**
	 * 四舍五入方法保留指定小数位
	 */
	public static BigDecimal halfUp(double n, int scale) {
		return BigDecimal.valueOf(n).setScale(scale, RoundingMode.HALF_UP);
	}
	
	/**
	 * 保留指定小数位,其他的直接舍去
	 */
	public static BigDecimal down(BigDecimal n, int scale) {
		return n.setScale(scale, RoundingMode.DOWN);
	}

	/**
	 * 保留指定小数位,其他的直接舍去
	 */
	public static BigDecimal down(double n, int scale) {
		return BigDecimal.valueOf(n).setScale(scale, RoundingMode.DOWN);
	}
	
	/**
	 * 四舍五入方法保留2位小数位,
	 */
	public static String halfUpStr(BigDecimal n) {
		return n.setScale(2, RoundingMode.HALF_UP).toString();
	}

	/**
	 * 四舍五入方法保留2位小数位,
	 */
	public static String halfUpStr(double n) {
		return BigDecimal.valueOf(n).setScale(2, RoundingMode.HALF_UP).toString();
	}

	/**
	 * 四舍五入方法保留指定小数位
	 */
	public static String halfUpStr(BigDecimal n, int scale) {
		return n.setScale(scale, RoundingMode.HALF_UP).toString();
	}

	/**
	 * 四舍五入方法保留指定小数位
	 */
	public static String halfUpStr(double n, int scale) {
		return BigDecimal.valueOf(n).setScale(scale, RoundingMode.HALF_UP).toString();
	}

	/**
	 * 取最大值
	 */
	public static BigDecimal max(BigDecimal... eles) {
		if (eles == null || eles.length == 0) {
			return null;
		}

		BigDecimal max = eles[0];
		for (BigDecimal ele : eles) {
			Objects.requireNonNull(ele);
			if (gt(ele, max))
				max = ele;
		}

		return max;
	}
	
	/**
	 * 取最大值
	 */
	public static int max(int a, int b, int c) {
		return Math.max(Math.max(a, b), c);
	}
	

	/**
	 * 取最小值
	 */
	public static BigDecimal min(BigDecimal... eles) {
		if (eles == null || eles.length == 0) {
			return null;
		}

		BigDecimal min = eles[0];
		for (BigDecimal ele : eles) {
			Objects.requireNonNull(ele);
			if (lt(ele, min))
				min = ele;
		}

		return min;
	}

	/**
	 * 将BigInteger, Float, Double, Long, Integer, Short, Byte, String转换为BigDecimal
	 */
	public static BigDecimal of(Object number) {
		Objects.requireNonNull(number);

		Class<?> clazz = number.getClass();
		
		if (clazz == Double.class) {
			return BigDecimal.valueOf((Double) number);
		}
		if (clazz == BigDecimal.class) {
			return (BigDecimal) number;
		}
		if(clazz == String.class){
			return new BigDecimal((String)number);
		}
		if (clazz == Integer.class) {
			return BigDecimal.valueOf((Integer) number);
		}
		if (clazz == Float.class) {
			return BigDecimal.valueOf((Float) number);
		}
		if (clazz == Long.class) {
			return BigDecimal.valueOf((Long) number);
		}
		if (clazz == BigInteger.class) {
			return new BigDecimal((BigInteger) number);
		}
		if (clazz == Short.class) {
			return BigDecimal.valueOf((Short) number);
		}
		if (clazz == Byte.class) {
			return BigDecimal.valueOf((Byte) number);
		}

		throw new IllegalArgumentException("不支持的转换类型 : " + number.getClass());
	}

}
