package com.haogrgr.test.main;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class BlockQueueTest<T> {

private Object lock = new Object();
    
    private Queue<T> queue = new LinkedList<T>();
    private int maxSize = 16;
    
    public BlockQueueTest() {
    }
    
    public BlockQueueTest(int size) {
        this.maxSize = size;
    }
    
    public T take() throws InterruptedException {
        synchronized (lock) {
            while (queue.size() == 0) {
                lock.wait(); 
                if (queue.size() == 0) { 
                    System.out.println(queue);
                }
            }
            if(queue.size() == maxSize) { lock.notifyAll(); }
            return queue.remove();
        }
    }
    
    public void offer(T t) throws InterruptedException {
        synchronized (lock) {
            if (queue.size() == maxSize) { lock.wait(); }
            if (queue.size() == 0) { lock.notifyAll(); }
            queue.offer(t);
        }
    }
    
    public static void main(String[] args) {
        final BlockQueueTest<String> blockingSizeQueue = new BlockQueueTest<String>(3);
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        blockingSizeQueue.offer(UUID.randomUUID().toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "生产者").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        blockingSizeQueue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "消费者").start();
    }
    
    
}
