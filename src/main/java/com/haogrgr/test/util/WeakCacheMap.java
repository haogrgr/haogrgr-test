package com.haogrgr.test.util;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 弱引用Map,适合做缓存,包装WeakHashMap, 将Value也使用WeakReference,
 * 防止当WeakHashMap的key回收后,还是有value的强引用,
 * 需要等到下一次WeakHashMap相关方法调用才会清除key为空的value
 * 抄的com.sum.WeakCache -,-;
 * @see java.util.WeakHashMap.expungeStaleEntries()
 * <p>Author: desheng.tu</p>
 * <p>Date: 2014年7月3日</p>
 */
public class WeakCacheMap<K, V> {

    private final Map<K, Reference<V>> map = new WeakHashMap<K, Reference<V>>();

    public V get(K key) {
        Reference<V> localReference = this.map.get(key);
        if (localReference == null) {
            return null;
        }
        
        V localObject = localReference.get();
        if (localObject == null) {
            this.map.remove(key);
        }
        
        return localObject;
    }

    public void put(K key, V value) {
        if (value != null) {
            this.map.put(key, new WeakReference<V>(value));
        } else
            this.map.remove(key);
    }

    public void clear() {
        this.map.clear();
    }
}
