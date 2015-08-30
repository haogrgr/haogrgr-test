package com.haogrgr.test.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可以参考Guava的Striped
 * 
 * @author desheng.tu
 * @date 2015年8月30日 下午1:12:31 
 *
 */
public class ConCurrentLockFetch {
    private AtomicLong[] excepts;
    private AtomicLong[] values;
    private ConcurrentHashMap<String, ReentrantLock> lockMap;
    
    public ConCurrentLockFetch(Integer threadNum) {
        for (int i = 0; i < threadNum; i++) {
            excepts[i] = new AtomicLong();
            values[i] = new AtomicLong();
        }
        lockMap = new ConcurrentHashMap<String, ReentrantLock>(threadNum);
    }
    
    public Lock getLock(String key){
        int index = Math.abs(key.hashCode() % values.length);
        long except = excepts[index].getAndIncrement();
        AtomicLong value = values[index];
        
        while(true){
            if(value.get() == except){
                ReentrantLock lock = getRealLock(key);
                value.set(except + 1);
                return lock;
            }
        }
    }
    
    public void releaseLock(String key, ReentrantLock lock){
        ReentrantLock original = lockMap.get(key);
        if(lock == original){
            lockMap.remove(key);
        }
    }
    
    private ReentrantLock getRealLock(String key){
        ReentrantLock lock = lockMap.get(key);
        if(lock == null){
            lock = new ReentrantLock();
            lockMap.put(key, lock);
        }
        return lock;
    }
}
