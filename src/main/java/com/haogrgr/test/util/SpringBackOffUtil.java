package com.haogrgr.test.util;

import org.springframework.util.backoff.BackOffExecution;
import org.springframework.util.backoff.ExponentialBackOff;

/**
 * 在如连接网络的应用中，网络是不稳定的有时候会连接断开，因此为了保证断开重连接；
 * 还有如系统之间互联，相互之间发生消息，如果某个服务器因为不确定因此连接不上，
 * 也需要断开重连；则需要一定的规则；常见的规则有：
 * 
 * 1、按照固定时间间隔重试，比如100毫秒；这种方式在网络不稳定时重连可能造成某一时间点流量同时发送，
 *    阻塞网络；或者造成发送一些无意义的请求；
 * 
 * 2、按照指数时间间隔重试，比如刚开始100毫秒，下一次200毫秒等；比如支付宝和第三方集成时就是类似方式。
 * 
 * 来源:http://jinnianshilongnian.iteye.com/blog/2103752
 * 
 * @author haogrgr
 */
public class SpringBackOffUtil {

	public static void main(String[] args) {
		ExponentialBackOff backOff = new ExponentialBackOff(100, 1.5);// 初始间隔,递增倍数(上次基础上)
		backOff.setMaxInterval(5 * 1000L);// 最大间隔(上一次和下一次最大间隔)
		backOff.setMaxElapsedTime(50 * 1000L);// 最大总时间间隔(第一次和最后一次间隔)

		BackOffExecution execution = backOff.start();
		for (int i = 1; i <= 18; i++) {
			System.out.println(execution.nextBackOff());
		}
		System.out.println(execution.nextBackOff());
	}

}
