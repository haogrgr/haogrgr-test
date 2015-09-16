package com.haogrgr.test.util;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 自已用的List工具类, 好吧, 还是超的Guava
 * 
 * @author desheng.tu
 * @date 2015年9月16日 上午10:36:06
 *
 */
public class Lists {

	/**
	 * 创建ArrayList
	 */
	public static <T> ArrayList<T> array() {
		return new ArrayList<T>();
	}

	/**
	 * 创建ArrayList
	 */
	public static <T> ArrayList<T> array(int initialCapacity) {
		return new ArrayList<T>(initialCapacity);
	}

	/**
	 * 创建长度为1的ArrayList
	 */
	public static <T> ArrayList<T> array(T e1) {
		ArrayList<T> list = new ArrayList<T>(1);
		list.add(e1);
		return list;
	}

	/**
	 * 创建长度为2的ArrayList
	 */
	public static <T> ArrayList<T> array(T e1, T e2) {
		ArrayList<T> list = new ArrayList<T>(2);
		list.add(e1);
		list.add(e2);
		return list;
	}

	/**
	 * 创建长度为3的ArrayList
	 */
	public static <T> ArrayList<T> array(T e1, T e2, T e3) {
		ArrayList<T> list = new ArrayList<T>(3);
		list.add(e1);
		list.add(e2);
		list.add(e3);
		return list;
	}

	/**
	 * 创建长度为4的ArrayList
	 */
	public static <T> ArrayList<T> array(T e1, T e2, T e3, T e4) {
		ArrayList<T> list = new ArrayList<T>(4);
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		return list;
	}

	/**
	 * 创建长度为5的ArrayList
	 */
	public static <T> ArrayList<T> array(T e1, T e2, T e3, T e4, T e5) {
		ArrayList<T> list = new ArrayList<T>(5);
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		list.add(e5);
		return list;
	}

	/**
	 * 创建长度为6的ArrayList
	 */
	public static <T> ArrayList<T> array(T e1, T e2, T e3, T e4, T e5, T e6) {
		ArrayList<T> list = new ArrayList<T>(6);
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		list.add(e5);
		list.add(e6);
		return list;
	}

	/**
	 * 创建长度为7的ArrayList
	 */
	public static <T> ArrayList<T> array(T e1, T e2, T e3, T e4, T e5, T e6, T e7) {
		ArrayList<T> list = new ArrayList<T>(7);
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		list.add(e5);
		list.add(e6);
		list.add(e7);
		return list;
	}

	/**
	 * 创建长度为8的ArrayList
	 */
	public static <T> ArrayList<T> array(T e1, T e2, T e3, T e4, T e5, T e6, T e7, T e8) {
		ArrayList<T> list = new ArrayList<T>(8);
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		list.add(e5);
		list.add(e6);
		list.add(e7);
		list.add(e8);
		return list;
	}

	/**
	 * 创建LinkedList
	 */
	public static <T> LinkedList<T> linked() {
		return new LinkedList<T>();
	}

	/**
	 * 创建长度为1的LinkedList
	 */
	public static <T> LinkedList<T> linked(T e1) {
		LinkedList<T> list = new LinkedList<T>();
		list.add(e1);
		return list;
	}

	/**
	 * 创建长度为2的LinkedList
	 */
	public static <T> LinkedList<T> linked(T e1, T e2) {
		LinkedList<T> list = new LinkedList<T>();
		list.add(e1);
		list.add(e2);
		return list;
	}

	/**
	 * 创建长度为3的LinkedList
	 */
	public static <T> LinkedList<T> linked(T e1, T e2, T e3) {
		LinkedList<T> list = new LinkedList<T>();
		list.add(e1);
		list.add(e2);
		list.add(e3);
		return list;
	}

	/**
	 * 创建长度为4的LinkedList
	 */
	public static <T> LinkedList<T> linked(T e1, T e2, T e3, T e4) {
		LinkedList<T> list = new LinkedList<T>();
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		return list;
	}

	/**
	 * 创建长度为5的LinkedList
	 */
	public static <T> LinkedList<T> linked(T e1, T e2, T e3, T e4, T e5) {
		LinkedList<T> list = new LinkedList<T>();
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		list.add(e5);
		return list;
	}

	/**
	 * 创建长度为6的LinkedList
	 */
	public static <T> LinkedList<T> linked(T e1, T e2, T e3, T e4, T e5, T e6) {
		LinkedList<T> list = new LinkedList<T>();
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		list.add(e5);
		list.add(e6);
		return list;
	}

	/**
	 * 创建长度为7的LinkedList
	 */
	public static <T> LinkedList<T> linked(T e1, T e2, T e3, T e4, T e5, T e6, T e7) {
		LinkedList<T> list = new LinkedList<T>();
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		list.add(e5);
		list.add(e6);
		list.add(e7);
		return list;
	}

	/**
	 * 创建长度为8的LinkedList
	 */
	public static <T> LinkedList<T> linked(T e1, T e2, T e3, T e4, T e5, T e6, T e7, T e8) {
		LinkedList<T> list = new LinkedList<T>();
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		list.add(e5);
		list.add(e6);
		list.add(e7);
		list.add(e8);
		return list;
	}

}
