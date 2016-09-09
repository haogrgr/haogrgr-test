package com.haogrgr.test.juc;

import java.util.concurrent.locks.AbstractOwnableSynchronizer;
import java.util.concurrent.locks.LockSupport;

import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public class MyAqs extends AbstractOwnableSynchronizer {

	private static final long serialVersionUID = 1L;

	static final long spinForTimeoutThreshold = 1000L;

	private transient volatile Node head;
	private transient volatile Node tail;
	private volatile int state;

	//将节点加入到链表尾部
	private Node enq(final Node node) {
		for (;;) {
			Node t = tail;
			if (t == null) {
				//tail为空, 补上tail/head, 进入下次循环, 将node设置为新的tail
				//因为acquire时, 如果直接tryacquire成功了, 则不会加入链表, 所以这里补上.
				if (compareAndSetHead(new Node())) {
					tail = head;
				}
			}
			else {
				//1.设置node.prev, 2.cas设置node为tail, 3.成功, 再设置t.next
				//所以, 入队成功(2操作完), 则prev一定被设置, 但是可能3还没有执行, 所以prev.next可能为null
				//所以, 后面可以看到有些地方并不是.next遍历下去, 而是.prev遍历下去, 防止漏掉已入队的节点
				node.prev = t;
				if (compareAndSetTail(t, node)) {
					t.next = node;
					return t;
				}
			}
		}
	}

	//添加指定模式(mode)的等待节点到链表尾部
	private Node addWaiter(Node mode) {
		//待添加的节点, mode为独占(EXCLUSIVE)or共享(SHARED), 初始状态为0.
		Node node = new Node(Thread.currentThread(), mode);

		//与enq逻辑类似, 链表不为空时, cas设置tail, 失败再走enq
		Node pred = tail;
		if (pred != null) {
			node.prev = pred;
			if (compareAndSetTail(pred, node)) {
				pred.next = node;
				return node;
			}
		}

		//enq就多了个判断tail为空是, new已个Node放进去, 然后在吧node加入链表
		enq(node);

		return node;
	}

	//唤醒node后继的有效节点, 被cancelAcquire, doReleaseShared, release方法调用.
	private void unparkSuccessor(Node node) {

		//走cancelAcquire来到这里的话, 就不改状态为0, 因为一旦node进入到cancel就不会进入其他状态了
		//为0状态的话, 就不需要修改状态了, 设置为0状态, 是为了表示, 走到这里已经要unpark后续节点了, 清除signel状态
		//这里修改失败, 不需要管, 什么时候会失败, cancelAcquire不会修改, release时, 状态为0不需要修改, 状态为Signal, 后面的线程也不会修改head状态
		//所以这里只有doReleaseShared时, 可能出现修改失败的情况, TODO
		int ws = node.waitStatus;
		if (ws < 0) {
			compareAndSetWaitStatus(node, ws, 0);
		}

		//找到有效的next, 并unpark
		Node tobeUnpark = node.next;
		if (tobeUnpark == null || tobeUnpark.waitStatus > 0) {
			//next为空或无效, 从尾到头遍历, 直到碰到最前的有效状态的节点
			//这里不从头开始遍历是因为: 从头开始的话, 就是t.next一路遍历下去
			//但是AQS是LCH的变种, next是优化, 不是原子性更新的, 不可靠
			//见Doug Lea大师的论文: http://ifeve.com/aqs-2/ (3.3 队列一节)
			//next链接仅是一种优化。如果通过某个节点的next字段发现其后继结点不存在（或看似被取消了），
			//总是可以使用pred字段从尾部开始向前遍历来检查是否真的有后续节点。
			//MCS队列和LCH队列的区别是: MCS是通过.next属性来链表, LCH是.prev属性, 
			//则入队的时候LCH只需要先修改自己的prev, 在cas更新tail就好, 而MCS就需要先获取tail, 然后修改tail.next属性, 步骤更多, 更复杂
			tobeUnpark = null;
			for (Node t = tail; t != null && t != node; t = t.prev) {
				if (t.waitStatus <= 0) {
					tobeUnpark = t;
					//这里不break, 是为了找到最接近head的有效节点, 来唤醒
					//为啥不从头开始遍历, 而是从尾巴开始, next是优化, 不可靠
				}
			}
		}

		//唤醒
		if (tobeUnpark != null) {
			LockSupport.unpark(tobeUnpark.thread);
		}
	}

	//共享模式的释放, 链表有超过一个节点, 就进行释放逻辑
	private void doReleaseShared() {
		//释放逻辑: 头节点状态变为0或PROPAGATE, 如果有继任者, 则唤醒

		for (;;) {
			Node h = head;
			if (h != null && h != tail) {//有头节点且有继任者
				int ws = h.waitStatus;
				if (ws == Node.SIGNAL) {//头节点状态为SIGNAL, 则改为0, 然后唤醒继任者
					if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0)) {
						continue;
					}
					//唤醒继任者, 退出循环
					unparkSuccessor(h);
				}
				//头节点状态为0, 则改为PROPAGATE 然后退出循环 , 改为PROPAGATE表示并非(0, -1, -2), 其他地方并直接未用到这个状态,
				//但是其他地方通过waitStatus < 0 来进行一些处理
				else if (ws == 0 && !compareAndSetWaitStatus(h, 0, Node.PROPAGATE)) {
					continue;
				}
				//TODO:这里会不会存在其他线程吧Node状态改为非0和非PROPAGATE, 导致死循环
			}
			//完成释放, 这是h的状态为0或者PROPAGATE
			if (h == head) {
				break;
			}
		}
	}

	//设置node为头节点, 检查继任者是不是共享模式, 如果是, 且(propagate值大于0或继任者PROPAGATE被设置)
	//则共享释放(head状态设置为0或-3, 唤醒继任者)  :  增殖
	//propagate: tryAcquireShared的返回值, 一般是state减去acquires, 大于0表示还可以继续try.
	private void setHeadAndPropagate(Node node, long propagate) {
		Node h = head;//旧的head
		setHead(node);//设置新的头节点

		if (propagate > 0 //还可以继续tryAcquireShared, 可能等于0
				|| h == null || h.waitStatus < 0 //原来的head等待状态为PROPAGATE 猜测, 前head已经release了, 所以这里可以继续unpark
				|| (h = head) == null || h.waitStatus < 0) { //新的head等待状态小于0
			//下一个节点不为空, 且是共享模式, 就释放(头节点状态变为0,-3, 唤醒继任者, 如果有的话)
			Node s = node.next;
			if (s == null || s.isShared()) {
				doReleaseShared();//感觉里面的逻辑和unpark很想
			}
		}
	}

	//取消进行中的acquire
	private void cancelAcquire(Node node) {
		if (node == null) {
			return;
		}

		//减少不必要的唤醒
		node.thread = null;

		//找到node前面第一个有效的pred节点
		Node pred = node.prev;
		while (pred.waitStatus > 0) {
			node.prev = pred = pred.prev;
		}

		Node predNext = pred.next;

		node.waitStatus = Node.CANCELLED;

		//node为tail节点, 则更新tail为有效的前驱节点
		if (node == tail && compareAndSetTail(node, pred)) {
			//设置tail成功后, 修改tail.next为null, 失败没关系(可能有新的节点入队列了)
			compareAndSetNext(pred, predNext, null);
		}
		//node不是tail, 表示node后面有节点可能要唤醒, 如果不需要唤醒, 则更新下队列就好
		else {
			int ws;
			if (pred != head //1.1
					&& ((ws = pred.waitStatus) == Node.SIGNAL //2.1
					/*    */|| (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) //2.2
					&& pred.thread != null //3.1
			) {
				//1.1.非首节点(意味着可能不需要唤醒后继节点)
				//2.1.pred状态为SIGNAL, 意味着不用修改状态为SIGNAL
				//2.2.pred状态不为SIGNAL, 则修改为SIGNAL, 标记后续节点要唤醒, 因为走到这里, 表示node不为tail(前面的if判断).
				//3.1.pred.thread不为空(为空就表示pred被取消了或成head了, 这时就要唤醒后继了)
				//满足1,2,3表示, 后继的节点不需要唤醒, 只要更新下队列, 避免多余的唤醒

				//将node的next移动到prev上去, 完成node的出队, (如果next有且有效的话)
				Node next = node.next;
				if (next != null && next.waitStatus <= 0) {
					compareAndSetNext(pred, predNext, next);
				}
			}
			else {
				//1.首节点 -> 意味着release后, 唤醒的是当前节点, 而当前节点已经cancal了, 需要传播下去, 唤醒node.next后继结点, 否则会导致后续节点没人去唤醒
				//2.修改为pred状态为SIGNAL失败(可能是prev被(成head然后realease了)或(cancel)了) -> SIGNAL标记后继结点可以安全的park了, 标记失败, 则需要唤醒
				//3.pred.thread为空(为空就表示pred被取消了或成head了, 这时就要唤醒后继了) -> 唤醒吧, 否则唤醒链到这里就断了, 后续节点没人唤醒了
				
				//node的出队, 交给node.next = node, unparkSuccessor里面unpark, 然后后面的节点会将node出队列, 
				//走到这里说明pred的pred要么为null(head), 要么无效
				
				//如果这里不唤醒的话, 可能head解锁的时候, 当前节点正再取消, 导致唤醒没有传播下去, 造成死锁
				unparkSuccessor(node);
			}

			//通过将next指向自己, 标记node被取消, 简化isOnSyncQueue的实现
			node.next = node; // help GC
		}

	}

	//acquire失败后, 检查并更新pred的状态
	//true:线程需要阻塞
	private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
		int ws = pred.waitStatus;
		if (ws == Node.SIGNAL) {
			//表示前驱节点状态已经被标记为SIGNAL状态了, 我可以放心阻塞了.
			return true;
		}
		if (ws > 0) {
			//取消状态, 跳过取消的前驱节点, 返回false, 
			//方法外层会继续重试.
			do {
				node.prev = pred = pred.prev;
			}
			while (pred.waitStatus > 0);
		}
		else {
			//前驱状态满足(0 or CONDITION or PROPAGATE)情况下啊, 更新前驱节点状态为SIGNAL, 
			//意思就是, 我这里tryAcquire失败了, 要阻塞了, 标记下前驱节点状态为SIGNAL, 好在后面唤醒我.
			//这里不需要判断是否修改成功, 因为外部是循环, 所以等待下一次进入此方法就行了, 返回false, 继续重试.
			//设置完成后, 外层会继续尝试一次tryAcquire
			compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
		}
		return false;
	}

	//等待, 直到前驱节点为head, 且tryAcquire成功
	//模式为, 检查资源, 不满足, 则通知前驱节点解锁后要唤醒我(前驱节点SIGNAL), 然后再次尝试获取资源, 再次获取失败, 且状态(SIGNAL)设置好了, 就可以安全的park了
	//如果不做两次检查, 可能导致当prev.state还没有被设置为SIGNAL时, prev就已经unlock了, 这时没有唤醒后继结点, 而后继结点刚刚设置完prev.state, 直接park了, 导致一直不会被唤醒.
	//while(!tryAcquire()){
	//    prev.state = SIGNAL;
	//    if(!tryAcquire()){
	//        park();
	//    }else{
	//        获取到锁的逻辑, 比如这里就是设置head
	//        break;
	//    }
	//}
	//这里的过程类似上面, 大概就是设置完唤醒标记后, 会再次检查是否满足申请条件
	final boolean acquireQueued(final Node node, int arg) {
		boolean failed = true;
		try {
			boolean interrupted = false;
			for (;;) {
				//前驱节点为首节点, 则tryAcquire, 成功就更新head, 返回
				final Node p = node.predecessor();
				if (p == head && tryAcquire(arg)) {
					setHead(node);
					p.next = null;
					failed = false;
					return interrupted;
				}

				//当前驱节点不为head, 
				//多次调用shouldParkAfterFailedAcquire后
				//能将node的前驱节点更新为有效的节点, 且前驱状态为-1
				//或者满足前面的if, 跳出循环
				if (shouldParkAfterFailedAcquire(p, node) //更新node前驱的状态
						&& parkAndCheckInterrupt()) {
					interrupted = true;
				}
			}
		} finally {
			if (failed) {
				//嘛, 这里应该是走不到的, 除非异常了
				cancelAcquire(node);
			}
		}
	}

	//acquireQueued的Interruptibly版本
	private void doAcquireInterruptibly(int arg) throws InterruptedException {
		//加入链表, 如果链表为空, 则head为new Node();
		final Node node = addWaiter(Node.EXCLUSIVE);
		boolean failed = true;

		try {
			for (;;) {
				final Node p = node.predecessor();
				if (p == head && tryAcquire(arg)) {
					setHead(node);
					p.next = null;
					failed = false;
					return;
				}

				if (shouldParkAfterFailedAcquire(p, node)//
						&& parkAndCheckInterrupt()) {
					throw new InterruptedException();
				}
			}
		} finally {
			if (failed) {
				cancelAcquire(node);
			}
		}
	}

	//加了超时处理
	private boolean doAcquireNanos(int arg, long nanosTimeout) throws InterruptedException {
		if (nanosTimeout <= 0L) {
			return false;
		}
		final long deadline = System.nanoTime() + nanosTimeout;
		final Node node = addWaiter(Node.EXCLUSIVE);
		boolean failed = true;
		try {
			for (;;) {
				final Node p = node.predecessor();
				if (p == head && tryAcquire(arg)) {
					setHead(node);
					p.next = null;
					failed = false;
					return true;
				}

				nanosTimeout = deadline - System.nanoTime();
				if (nanosTimeout <= 0l) {
					return false;//超时
				}

				//要等待, 且等待时间超过阀值, 太短的话, 没必要park
				if (shouldParkAfterFailedAcquire(p, node) && nanosTimeout > spinForTimeoutThreshold) {
					LockSupport.parkNanos(nanosTimeout);
				}

				//中断状态处理
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
			}
		} finally {
			if (failed) {
				cancelAcquire(node);
			}
		}
	}

	//共享模式的非中断Acquires
	private void doAcquireShared(int arg) {
		final Node node = addWaiter(Node.SHARED);
		boolean failed = true;
		try {
			boolean interrupted = false;
			for (;;) {
				final Node p = node.predecessor();
				if (p == head) {
					int r = tryAcquireShared(arg);
					if (r >= 0) {//Acquire成功
						//设置node为head, 并增殖
						setHeadAndPropagate(node, r);
						p.next = null;
						if (interrupted)
							selfInterrupt();
						failed = false;
						return;
					}
				}
				if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt()) {
					interrupted = true;
				}
			}
		} finally {
			if (failed) {
				cancelAcquire(node);
			}
		}
	}

	private void doAcquireSharedInterruptibly(int arg) throws InterruptedException {
		final Node node = addWaiter(Node.SHARED);
		boolean failed = true;
		try {
			for (;;) {
				final Node p = node.predecessor();
				if (p == head) {
					int r = tryAcquireShared(arg);
					if (r >= 0) {
						setHeadAndPropagate(node, r);
						p.next = null;
						failed = false;
						return;
					}
				}

				if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt()) {
					throw new InterruptedException();
				}
			}
		} finally {
			if (failed) {
				cancelAcquire(node);
			}
		}
	}

	private boolean doAcquireSharedNanos(int arg, long nanosTimeout) throws InterruptedException {
		if (nanosTimeout <= 0l) {
			return false;
		}
		final long deadline = System.nanoTime() + nanosTimeout;
		final Node node = addWaiter(Node.SHARED);
		boolean failed = true;
		try {
			for (;;) {
				final Node p = node.predecessor();
				if (p == head) {
					int r = tryAcquireShared(arg);
					if (r >= 0) {
						setHeadAndPropagate(node, r);
						p.next = null;
						failed = false;
						return true;
					}
				}
				nanosTimeout = deadline - System.nanoTime();
				if (nanosTimeout <= 0l) {
					return false;
				}
				if (shouldParkAfterFailedAcquire(p, node) && nanosTimeout > spinForTimeoutThreshold) {
					LockSupport.parkNanos(this, nanosTimeout);
				}
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
			}
		} finally {
			if (failed) {
				cancelAcquire(node);
			}
		}
	}

	public final void acquire(int arg) {
		if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) {
			selfInterrupt();
		}
	}

	public final void acquireInterruptibly(int arg) throws InterruptedException {
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
		if (!tryAcquire(arg)) {
			doAcquireInterruptibly(arg);
		}
	}

	public final boolean tryAcquireNanos(int arg, long nanosTimeout) throws InterruptedException {
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
		return tryAcquire(arg) || doAcquireNanos(arg, nanosTimeout);
	}

	//释放逻辑, 返回值为tryRelease结果
	public final boolean release(int arg) {
		if (tryRelease(arg)) {//释放成功
			Node h = head;

			//当没有竞争时, head是为空的, 当有竞争时, head被后面来的线程创建, 并加入队列,
			//然后在shouldParkAfterFailedAcquire()中修改状态(0 -> -1).
			//所以这里, head会空的话, 不用unpark继任.

			//head不为空时, 状态不为0, 表示:

			if (h != null && h.waitStatus != 0) {
				unparkSuccessor(h);
			}
			return true;
		}
		return false;
	}

	public final void acquireShared(int arg) {
		if (tryAcquireShared(arg) < 0) {
			//try失败, 则一直试, 直到成功
			doAcquireShared(arg);
		}
	}

	public final void acquireSharedInterruptibly(int arg) throws InterruptedException {
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
		if (tryAcquireShared(arg) < 0) {
			doAcquireSharedInterruptibly(arg);
		}
	}

	public final boolean tryAcquireSharedNanos(int arg, long nanosTimeout) throws InterruptedException {
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
		return tryAcquireShared(arg) >= 0 //try成功直接返回, 失败继续试
				|| doAcquireSharedNanos(arg, nanosTimeout);
	}

	public final boolean releaseShared(int arg) {
		if (tryReleaseShared(arg)) {
			doReleaseShared();
			return true;
		}
		return false;
	}

	static void selfInterrupt() {
		Thread.currentThread().interrupt();
	}

	//设置头节点, 并释放thread和prev的引用, 因为已经不需要了, GC友好.
	private void setHead(Node node) {
		head = node;
		node.thread = null;
		node.prev = null;
	}

	protected boolean tryAcquire(int arg) {
		throw new UnsupportedOperationException();
	}

	protected boolean tryRelease(int arg) {
		throw new UnsupportedOperationException();
	}

	protected int tryAcquireShared(int arg) {
		throw new UnsupportedOperationException();
	}

	protected boolean tryReleaseShared(int arg) {
		throw new UnsupportedOperationException();
	}

	protected boolean isHeldExclusively() {
		throw new UnsupportedOperationException();
	}

	private final boolean parkAndCheckInterrupt() {
		LockSupport.park(this);
		return Thread.interrupted();
	}

	protected final int getState() {
		return state;
	}

	protected final void setState(int newState) {
		state = newState;
	}

	static final class Node {
		static final Node SHARED = new Node();
		static final Node EXCLUSIVE = null;

		static final int CANCELLED = 1;
		static final int SIGNAL = -1;
		static final int CONDITION = -2;
		static final int PROPAGATE = -3;

		volatile int waitStatus;
		volatile Node prev;
		volatile Node next;
		volatile Thread thread;
		Node nextWaiter;

		final boolean isShared() {
			return nextWaiter == SHARED;
		}

		final Node predecessor() {
			Node p = prev;
			if (p == null) {
				throw new NullPointerException();
			}
			else {
				return p;
			}
		}

		Node() {
		}

		Node(Thread thread, Node mode) {
			this.nextWaiter = mode;
			this.thread = thread;
		}

		Node(Thread thread, int waitStatus) {
			this.waitStatus = waitStatus;
			this.thread = thread;
		}
	}

	private static final Unsafe unsafe = Unsafe.getUnsafe();
	private static final long stateOffset;
	private static final long headOffset;
	private static final long tailOffset;
	private static final long waitStatusOffset;
	private static final long nextOffset;

	static {
		try {
			stateOffset = unsafe.objectFieldOffset(MyAqs.class.getDeclaredField("state"));
			headOffset = unsafe.objectFieldOffset(MyAqs.class.getDeclaredField("head"));
			tailOffset = unsafe.objectFieldOffset(MyAqs.class.getDeclaredField("tail"));
			waitStatusOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("waitStatus"));
			nextOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("next"));
		} catch (Exception ex) {
			throw new Error(ex);
		}
	}

	protected final boolean compareAndSetState(int expect, int update) {
		return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
	}

	private final boolean compareAndSetHead(Node update) {
		return unsafe.compareAndSwapObject(this, headOffset, null, update);
	}

	private final boolean compareAndSetTail(Node expect, Node update) {
		return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
	}

	private static final boolean compareAndSetWaitStatus(Node node, int expect, int update) {
		return unsafe.compareAndSwapInt(node, waitStatusOffset, expect, update);
	}

	private static final boolean compareAndSetNext(Node node, Node expect, Node update) {
		return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
	}

}
