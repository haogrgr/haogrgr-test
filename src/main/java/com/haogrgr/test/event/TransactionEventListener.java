package com.haogrgr.test.event;

import org.springframework.context.PayloadApplicationEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.haogrgr.test.model.TestModel;

@Component
public class TransactionEventListener {

	@TransactionalEventListener
	public void handle(PayloadApplicationEvent<TestModel> event) {
		System.err.println(event.getPayload().getName());
		//这里可以记录日志, 发送消息等操作.
		//这里抛出异常, 会导致addTestModel方法异常, 但不会回滚事务.
		//注意, ApplicationEventPublisher不能使用线程池, 否则不会执行到这里
		//因为, 包装类是通过ThreadLocal来判断当前是否有活动的事务信息.
		//TransactionalEventListener.fallbackExecution就是为了决定当当前线程没有事务上下文时, 
		//是否还调用 handle 方法, 默认不调用.
	}
}
