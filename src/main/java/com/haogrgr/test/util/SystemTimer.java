package com.haogrgr.test.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 不精确的时间调用. System.currentTimeMillis()调用是系统调用, 虽然不慢, 但是频繁调用损耗还是比较大的. 当不需要精确的时间时,
 * 可以使用定时任务来缓存时间, 通过牺牲精度来换取性能.
 * 
 * @see https://blogs.oracle.com/dholmes/entry/inside_the_hotspot_vm_clocks
 * @see http://www.blogjava.net/killme2008/archive/2014/09/01/338420.html
 * 
 * @author desheng.tu
 * @since 2015年7月23日 下午7:41:03
 *
 */
public class SystemTimer {

	private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor((r) -> {
		return new Thread(r, "systime-schedule-thread");
	});

	private static final long tickUnit = Long.parseLong(System.getProperty("notify.systimer.tick", "50"));

	private static volatile long now = System.currentTimeMillis();

	/**
	 * 获取不准确的当前时间
	 */
	public long currentTimeMillis() {
		return now;
	}

	static {
		executor.scheduleAtFixedRate(() -> now = System.currentTimeMillis(), tickUnit, tickUnit, TimeUnit.MILLISECONDS);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				executor.shutdown();
			}
		});
	}

}
