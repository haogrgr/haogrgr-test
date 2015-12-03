package com.haogrgr.test.server;

import com.haogrgr.test.dao.BaseMapper;
import com.haogrgr.test.dao.TestMapper;
import com.haogrgr.test.model.TestModel;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl extends BaseServiceSupport<TestModel, Integer> implements TestService {

    @Resource
    private TestMapper testMapper;

    @Override
    public BaseMapper<TestModel, Integer> getMapper() {
        return testMapper;
    }
}