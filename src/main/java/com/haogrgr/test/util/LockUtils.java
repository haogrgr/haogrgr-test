package com.haogrgr.test.util;

import java.util.concurrent.ConcurrentHashMap;

public class LockUtils {
    
    private static final ConcurrentHashMap<String, Object> syncObjs = new ConcurrentHashMap<String, Object>();
    
    public static Object getSyncObj(String key){
        if(key == null){
            throw new RuntimeException("key is null");
        }
        Object obj = syncObjs.get(key);
        while(obj == null){
            syncObjs.putIfAbsent(key, new Object());
            obj = syncObjs.get(key);
        }
        return obj;
    }
    
    public static Object getSyncObj2(String key){
        if(key == null){
            throw new RuntimeException("key is null");
        }
        Object obj = syncObjs.get(key);
        while(obj == null){
            Object temp = new Object();
            Object putIfAbsent = syncObjs.putIfAbsent(key, temp);
            if(putIfAbsent == null){
                obj = temp;
            }else{
                obj = putIfAbsent;
            }
        }
        return obj;
    }
    
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Object syncObj = LockUtils.getSyncObj2("haogrgr");
            System.out.println(syncObj);
        }
    }

}
