package com.haogrgr.test.kafka;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.haogrgr.test.util.Maps;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;

public class ConsumerMain {

	public static void main(String[] args) {
		startConsumer("10.128.8.57:2181", "group-1", "haogrgr");
	}

	//消息处理类
	private static class ConsumerProcessTask implements Runnable {

		private ConsumerConnector consumer;
		private KafkaStream<String, String> stream;

		public ConsumerProcessTask(ConsumerConnector consumer, KafkaStream<String, String> stream) {
			this.consumer = consumer;
			this.stream = stream;
		}

		@Override
		public void run() {
			ConsumerIterator<String, String> itr = stream.iterator();
			while (itr.hasNext()) {
				try {
					MessageAndMetadata<String, String> next = itr.next();

					String msg = next.message();
					System.err.println(next.key() + " : " + msg);
					
					//这里业务处理
					
				} catch (Throwable e) {
					e.printStackTrace();
					//这里想sleep一段时间后, 重新消费
				}
			}
		}
	}

	//初始化customer实例
	public static ConsumerConnector getConsumer(String zkConnect, String groupId) {
		Properties props = new Properties();
		props.put("zookeeper.connect", zkConnect);
		props.put("group.id", groupId);
		props.put("zookeeper.session.timeout.ms", "3000");
		props.put("zookeeper.sync.time.ms", "2000");
		props.put("auto.commit.interval.ms", "1000");
		props.put("auto.commit.enable", "false");
		ConsumerConfig conf = new ConsumerConfig(props);
		return Consumer.createJavaConsumerConnector(conf);
	}

	public static void startConsumer(String zkConnect, String group, String topic) {
		ConsumerConnector consumer = getConsumer(zkConnect, group);

		StringDecoder decoder = new StringDecoder(null);
		Map<String, Integer> topicCountMap = Maps.of(topic, 1);
		Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap,
				decoder, decoder);

		ExecutorService executor = Executors.newFixedThreadPool(1);

		List<KafkaStream<String, String>> streams = consumerMap.get(topic);
		for (KafkaStream<String, String> stream : streams) {
			executor.submit(new ConsumerProcessTask(consumer, stream));
		}
	}

	public static void destory(ConsumerConnector consumer, ExecutorService executor) {
		if (consumer != null) {
			consumer.shutdown();
		}
		if (executor != null) {
			executor.shutdown();
		}
	}
}
