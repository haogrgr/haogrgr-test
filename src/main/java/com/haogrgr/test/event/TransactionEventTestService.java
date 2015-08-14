package com.haogrgr.test.event;

import javax.annotation.Resource;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haogrgr.test.dao.TestMapper;
import com.haogrgr.test.model.TestModel;

@Service
public class TransactionEventTestService {

	@Resource
	private TestMapper mapper;

	@Resource
	private ApplicationEventPublisher publisher;

	@Transactional
	public void addTestModel() {
		TestModel model = new TestModel();
		model.setName("haogrgr");
		mapper.insert(model);

		//如果model没有继承ApplicationEvent, 则内部会包装为PayloadApplicationEvent
		//对于@TransactionalEventListener, 会在事务提交后才执行Listener处理逻辑.
		//
		//发布事件, 事务提交后, 记录日志, 或发送消息等操作
		//注意这里不能使用线程池来执行event
		publisher.publishEvent(model);
	}
	//当事务提交后, 才会真正的执行@TransactionalEventListener配置的Listener, 如果Listener抛异常, 方法返回失败, 但事务不会回滚.

}
