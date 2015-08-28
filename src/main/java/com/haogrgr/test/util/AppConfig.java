package com.haogrgr.test.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.apache.commons.configuration.AbstractFileConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;

import com.google.common.collect.ImmutableMap;

/**
 * 应用配置熟悉类
 * 
 * @author desheng.tu
 * @date 2015年8月27日 下午3:37:48
 *
 */
public class AppConfig implements InitializingBean, ApplicationContextAware {

	private static Logger logger = LoggerFactory.getLogger(AppContextUtil.class);

	private static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	private Resource configLocation; //配置地址
	private Long refreshDelayMilliseconds = -1l; //默认不刷新
	private Map<String, String> defaultConfig = ImmutableMap.of();//默认配置项, 未获取到配置时的配置
	private PropertiesConfiguration config;

	private ApplicationContext context;//注入context, 用于发送修改通知事件
	private ScheduledFuture<?> watcher;//唉, 唉, 唉

	/**
	 * 获取指定的key值
	 */
	public String getString(String key) {
		String value = config.getString(key);
		if (value == null) {
			return defaultConfig.get(key);
		}
		return value;
	}

	/**
	 * 获取指定的key值, 没有则使用默认值
	 */
	public String getString(String key, String defaultValue) {
		return config.getString(key, defaultValue);
	}

	//set, afterPropertiesSet

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public void setRefreshDelayMilliseconds(Long refreshDelayMilliseconds) {
		this.refreshDelayMilliseconds = refreshDelayMilliseconds;
	}

	public void setDefaultConfig(Map<String, String> defaultConfig) {
		this.defaultConfig = defaultConfig;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Objects.requireNonNull(configLocation, "配置文件地址不能为空");

		this.config = new PropertiesConfiguration(configLocation.getURL());

		//配置刷新策略
		if (refreshDelayMilliseconds > 0) {
			FileChangedReloadingStrategy strategy = new FileChangedReloadingStrategy();
			strategy.setRefreshDelay(refreshDelayMilliseconds);
			config.setReloadingStrategy(strategy);
		}

		//发布配置重新加载事件
		config.addConfigurationListener(new ConfigurationListener() {
			@Override
			public void configurationChanged(ConfigurationEvent event) {
				if (event.getType() == AbstractFileConfiguration.EVENT_RELOAD && !event.isBeforeUpdate()) {
					context.publishEvent(new AppConfigReloadedEvent(AppConfig.this));
				}
			}
		});

		//apache common 配置刷新是lazy的, 所以需要主动去get以触发load, 蛋疼
		if (refreshDelayMilliseconds > 0) {
			watcher = scheduler.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					try{
						getString("test", "test");
					}catch(Throwable e){
						logger.warn("获取属性出错");
					}
				}
			}, refreshDelayMilliseconds, refreshDelayMilliseconds, TimeUnit.MILLISECONDS);
		}

		//日志
		logger.info("加载配置文件 : " + configLocation.getURL());
		if (logger.isDebugEnabled()) {
			Iterator<String> keys = config.getKeys();
			while (keys.hasNext()) {
				String key = keys.next();
				logger.debug("key: " + key + ", value: " + config.getProperty(key));
			}
		}
	}

	/**
	 * 测试, 及日志输出
	 */
	@EventListener
	public void onConfigReloaded(AppConfigReloadedEvent event) {
		try {
			logger.warn("重新加载配置文件 : " + configLocation.getURL());
			if (logger.isDebugEnabled()) {
				Iterator<String> keys = config.getKeys();
				while (keys.hasNext()) {
					String key = keys.next();
					logger.debug("key: " + key + ", value: " + config.getProperty(key));
				}
			}
		} catch (IOException e) {
			logger.error("重新加载配置文件 : 获取配置文件路径错误");
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

	@PreDestroy
	public void cancelWatcher() {
		if (watcher != null) {
			watcher.cancel(true);
			watcher = null;
		}
	}

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				scheduler.shutdownNow();
			}
		});
	}
}
