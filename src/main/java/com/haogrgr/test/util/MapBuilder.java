package com.haogrgr.test.util;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap工具类,精简代码
 * <p>Description: HashMap工具类,精简代码</p>
 * <p>Author: desheng.tu</p>
 * <p>Date: 2014年5月13日</p>
 */
public class MapBuilder<K, V> {
    
    private Map<K, V> map;
    
    public static void main(String[] args) {
        Map<String, String> build = MapBuilder.make("key1", "value1").put("key2", "value2").build();
        System.out.println(build);
    }
    
    public MapBuilder(){
        this(8);
    }
    
    public MapBuilder(int initSize){
        this.map = new HashMap<K, V>(initSize);
    }
    
    public MapBuilder(K key, V value){
        this(16, key, value);
    }
    
    public MapBuilder(int initSize, K key, V value){
        this.map = new HashMap<K, V>(initSize);
        map.put(key, value);
    }
    
    public static <K, V> MapBuilder<K, V> make(K key, V value ){
        MapBuilder<K, V> builder = new MapBuilder<K, V>(key, value);
        return builder;
    }
    
    public MapBuilder<K, V> put(K key, V value){
        map.put(key, value);
        return this;
    }
    
    public Map<K, V> build(K key, V value){
        map.put(key, value);
        return map;
    }
    
    public Map<K, V> build(){
        return map;
    }
}
