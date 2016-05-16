package com.haogrgr.test.kafka;

public class ConsumerMain {

	public static void main(String[] args) throws Exception {
		KafkaConsumerManager consumerManager = new KafkaConsumerManager("10.128.8.57:2181", "group-1", "haogrgr",
				new KafkaMessageCustomer() {
					@Override
					public void consume(String key, String msg) {
						System.out.println(msg);
					}
				});

		consumerManager.afterPropertiesSet();

		Thread.sleep(100000);

		consumerManager.shutdown();
	}

}
