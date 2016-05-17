package com.haogrgr.test.kafka;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.haogrgr.test.util.Maps;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;

/**
 * kafka消费类, 负责初始化链接, 调用业务方法消费消息
 * 
 * @author tudesheng
 * @since 2016年5月15日 下午9:23:18
 *
 */
public class KafkaConsumerManager implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerManager.class);

	private String zkConnect;
	private String group;
	private String topic;
	private int reOpenSeconds = 15;
	private long commitIntervalMilliseconds = 3000l;

	private ExecutorService executor = Executors.newFixedThreadPool(1);
	private ConsumerConnector consumer;
	private KafkaStream<String, String> stream;

	private KafkaMessageCustomer handler;

	private Lock lock = new ReentrantLock();
	private volatile boolean started = false;
	private long incer = 0, time = System.currentTimeMillis();

	public KafkaConsumerManager(String zkConnect, String group, String topic, KafkaMessageCustomer handler) {
		this.zkConnect = zkConnect;
		this.group = group;
		this.topic = topic;
		this.handler = handler;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("初始化Kafka消费者: {}, {}, {}", zkConnect, group, topic);

		Assert.hasText(zkConnect);
		Assert.hasText(group);
		Assert.hasText(topic);
		Assert.notNull(handler);

		openKafkaStream();
		started = true;

		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					process();
				} catch (Throwable e) {
					logger.error("消费消息出错", e);
					throw e;
				}
			}
		});
	}

	/**
	 * 消费Kafka消息
	 */
	private void process() {
		while (started) {
			logger.info("开始消费Kafka消息 : {}", stream);

			ConsumerIterator<String, String> itr = stream.iterator();
			while (itr.hasNext()) {
				lock.lock();

				String info = null;
				try {
					//这里先lock再判断started, 而shutdown方法是先设置started再lock, 防止处理到一半就shutdown
					if (!started) {
						break;
					}

					MessageAndMetadata<String, String> next = itr.next();
					String key = next.key(), value = next.message();
					info = "[" + next.partition() + ", " + next.offset() + "] [" + key + ", " + value + "]";
					logger.info("收到Kafka消息: {}", info);

					//业务处理, 业务需要实现幂等
					handler.consume(key, value);

					commit(next);
				} catch (Throwable e) {
					logger.error("处理消息出错 : " + info, e);

					//重新初始化客户端, 重新消费
					if (started) {
						reOpenKafkaStream();
					}
				} finally {
					lock.unlock();
				}
			}
		}
	}

	/**
	 * 手动提交消费位点
	 */
	private void commit(MessageAndMetadata<String, String> msg) {
		//(每10条消息  || 每间隔5秒   || 准备停止了[当started=false时, 表示已经调用shutdown方法了, 需要commit]) => 提交消费位点
		if (incer++ % 10 == 0 || (System.currentTimeMillis() - time) > commitIntervalMilliseconds || !started) {
			consumer.commitOffsets(true);
			logger.info("提交消费位点 : " + "[" + msg.partition() + ", " + msg.offset() + ", " + msg.key() + "]");

			time = System.currentTimeMillis();
		}
	}

	/**
	 * 错误重试逻辑 : 关闭现有链接, 等待一定时间后, 重新初始化链接, 以便重新消费消息
	 */
	private void reOpenKafkaStream() {
		logger.info("关闭消费客户端, 等待{}秒后, 重新初始化消费", reOpenSeconds);

		if (consumer != null) {
			consumer.shutdown();
		}

		try {
			TimeUnit.SECONDS.sleep(reOpenSeconds);
		} catch (InterruptedException e) {
			logger.error("", e);
		}

		openKafkaStream();
	}

	/**
	 * 初始化Kafka消费者客户端, 并获取Topic对应的Stream
	 */
	private void openKafkaStream() {
		logger.info("开始初始化Kafka消费客户端");

		this.consumer = Consumer.createJavaConsumerConnector(getConsumerConfig());

		StringDecoder decoder = new StringDecoder(null);
		Map<String, Integer> topicCountMap = Maps.of(topic, 1);
		Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap,
				decoder, decoder);

		List<KafkaStream<String, String>> streams = consumerMap.get(topic);
		this.stream = streams.get(0);

		Assert.notNull(stream);
	}

	/**
	 * 获取Kafka客户端配置类
	 */
	private ConsumerConfig getConsumerConfig() {
		Properties props = new Properties();
		props.put("zookeeper.connect", zkConnect);
		props.put("group.id", group);
		props.put("zookeeper.session.timeout.ms", "3000");
		props.put("zookeeper.sync.time.ms", "2000");
		props.put("auto.commit.interval.ms", "1000");
		props.put("auto.commit.enable", "false");
		return new ConsumerConfig(props);
	}

	public void shutdown() throws InterruptedException {
		logger.info("正在停止Kafka消费端");

		started = false;
		lock.lock();
		try {
			consumer.shutdown();
		} finally {
			lock.unlock();
		}

		executor.shutdown();
		executor.awaitTermination(5, TimeUnit.SECONDS);
	}

}
