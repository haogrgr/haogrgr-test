package com.haogrgr.test.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class SpringConditionTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ConfigA.class, ConfigB.class);
		System.out.println(ctx.containsBeanDefinition("bean1"));//true
		System.out.println(ctx.containsBeanDefinition("bean2"));//false
		ctx.close();
	}

	@Configuration
	public static class ConfigA {

		@Bean
		public JustABean bean1() {
			return new JustABean();
		}

		@Bean
		public JustABean springConditionTest() {
			return new JustABean();
		}

	}

	@Configuration
	@SpringConditionTestAnno(SpringConditionTest.class)
	public static class ConfigB {

		@Bean
		public JustABean bean2() {
			return new JustABean();
		}

	}

	public static class JustABean {}

}
