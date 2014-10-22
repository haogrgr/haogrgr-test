package com.haogrgr.test.server;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haogrgr.test.dao.BaseMapper;
import com.haogrgr.test.dao.TestMapper;
import com.haogrgr.test.model.TestModel;

@Service
public class TestService extends BaseServiceSupport<TestModel> {

	@Resource
	private TestMapper testMapper;
	
	@Transactional(propagation=Propagation.NEVER)
	public void testExp(){
		TestModel obj = new TestModel();;
		testMapper.insert(obj);
	}
	
	@Override
	public BaseMapper<TestModel> getMapper() {
		return testMapper;
	}

}
