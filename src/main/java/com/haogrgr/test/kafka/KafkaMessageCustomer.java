package com.haogrgr.test.kafka;

public interface KafkaMessageCustomer {

	public void consume(String key, String msg);

}
