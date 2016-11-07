package com.haogrgr.test.main;

import java.util.ArrayList;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.haogrgr.test.dao.TestMapper;
import com.haogrgr.test.model.TestModel;
import com.haogrgr.test.util.Lists;

public class Temp {

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-root.xml");
		TestMapper bean = context.getBean(TestMapper.class);

		ArrayList<TestModel> array = Lists.array(new TestModel().setName("hehe"), new TestModel().setName("hehe"));
		bean.saveBatch(array);

		for (TestModel testModel : array) {
			System.out.println(testModel.getId());
		}

		context.close();
	}

}
