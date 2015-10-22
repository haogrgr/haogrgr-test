package com.haogrgr.test.main;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

/**
 * 使用Spring的类扫描并获取bean定义.
 */
@SpringBeanDefFindTestAnno("test")
public class SpringBeanDefFindTest {

	public static void main(String[] args) {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new TestTypeFilter());

		Set<BeanDefinition> beans = provider.findCandidateComponents("com.haogrgr.test.main");

		for (BeanDefinition bdf : beans) {
			System.out.println(bdf.getBeanClassName());
		}
	}
	
	public static final class TestTypeFilter implements TypeFilter {
		@Override
		public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
			return metadataReader.getAnnotationMetadata().isAnnotated("com.haogrgr.test.main.SpringBeanDefFindTestAnno");
		}
	}
	
}

