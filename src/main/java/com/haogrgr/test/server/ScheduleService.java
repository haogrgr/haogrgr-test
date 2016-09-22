package com.haogrgr.test.server;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

	private AtomicInteger inc = new AtomicInteger();

	@Scheduled(fixedRate = 1000)
	public void test() {
		System.out.println(inc);
		if (inc.incrementAndGet() % 3 == 0) {
			System.out.println("exception");
			//throw new RuntimeException("schedule");
		}
	}

}
