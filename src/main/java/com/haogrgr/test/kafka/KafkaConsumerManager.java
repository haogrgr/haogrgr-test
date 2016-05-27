package com.haogrgr.test.kafka;

import java.util.HashMap;
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
	private int commitBatchSize = 30;
	private long commitIntervalMilliseconds = 5000l;

	private ConsumerConnector consumer;
	private KafkaStream<String, String> stream;
	private ExecutorService executor = Executors.newFixedThreadPool(1);

	private KafkaMessageCustomer handler; //消息处理类, 只有一个consume(key, value)方法.

	private Lock lock = new ReentrantLock();
	private volatile boolean started = false;
	private long incer = 0, time = System.currentTimeMillis();

	private Map<Integer, Long> offsetMap = new HashMap<>();

	public KafkaConsumerManager(String zkConnect, String group, String topic, KafkaMessageCustomer handler) {
		this.zkConnect = zkConnect;
		this.group = group;
		this.topic = topic;
		this.handler = handler;
	}

	@Override
	public synchronized void afterPropertiesSet() throws Exception {
		logger.info("初始化Kafka消费者: {}, {}, {}", zkConnect, group, topic);

		Assert.hasText(zkConnect);
		Assert.hasText(group);
		Assert.hasText(topic);
		Assert.notNull(executor);
		Assert.notNull(handler);

		if (started) {
			throw new IllegalArgumentException("不能多次调用afterPropertiesSet方法");
		}

		openKafkaStream();
		started = true;

		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					process();
				} catch (Throwable e) {
					logger.error("消费消息出错, 线程停止", e);
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
						commitOffset(info, true);//强制提交上次消费的位点
						break;
					}

					MessageAndMetadata<String, String> next = itr.next();
					String key = next.key(), value = next.message();
					info = "[" + next.partition() + ", " + next.offset() + ", " + key + "]";
					logger.info("收到Kafka消息: {}", info);

					//简单去重
					if (!checkDuplicateMsg(next.partition(), next.offset())) {
						boolean handleSuccOrShutdown = handleMessage(next.partition(), next.offset(), key, value, info);
						if (handleSuccOrShutdown) {//succ
							updateOffsetMap(next.partition(), next.offset());
						} else {//shutown
							return;
						}
					}

					commitOffset(info, false);
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
	 * 处理消息, 处理失败, 不断重试, 业务方法需幂等
	 * 
	 * @param key 消息键
	 * @param value 消息体
	 * @param info 日志信息
	 * @return true:处理成功, false:shutdown被调用了
	 */
	private boolean handleMessage(int partition, long offset, String key, String value, String info) {
		while (started) {
			try {
				handler.consume(partition, offset, key, value);
				return true;
			} catch (Throwable e) {
				logger.error("处理消息出错 : " + info, e);

				if (started) {
					try {
						TimeUnit.SECONDS.sleep(reOpenSeconds);
					} catch (InterruptedException ee) {
						logger.error("", ee);
						throw e;
					}
				}
				//retry anyway
			}
		}
		return false;
	}

	/**
	 * 手动提交消费位点
	 */
	private void commitOffset(String info, boolean force) {
		boolean commitBatch = incer++ % commitBatchSize == 0;
		boolean commitTime = (System.currentTimeMillis() - time) > commitIntervalMilliseconds;

		//(每10条消息  || 每间隔5秒   || 准备停止了[当started=false时, 表示已经调用shutdown方法了, 需要commit]) => 提交消费位点
		if (force || commitBatch || commitTime || !started) {
			consumer.commitOffsets(true);
			time = System.currentTimeMillis();
			logger.info("提交消费位点 : {}", info);
		}
	}

	/**
	 * 检查重复消费, 业务异常时, 会重新openKafkaStream, 导致重复消费, 这个时候可以通过offsetMap来过滤已经消费过的消息, 减少重复消费
	 * 
	 * @param partition 分区号
	 * @param offset 消费位点
	 * @return true:已经消费过
	 */
	private boolean checkDuplicateMsg(int partition, long offset) {
		Long oldOffset = offsetMap.get(partition);
		if (oldOffset != null && oldOffset.longValue() > offset) {
			return true;
		}
		return false;
	}

	/**
	 * 更新offsetMap到最新消费成功的offset
	 * 
	 * @param partition 分区号
	 * @param offset 消费位点
	 */
	private void updateOffsetMap(int partition, long offset) {
		offsetMap.put(partition, offset);
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

	public synchronized void shutdown() throws InterruptedException {
		logger.info("正在停止Kafka消费端");

		started = false;
		lock.lock();
		try {
			if (consumer != null) {
				consumer.shutdown();
				consumer = null;
			}
		} finally {
			lock.unlock();
		}

		if (executor != null) {
			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.SECONDS);
			executor = null;
		}
	}

}
