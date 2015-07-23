package com.haogrgr.test.main;

import io.netty.util.HashedWheelTimer;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * netty中的WhellTimer实现
 * 
 * @author desheng.tu
 * @date 2015年7月23日 下午4:26:41 
 *
 */
public class WhellTimerTest {

	public static void main(String[] args) throws Exception {
		HashedWheelTimer timer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS, 4);

		System.out.println(LocalTime.now());

		timer.newTimeout((timeout) -> {
			System.out.println(LocalTime.now());
			System.out.println(timeout);
		}, 5, TimeUnit.SECONDS);

		System.in.read();
	}

}
