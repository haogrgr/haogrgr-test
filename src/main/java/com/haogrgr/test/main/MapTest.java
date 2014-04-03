package com.haogrgr.test.main;

import java.util.concurrent.ConcurrentHashMap;

public class MapTest {

    public static void main(String[] args) throws Exception {
        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>(16, 0.75f, 16);
        map.put(1, 1);
        map.put(2, 2);
        map.put(1, 1);
        
        map.get(1);
        
        map.size();
        
        map.clear();
    }

}
