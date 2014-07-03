package com.haogrgr.test.util;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

public class WeakCacheMap<K, V> {

    private final Map<K, Reference<V>> map = new WeakHashMap<K, Reference<V>>();

    public V get(K paramK) {
        Reference<V> localReference = this.map.get(paramK);
        if (localReference == null) {
            return null;
        }
        
        V localObject = localReference.get();
        if (localObject == null) {
            this.map.remove(paramK);
        }
        
        return localObject;
    }

    public void put(K paramK, V paramV) {
        if (paramV != null) {
            this.map.put(paramK, new WeakReference<V>(paramV));
        } else
            this.map.remove(paramK);
    }

    public void clear() {
        this.map.clear();
    }
}
