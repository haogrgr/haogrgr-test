package com.haogrgr.test.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 方便设置线程名字
 * 
 * @author desheng.tu
 * @since 2015年9月22日 上午11:54:34
 *
 */
public class NamedThreadFactory implements ThreadFactory {

	private final String baseName;
	private final AtomicInteger threadNum = new AtomicInteger(0);

	public NamedThreadFactory(String baseName) {
		this.baseName = baseName;
	}

	@Override
	public synchronized Thread newThread(Runnable r) {
		Thread t = Executors.defaultThreadFactory().newThread(r);

		t.setName(baseName + "-" + threadNum.getAndIncrement());

		return t;
	}
}
