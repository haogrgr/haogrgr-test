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

public class SimpleKafkaConsumer {

	private static ConsumerConnector consumer = null;

	public static void main(String[] args) {
		List<KafkaStream<String, String>> streams = openKafkaStream("10.128.8.57:2181", "group-1", "haogrgr1");

		ExecutorService exec = Executors.newFixedThreadPool(1);

		int id = 0;

		for (KafkaStream<String, String> stream : streams) {
			final int theid = id++;
			exec.execute(new Runnable() {
				@Override
				public void run() {
					ConsumerIterator<String, String> itr = stream.iterator();
					while (itr.hasNext()) {
						MessageAndMetadata<String, String> next = itr.next();
						System.out.println(next.key() + ", " + next.partition() + ", " + next.offset() + ", " + theid);

						consumer.commitOffsets(true);
					}
				}
			});
		}

	}

	private static List<KafkaStream<String, String>> openKafkaStream(String zkConnect, String group, String topic) {
		consumer = Consumer.createJavaConsumerConnector(getConsumerConfig(zkConnect, group));

		StringDecoder decoder = new StringDecoder(null);
		Map<String, Integer> topicCountMap = Maps.of(topic, 1);
		Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap,
				decoder, decoder);

		return consumerMap.get(topic);
	}

	private static ConsumerConfig getConsumerConfig(String zkConnect, String group) {
		Properties props = new Properties();
		props.put("zookeeper.connect", zkConnect);
		props.put("group.id", group);
		props.put("zookeeper.session.timeout.ms", "3000");
		props.put("zookeeper.sync.time.ms", "2000");
		props.put("auto.commit.interval.ms", "1000");
		props.put("auto.commit.enable", "false");
		return new ConsumerConfig(props);
	}
}
