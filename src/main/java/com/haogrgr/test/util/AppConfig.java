package com.haogrgr.test.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;

/**
 * 应用配置属性类, 配置刷新时会向Spring发送AppConfigReloadedEvent事件.
 * 
 * @author desheng.tu
 * @date 2015年8月27日 下午3:37:48
 *
 */
public class AppConfig implements ApplicationContextAware, InitializingBean {

	private static Logger logger = LoggerFactory.getLogger(AppConfig.class);

	private volatile Map<String, String> config;

	private Resource configLocation; //配置文件
	private Integer refreshDelaySeconds = 180; //默认180秒刷新
	private final Lock lock = new ReentrantLock();

	private ApplicationContext context;
	private ScheduledExecutorService scheduler;

	private boolean setScheduler = false; //外部的scheduler由外部关闭
	private ScheduledFuture<?> watcher;

	public String getStr(String key) {
		String val = getValue(key);
		return val;
	}

	public String getStr(String key, String defaultValue) {
		String val = getValue(key);
		return val != null ? val : defaultValue;
	}

	public Integer getInt(String key) {
		String val = getValue(key);
		return val == null ? null : Integer.valueOf(val);
	}

	public Integer getInt(String key, Integer defaultValue) {
		String val = getValue(key);
		return val == null ? defaultValue : Integer.valueOf(val);
	}

	//key不为null判断
	private String getValue(String key) {
		Objects.requireNonNull(key, "key不能为null");
		return config.get(key);
	}

	//set, afterPropertiesSet

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public void setRefreshDelaySeconds(Integer refreshDelaySeconds) {
		this.refreshDelaySeconds = refreshDelaySeconds;
	}

	public void setScheduler(ScheduledExecutorService scheduler) {
		this.scheduler = scheduler;
		this.setScheduler = true;//谁创建, 谁关闭
	}

	//spring

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Objects.requireNonNull(configLocation, "配置文件地址不能为空");
		Objects.requireNonNull(configLocation.getFile(), "配置文件不能为空");

		//初始化properties
		loadConfig();

		//自动刷新检查定时任务
		if (refreshDelaySeconds > 0) {
			if (scheduler == null) {
				scheduler = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory());
			}

			watcher = scheduler.scheduleAtFixedRate(new ConfigMonitorTask(this, configLocation.getFile()),
					refreshDelaySeconds, refreshDelaySeconds, TimeUnit.SECONDS);
		}

		printDebugLog();
	}

	//从properties配置文件中加载配置
	private void loadConfig() throws IOException {
		Properties props = new Properties();
		InputStream input = configLocation.getInputStream();
		try {
			props.load(input);
		} finally {
			input.close();
		}

		HashMap<String, String> map = new HashMap<>(props.size());
		for (Map.Entry<Object, Object> entry : props.entrySet()) {
			Objects.requireNonNull(entry.getValue());
			map.put((String) entry.getKey(), (String) entry.getValue());
		}

		this.config = map;
	}

	/**
	 * 输出日志输出
	 */
	@EventListener
	public void onConfigReloaded(AppConfigReloadedEvent event) {
		printDebugLog();
	}

	/**
	 * debug输出配置
	 */
	private void printDebugLog() {
		try {
			logger.info("加载配置文件 : " + configLocation.getURL());
			if (logger.isDebugEnabled()) {
				StringBuilder sb = new StringBuilder();
				for (Entry<String, String> entry : config.entrySet()) {
					sb.append("\nkey: ").append(entry.getKey()).append(", value: ").append(entry.getValue());
				}
				logger.debug(sb.toString());
			}
		} catch (IOException e) {
			logger.error("重新加载配置文件 : 获取配置文件路径错误");
		}
	}

	@PreDestroy
	public void cancelWatcher() {
		if (watcher != null) {
			watcher.cancel(true);
			watcher = null;
		}

		if (scheduler != null && setScheduler) {
			scheduler.shutdownNow();
		}
	}

	//自定义线程工厂
	private static class NamedThreadFactory implements ThreadFactory {

		private AtomicInteger inc = new AtomicInteger(0);

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "appconfig-" + inc.getAndIncrement());
		}

	}

	//定时任务, 根据文件修改时间, 刷新配置
	private static class ConfigMonitorTask implements Runnable {

		private final AppConfig config;
		private final File file;
		private volatile long lastModified;

		public ConfigMonitorTask(AppConfig config, File file) {
			this.config = config;
			this.file = file;
			this.lastModified = file.lastModified();
		}

		@Override
		public void run() {
			config.lock.lock();
			try {
				final long currentLastModified = file.lastModified();
				if (currentLastModified <= lastModified) {
					return;
				}

				lastModified = currentLastModified;

				config.loadConfig();

				//发布配置重新加载
				config.context.publishEvent(new AppConfigReloadedEvent(config));
			} catch (IOException e) {
				logger.error("重新加载配置文件异常", e);
			} finally {
				config.lock.unlock();
			}
		}
	}

	/**
	 * 配置重新加载完成事件
	 */
	public static class AppConfigReloadedEvent extends ApplicationEvent {

		private static final long serialVersionUID = 1L;

		public AppConfigReloadedEvent(AppConfig config) {
			super(config);
		}

		public AppConfig getAppConfig() {
			return (AppConfig) this.getSource();
		}
	}

}
