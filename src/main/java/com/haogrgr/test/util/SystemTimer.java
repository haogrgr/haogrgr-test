package com.haogrgr.test.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 不精确的时间调用. System.currentTimeMillis()调用是系统调用, 虽然不慢, 但是频繁调用损耗还是比较大的. 当不需要精确的时间时,
 * 可以使用定时任务来缓存时间, 通过牺牲精度来换取性能.
 * 
 * @see https://blogs.oracle.com/dholmes/entry/inside_the_hotspot_vm_clocks
 * @see http://blog.csdn.net/yang_net/article/details/6056254
 * 
 * @author desheng.tu
 * @date 2015年7月23日 下午7:41:03
 *
 */
public class SystemTimer {

	private static ScheduledExecutorService scheduler = Executors
			.newSingleThreadScheduledExecutor(new DefaultThreadFactory());

	private volatile long now = System.currentTimeMillis();
	private volatile ScheduledFuture<?> task;

	/**
	 * 创建系统时间
	 * @param tick 时间更新周期
	 * @param unit 时间更新周期单位
	 */
	public SystemTimer(int tick, TimeUnit unit) {
		if (tick <= 0) {
			throw new IllegalArgumentException("tick 不能小于零  : " + tick);
		}

		task = scheduler.scheduleAtFixedRate(() -> {
			now = System.currentTimeMillis();
		}, 0, tick, unit);

		try {
			Thread.sleep(unit.toMillis(tick));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取不准确的当前时间
	 */
	public long now() {
		return now;
	}

	/**
	 * 修改系统时间更新频率
	 * @param tick 时间更新周期
	 * @param unit 时间更新周期单位
	 */
	public void update(int tick, TimeUnit unit) {
		if (tick <= 0) {
			throw new IllegalArgumentException("tick 不能小于零  : " + tick);
		}

		ScheduledFuture<?> tasknew = scheduler.scheduleAtFixedRate(() -> {
			now = System.currentTimeMillis();
		}, 0, tick, unit);

		task.cancel(true);

		task = tasknew;
	}

	/**
	 * 停止线程
	 */
	public static void stop() {
		scheduler.shutdown();

		try {
			scheduler.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	static class DefaultThreadFactory implements ThreadFactory {

		private static final AtomicInteger adder = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "systime-schedule-thread-" + adder.getAndIncrement());
		}
	}
}
