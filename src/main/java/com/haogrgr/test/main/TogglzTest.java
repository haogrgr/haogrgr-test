//package com.haogrgr.test.main;
//
//import org.togglz.core.Feature;
//import org.togglz.core.activation.Parameter;
//import org.togglz.core.activation.ParameterBuilder;
//import org.togglz.core.annotation.EnabledByDefault;
//import org.togglz.core.annotation.Label;
//import org.togglz.core.context.FeatureContext;
//import org.togglz.core.context.StaticFeatureManagerProvider;
//import org.togglz.core.manager.FeatureManager;
//import org.togglz.core.manager.FeatureManagerBuilder;
//import org.togglz.core.manager.TogglzConfig;
//import org.togglz.core.repository.FeatureState;
//import org.togglz.core.repository.StateRepository;
//import org.togglz.core.repository.mem.InMemoryStateRepository;
//import org.togglz.core.spi.ActivationStrategy;
//import org.togglz.core.user.FeatureUser;
//import org.togglz.core.user.UserProvider;
//import org.togglz.core.user.thread.ThreadLocalUserProvider;
//
//import com.google.common.base.Splitter;
//import com.google.common.base.Strings;
//
///**
// * Togglz 简单使用
// * 
// * Togglz is an implementation of the Feature Toggles pattern for Java. 
// * Feature Toggles are a very common agile development practices in the context of continuous deployment and delivery. 
// * The basic idea is to associate a toggle with each new feature you are working on. 
// * This allows you to enable or disable these features at application runtime, even for individual users.
// * 
// * @see http://abhishek-tiwari.com/post/decoupling-deployment-and-release-feature-toggles
// * @see http://www.togglz.org/#example
// * 
// * @author desheng.tu
// * @date 2015年12月22日 上午11:00:06 
// *
// */
//public class TogglzTest {
//
//	public static void main(String[] args) {
//		System.out.println(ADFeatures.AD_BOTTOM.name());
//
//		//添加名为uname的策略
//		FeatureManager featureManager = new FeatureManagerBuilder().activationStrategy(new ADUNameActivationStrategy())
//				.togglzConfig(new ADFeatureConfigs()).build();
//		StaticFeatureManagerProvider.setFeatureManager(featureManager);
//
//		//没有FeatureUser时, 策略ADUNameActivationStrategy不满足, 所以都返回false
//		System.out.println(ADFeatures.AD_TOP.isActive());
//		System.out.println(ADFeatures.AD_BOTTOM.isActive());
//
//		printFeatures("user1");
//		printFeatures("user2");
//		printFeatures("user3");
//		printFeatures("user4");
//
//	}
//
//	public static void printFeatures(String uname) {
//		System.out.println("set current user : " + uname);
//
//		ThreadLocalUserProvider.bind(new User(uname));
//		System.out.println(ADFeatures.AD_TOP.isActive());
//		System.out.println(ADFeatures.AD_BOTTOM.isActive());
//		ThreadLocalUserProvider.release();
//	}
//
//	public enum ADFeatures implements Feature {
//
//		@EnabledByDefault
//		@Label("头部广告")
//		AD_TOP,
//
//		@Label("底部广告")
//		AD_BOTTOM;
//
//		public boolean isActive() {
//			return FeatureContext.getFeatureManager().isActive(this);
//		}
//
//	}
//
//	public static class ADUNameActivationStrategy implements ActivationStrategy {
//
//		private Splitter splitter = Splitter.on(",").trimResults();
//
//		@Override
//		public String getId() {
//			return ADUNameActivationStrategy.class.getName();
//		}
//
//		@Override
//		public String getName() {
//			return "ADFeatures的用户名策略";
//		}
//
//		@Override
//		public boolean isActive(FeatureState featureState, FeatureUser user) {
//			String users = featureState.getParameter("users");
//			if (Strings.isNullOrEmpty(users) || user == null) {
//				return false;
//			}
//
//			return splitter.splitToList(users).stream().anyMatch(uname -> uname.equals(user.getName()));
//		}
//
//		/**
//		 * 应该后面管理界面配置时的说明
//		 */
//		@Override
//		public Parameter[] getParameters() {
//			return new Parameter[] { ParameterBuilder.create("uname").label("用户名列表").description("用户名列表, 以英文逗号分隔") };
//		}
//
//	}
//
//	public static class User implements FeatureUser {
//
//		private String name;
//
//		public User(String name) {
//			this.name = name;
//		}
//
//		@Override
//		public String getName() {
//			return name;
//		}
//
//		@Override
//		public boolean isFeatureAdmin() {
//			return false;
//		}
//
//		@Override
//		public Object getAttribute(String name) {
//			return null;
//		}
//
//	}
//
//	public static class ADFeatureConfigs implements TogglzConfig {
//
//		@Override
//		public Class<? extends Feature> getFeatureClass() {
//			return ADFeatures.class;
//		}
//
//		@Override
//		public StateRepository getStateRepository() {
//			InMemoryStateRepository repository = new InMemoryStateRepository();
//
//			//特性一: 对用户名为user1, user2的用户显示头部广告
//			//只有enabled为true, 才会走后门的策略判断, 无策略返回true
//			FeatureState top = new FeatureState(ADFeatures.AD_TOP, true);
//			top.setStrategyId(ADUNameActivationStrategy.class.getName());
//			top.setParameter("users", "user1,user2");
//			repository.setFeatureState(top);
//
//			//特性二: 对用户名为user2, user3的用户显示底部广告
//			FeatureState bottom = new FeatureState(ADFeatures.AD_BOTTOM, true);
//			bottom.setStrategyId(ADUNameActivationStrategy.class.getName());
//			bottom.setParameter("users", "user2,user3");
//			repository.setFeatureState(bottom);
//
//			//结果就是user1会显示头部广告, user2显示头部底部广告, user3显示底部广告, 其他用户不显示广告
//			return repository;
//		}
//
//		@Override
//		public UserProvider getUserProvider() {
//			return new ThreadLocalUserProvider();
//		}
//
//	}
//
//}
