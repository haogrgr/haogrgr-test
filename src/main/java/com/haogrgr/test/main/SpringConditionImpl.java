package com.haogrgr.test.main;

import java.beans.Introspector;
import java.util.List;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.MultiValueMap;

/**
 * 当包含BeanFactory中包含SpringConditionTestAnno.value指定的BeanDefinition时, 不初始化
 * 
 * @author tudesheng
 * @since 2016年5月18日 下午5:15:07
 *
 */
public class SpringConditionImpl implements Condition{//ConfigurationCondition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		MultiValueMap<String, Object> attr = metadata
				.getAllAnnotationAttributes(SpringConditionTestAnno.class.getName());

		List<Object> list = attr.get("value");
		Class<?> clazz = (Class<?>) list.get(0);

		String beanName = Introspector.decapitalize(ClassUtils.getShortName(clazz));
		if (context.getRegistry().containsBeanDefinition(beanName)) {
			//or you can remove it
			//context.getRegistry().removeBeanDefinition(beanName);
			return false;
		}

		return true;
	}

//	@Override
//	public ConfigurationPhase getConfigurationPhase() {
//		return ConfigurationPhase.PARSE_CONFIGURATION;
//	}
}
