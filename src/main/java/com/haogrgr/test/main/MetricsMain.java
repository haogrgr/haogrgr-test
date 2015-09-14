package com.haogrgr.test.main;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.haogrgr.test.util.RandomUtil;

/**
 * io.dropwizard.metrics的前身是com.codahale.metrics
 * 
 * Java的应用韵律
 * 
 * @see http://metrics.dropwizard.io/3.1.0/getting-started/
 * 
 * @author desheng.tu
 * @date 2015年9月14日 下午5:34:40
 *
 */
public class MetricsMain {

	static final MetricRegistry metrics = new MetricRegistry();

	public static void main(String args[]) {
		startReport();
		Meter requests = metrics.meter("requests");
		for (int i = 0; i < 1000; i++) {
			requests.mark();
			waitRandSeconds();
		}
	}

	static void startReport() {
		ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS).build();
		reporter.start(5, TimeUnit.SECONDS);
	}

	static void waitRandSeconds() {
		try {
			Thread.sleep(RandomUtil.random(1000));
		} catch (InterruptedException e) {
		}
	}

}
