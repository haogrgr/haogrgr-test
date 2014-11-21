package com.haogrgr.test.server;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haogrgr.test.dao.BaseMapper;
import com.haogrgr.test.dao.TestMapper;
import com.haogrgr.test.model.TestModel;

@Service
public class TestServiceImpl extends BaseServiceSupport<TestModel> implements TestService {

	@Resource
	private TestMapper testMapper;

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public void testExp() {
		TestModel obj = new TestModel();
		testMapper.insert(obj);
	}

	@Override
	public BaseMapper<TestModel> getMapper() {
		return testMapper;
	}

}
