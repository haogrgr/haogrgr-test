package com.haogrgr.test.util;

import org.apache.commons.lang.StringUtils;

import com.haogrgr.test.exception.BizException;

public class ExpUtil {

	public static void main(String[] args) {

	}

	/**
	 * 是否抛异常 eg: ExpUtil.throwExp(xxx == null, "显示信息");
	 * 
	 * @param isThrow
	 *            抛异常的条件
	 * @param msg
	 *            显示信息
	 */
	public static void throwExp(boolean isThrow, String msg) throws BizException {
		if (isThrow) {
			throw new BizException(msg);
		}
	}

	/**
	 * 为空就报异常
	 * 
	 * @param test
	 * @param msg
	 * @throws BizException
	 */
	public static void throwIfBlank(String test, String msg) throws BizException {
		if (StringUtils.isBlank(test)) {
			throw new BizException(msg);
		}
	}

	/**
	 * 为空就报异常
	 * 
	 * @param test
	 * @param msg
	 * @throws BizException
	 */
	public static void throwIfBlank(Integer test, String msg) throws BizException {
		if (test == null) {
			throw new BizException(msg);
		}
	}

	/**
	 * 是否抛异常 eg: ExpUtil.throwExp(xxx == null, "前台显示信息", "调试信息");
	 * 
	 * @param isThrow
	 *            true : 抛异常
	 * @param msg
	 *            显示信息
	 * @param debug
	 *            调试信息
	 */
	public static void throwExp(boolean isThrow, String msg, String debug) throws BizException {
		if (isThrow) {
			throw new BizException(msg, debug);
		}
	}

}
