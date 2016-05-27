package com.haogrgr.test.kafka;

/**
 * kafka消费处理类
 * 
 * @author tudesheng
 * @since 2016年5月27日 下午1:58:18
 *
 */
public interface KafkaMessageCustomer {

	/**
	 * 消费kafka消息
	 * 
	 * @param partition 分区号
	 * @param offset 当前消费位点
	 * @param key 消息键
	 * @param msg 消息内容
	 */
	public void consume(int partition, long offset, String key, String msg);

}
