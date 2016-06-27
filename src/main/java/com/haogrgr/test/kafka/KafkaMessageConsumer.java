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
public class KafkaMessageConsumer implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(KafkaMessageConsumer.class);

	private String zkConnect;
	private String group;
	private String topic;

	private int reOpenSeconds = 15;
	private int commitBatchSize = 30;
	private long commitIntervalMilliseconds = 5000l;

	private ConsumerConnector consumer;
	private KafkaStream<String, String> stream;
	private ExecutorService executor = Executors.newFixedThreadPool(1);

	private KafkaMessageHandler handler;

	private Lock lock = new ReentrantLock();
	private volatile boolean started = false;
	private long incer = 0, time = System.currentTimeMillis();

	private Map<Integer, Long> offsetMap = new HashMap<>();

	public KafkaMessageConsumer(String zkConnect, String group, String topic, KafkaMessageHandler handler) {
		this.zkConnect = zkConnect;
		this.group = group;
		this.topic = topic;
		this.handler = handler;
	}

	@Override
	public synchronized void afterPropertiesSet() throws Exception {
		logger.info("初始化Kafka消费者: {}, {}, {}", zkConnect, group, topic);

		verifyProperties();

		openKafkaStream();

		started = true;
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					process();
				} catch (Exception e) {
					logger.error("消费消息出错, 线程停止", e);
					throw e;
				}
			}
		});
	}

	/**
	 * 属性校验
	 */
	private void verifyProperties() {
		Assert.hasText(zkConnect);
		Assert.hasText(group);
		Assert.hasText(topic);
		Assert.notNull(executor);
		Assert.notNull(handler);
		Assert.state(!started, "不能多次调用afterPropertiesSet方法");
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

				String info = "";
				try {
					//已调用shutdown方法关闭, 则提交上次的消费位点, 并不处理当前消息, 尽早关闭
					if (!started) {
						commitOffset(info, true);
						break;
					}

					//获取消息
					MessageAndMetadata<String, String> next = itr.next();
					String key = next.key(), value = next.message();
					info = "[" + next.partition() + ", " + next.offset() + ", " + key + "]";
					logger.info("收到Kafka消息: {} {}", info, value);

					//简单去重 + 业务处理
					if (!checkDuplicateMsg(next.partition(), next.offset(), info)) {
						boolean handleSuccOrShutdown = handleMessage(next.partition(), next.offset(), key, value, info);
						if (handleSuccOrShutdown) {//handleSucc : 更新最新进度
							updateOffsetMap(next.partition(), next.offset());
						} else {//Shutdown : 退出
							return;
						}
					}

					//提交消费进度
					commitOffset(info, false);
				} catch (Exception e) {
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
	 * @return true:处理成功或者跳过处理, false:shutdown被调用了
	 */
	private boolean handleMessage(int partition, long offset, String key, String value, String info) {
		logger.info("准备处理kafka消息: {}", info);

		boolean accept = handler.accept(partition, offset, key, value);
		if (!accept) {
			return true;
		}

		int retryCount = 0;
		while (started) {
			retryCount++;
			try {
				handler.consume(partition, offset, key, value);
				return true;
			} catch (Exception e) {
				logger.error("处理消息出错 : " + info, e);

				if (retryCount == 3) {
					logger.info("消费重试三次仍然失败, 插入错误日志: {}", info);
					handler.handleError(partition, offset, key, value, e);
					return true;
				}

				sleepWithStartCheck(reOpenSeconds);
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
	private boolean checkDuplicateMsg(int partition, long offset, String info) {
		Long oldOffset = offsetMap.get(partition);
		if (oldOffset != null && oldOffset.longValue() > offset) {
			logger.info("忽略重复Kafka消息 : {}", info);
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
	 * 睡眠当前线程指定秒数, 每睡眠一秒检查一次启动状态, 防止shutdown时, 不必要的等待, 快速关闭
	 * 
	 * @param sleepSeconds sleep秒数
	 */
	private void sleepWithStartCheck(long sleepSeconds) {
		for (int i = 0; started && i < sleepSeconds; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ee) {
				logger.error("", ee);
			}
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

		sleepWithStartCheck(reOpenSeconds);

		if (started) {
			openKafkaStream();
		}
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
