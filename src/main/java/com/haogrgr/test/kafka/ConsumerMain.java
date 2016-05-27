package com.haogrgr.test.kafka;

public class ConsumerMain {

	public static void main(String[] args) throws Exception {
		KafkaConsumerManager consumerManager = new KafkaConsumerManager("10.128.8.57:2181", "group-1", "haogrgr",
				new KafkaMessageCustomer() {
					@Override
					public void consume(int partition, long offset, String key, String msg) {
						System.out.println(msg);
						if (key.equals("5"))
							throw new RuntimeException("x");
					}
				});

		consumerManager.afterPropertiesSet();

		Thread.sleep(100000000l);

		consumerManager.shutdown();
	}

}
