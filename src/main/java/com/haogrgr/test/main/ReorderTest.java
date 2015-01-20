package com.haogrgr.test.main;

import java.util.ArrayList;
import java.util.List;

public class ReorderTest {
	private static List<String> l = new ArrayList<>();

	public static void main(String[] args) {
		Thread[] threads = new Thread[20];

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						synchronized (ReorderTest.class) {
							if (l.size() < 1024)
								l.add("ttt");
						}

					}
				}
			});
			threads[i].start();
		}

		Thread t1 = new Thread() {
			public void run() {
				while (true) {
					//l加上volatile即可解决,利用了volatile获取语义
					//JMM的final语义 ：1在构造函数内对一个final域的写入，与随后把这个被构造对象的引用赋值给一个引用变量，这两个操作之间不能重排序。 
					//2初次读一个包含final域的对象的引用，与随后初次读这个final域，这两个操作之间不能重排序。 
					/**
					 * 曾经天真的以为JDK8已经修复了上述问题，其实没有，是因为看错了逻辑，如果initialCapacity ==
					 * 0时确实没有上面的问题，
					 * 因为EMPTY_ELEMENTDATA是一个final类型的依据JMM可以保证，
					 * 调用add的线程在拿到的arraylist的引用是已经初始化好的，加上
					 * volatile即可避免此错误，利用了volatile的获取语义
					 */
					List<String> local = l;
					l = new ArrayList<String>(1024);
				}
			}
		};
		t1.start();
	}

}