package com.haogrgr.test.util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentExec {
	
	public static void main(String[] args) throws Exception {
		final AtomicInteger inc = new AtomicInteger(1);
		
		System.out.println(TestUtils.getTimeStr());
		List<Future<Integer>> re = ConcurrentExec.exec(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				int s = inc.getAndIncrement();
				Thread.sleep(s * 100);
				return s;
			}
		}, 10, 500);
		System.out.println(TestUtils.getTimeStr());
		
		for (Future<Integer> future : re) {
			try{
				System.out.println(future.get());
			}catch(Exception e){
				System.out.println(e.getCause());
			}
		}
	}
	
	/**
	 * 并发执行指定任务
	 * 通过future.isDone轮询任务结果,直到所有任务都完成,或者超时
	 * 
	 * @param task 要并发执行的任务
	 * @param concurrentCallCount 并发执行线程数
	 * @param timeoutMillis 超时时间, 小于等于0则永不超时
	 * @return 完成后的任务结果
	 */
	public static <T> List<Future<T>> exec(Callable<T> task, int concurrentCallCount, int timeoutMillis) {
		if (task == null) {
			throw new NullPointerException("任务不能为空 : " + task);
		}
		if (concurrentCallCount < 1) {
			throw new IllegalArgumentException("并行执行次数不能小于1 : " + concurrentCallCount);
		}

		List<Future<T>> tasks = new ArrayList<>(concurrentCallCount);
		final ExecutorService execer = Executors.newFixedThreadPool(concurrentCallCount);
		
		try{
			for (int i = 0; i < concurrentCallCount; i++) {
				tasks.add(execer.submit(task));
			}
	
			int count = 0;
			BitSet mark = new BitSet(concurrentCallCount);
			Future<T> unexectued = tasks.get(tasks.size() - 1);
			long deadline = timeoutMillis > 0 ? System.currentTimeMillis() + timeoutMillis : 0l; //计算超时时间点, 0不超时
			while (true) {
				try {
					unexectued.get(10, TimeUnit.MILLISECONDS);//先阻塞获取最后一个任务结果, 这里10可以不用写死,弄成衰减
				} catch (InterruptedException | ExecutionException | TimeoutException e) {
					//ignore
				}
	
				for (int i = 0; i < concurrentCallCount && !mark.get(i); i++) {
					Future<T> future = tasks.get(i);
					if (future.isDone()) {
						count++; mark.set(i);//标记为已完成
					} else {
						unexectued = future;
					}
				}
	
				//结果在超时前成功获取,退出循环
				if (count == concurrentCallCount) {
					break;
				}
	
				//超时
				if (deadline != 0 && System.currentTimeMillis() > deadline) {
					break;
				}
			}
			
			for (int i = 0; i < concurrentCallCount && !mark.get(i); i++) {
				tasks.get(i).cancel(true);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			execer.shutdownNow();
		}
		
		return tasks;
	}

}