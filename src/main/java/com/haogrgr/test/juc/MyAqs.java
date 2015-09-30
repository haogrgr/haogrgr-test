package com.haogrgr.test.juc;

import java.util.concurrent.locks.AbstractOwnableSynchronizer;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
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
				//因为acquire时, 如果直接tryacquire成功了, 则不会加入链表, 所以这里补上
				if (compareAndSetHead(new Node())) {
					tail = head;
				}
			}
			else {
				//cas设置tail
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
		//待添加的节点, mode为独占(EXCLUSIVE)or共享(SHARED)
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

	//唤醒node后继的有效节点, 被cancelAcquire,release等调用
	private void unparkSuccessor(Node node) {

		//如果状态不为0或CANCELLED, 则改为0, 没改成功也不管 TODO: 为啥没改成功不用管
		int ws = node.waitStatus;
		if (ws < 0) {
			compareAndSetWaitStatus(node, ws, 0);
		}

		//找到有效的next, 并unpark
		Node tobeUnpark = node.next;
		if (tobeUnpark == null || tobeUnpark.waitStatus > 0) {
			//next为空或无效, 从尾到头遍历, 直到碰到最前的有效状态的节点
			tobeUnpark = null;
			for (Node t = tail; t != null && t != node; t = t.prev) {
				if (t.waitStatus <= 0) {
					tobeUnpark = t;
					//这里不break, 是为了找到最接近head的有效节点, 来唤醒
					//TODO:为啥不从头开始遍历, 而是从尾巴开始
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
	private void setHeadAndPropagate(Node node, long propagate) {
		Node h = head;
		setHead(node);//设置新的头节点

		//TODO: 这几个条件待撸
		if (propagate > 0 // tryAcquireShared的返回值, 一般是state减去acquires, 
				|| h == null //原来的head为空
				|| h.waitStatus < 0 //原来的head等待状态小于0
				|| (h = head) == null //head为空了
				|| h.waitStatus < 0) { //新的head等待状态小于0
			//下一个节点不为空, 且是共享模式, 就释放(头节点状态变为0,-3, 唤醒继任者, 如果有的话)
			Node s = node.next;
			if (s == null || s.isShared()) {
				doReleaseShared();
			}
		}
	}

	//取消进行中的acquire
	private void cancelAcquire(Node node) {
		if (node == null) {
			return;
		}

		node.thread = null;

		//跳过cancelled的前驱节点
		Node pred = node.prev;
		while (pred.waitStatus > 0) {
			node.prev = pred = pred.prev;
		}

		Node predNext = pred.next;

		node.waitStatus = Node.CANCELLED;

		//更新新的尾节点为node的前面的第一个有效的节点
		if (node == tail && compareAndSetTail(node, pred)) {
			compareAndSetNext(pred, predNext, null);
		}
		//
		else {
			int ws;
			//非首节点,且状态为-1,且线程属性不为空
			if (pred != head //不为首节点
					//pred状态为-1, 如果不为-1, 就修改为-1
					&& ((ws = pred.waitStatus) == Node.SIGNAL //
					/**/|| (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) //
					&& pred.thread != null //线程不为空 TODO:为啥
			) {
				//继任者需要signal, 设置pred新的next
				Node next = node.next;
				if (next != null && next.waitStatus <= 0) {
					compareAndSetNext(pred, predNext, next);
				}
			}
			//pred为首节点, 或线程属性为空, 或修改Node状态为-1失败
			else {
				//唤醒后继节点  TODO:为啥
				unparkSuccessor(node);
			}
		}

	}

	//acquire失败后, 检查并更新pred的状态
	//true:线程需要阻塞
	private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
		int ws = pred.waitStatus;
		if (ws == Node.SIGNAL) {//-1等待
			return true;
		}
		if (ws > 0) {//取消, 更新前驱节点引用, 返回false, 继续尝试
			do {
				node.prev = pred = pred.prev;
			}
			while (pred.waitStatus > 0);
		}
		else {//小于等于0, 更新前驱节点状态为-1, 返回false, 继续尝试;
				//这里不需要判断是否修改成功, 因为外部是循环, 所以等待下一次进入此方法就行了
			compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
		}
		return false;
	}

	//等待, 直到前驱节点为head, 且tryAcquire成功
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
			stateOffset = unsafe.objectFieldOffset(AbstractQueuedSynchronizer.class.getDeclaredField("state"));
			headOffset = unsafe.objectFieldOffset(AbstractQueuedSynchronizer.class.getDeclaredField("head"));
			tailOffset = unsafe.objectFieldOffset(AbstractQueuedSynchronizer.class.getDeclaredField("tail"));
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
