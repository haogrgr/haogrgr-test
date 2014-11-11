package com.haogrgr.test.main;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureTest {

	private static final Map<String, ManualFutureTask> tasks = new ConcurrentHashMap<String, ManualFutureTask>();

	public synchronized static void main(String[] args) throws Exception {
		Thread poster = new Thread(new Poster());
		System.out.println("开始支付...");
		poster.start();
		
		//Thread.sleep(5000);//模拟三方回调等待时间
		
		Thread notifyer = new Thread(new Notifyer());
		System.out.println("开始回调...");
		notifyer.start();
		
		Thread.sleep(5000);
	}

	public static final class Poster implements Runnable {
		public void run() {
			System.out.println("提交到三方支付平台...");
			ManualFutureTask task = new ManualFutureTask();
			tasks.put("订单号", task);
			try {
				String statuCode = task.get(10, TimeUnit.SECONDS);//阻塞等待结果
				System.out.println("支付完成,三方返回状态:" + statuCode);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				e.printStackTrace();
			}
			tasks.remove(task);
		}
	}

	public static final class Notifyer implements Runnable {
		@Override
		public void run() {
			ManualFutureTask task = tasks.get("订单号");//三方的回调里应该有订单号的吧.
			if(task != null){
				task.set("返回状态为:xxxxxxxxx");
			}
		}
	}

	public static class ManualFutureTask extends FutureTask<String> {
		private static final Callable<String> NULL = new Callable<String>() {public String call() throws Exception {return null;}};
		public ManualFutureTask() {
			super(NULL);
		}
		@Override
		public void set(String v) {
			super.set(v);
		}
	}
}
