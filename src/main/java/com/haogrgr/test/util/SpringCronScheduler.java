package com.haogrgr.test.util;

import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

public class SpringCronScheduler {

	public static void main(String[] args) throws Exception {
		ConcurrentTaskScheduler scheduler =  new ConcurrentTaskScheduler();
		scheduler.schedule(new Runnable() {
			@Override
			public void run() {
				System.out.println("test");
			}
		}, new CronTrigger("0/1 * * * * ?"));//3秒一次
		
		System.in.read();
	}

}
