package com.haogrgr.test.kafka;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class ProducerMain {

	public static void main(String[] args) throws Exception {
		Producer<String, String> producer = getProducer("localhost:9092");

		for (int i = 0; i < 50000; i++) {
			sendMsg(producer, "haogrgr", i + "", i + "");
			//Thread.sleep(10);
		}

		close(producer);
	}

	//发送消息到MQ
	public static void sendMsg(Producer<String, String> producer, String topic, String key, String value) {
		KeyedMessage<String, String> data = new KeyedMessage<>(topic, key, value);
		producer.send(data);
	}

	//获取kafka配置
	public static Producer<String, String> getProducer(String brokers) {
		Properties props = new Properties();
		props.put("metadata.broker.list", brokers);
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("key.serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "-1");
		ProducerConfig config = new ProducerConfig(props);
		Producer<String, String> producer = new Producer<>(config);
		return producer;
	}

	public static void close(Producer<?, ?> producer) throws InterruptedException {
		Thread.sleep(500);
		producer.close();
		Thread.sleep(500);
	}

}
