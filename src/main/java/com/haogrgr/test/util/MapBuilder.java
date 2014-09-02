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
    
    private Map<K, V> map = new HashMap<K, V>(8);
    
    public static void main(String[] args) {
        System.out.println(MapBuilder.make("a", "a").build("b", "b"));
        System.out.println(MapBuilder.make("a", "a").put("b", "b").build());
    }
    
    public MapBuilder(){}
    
    public MapBuilder(K key, V value){
        map.put(key, value);
    }
    
    /**
     * 创建key类型为K, value类型为V的map
     */
    public static <K, V> MapBuilder<K, V> make(K key, V value ){
        MapBuilder<K, V> builder = new MapBuilder<K, V>(key, value);
        return builder;
    }
    
    /**
     * 创建key类型为K, value类型为Object的map
     */
    public static <K, V> MapBuilder<K, Object> makeO(K key, Object value ){
        MapBuilder<K, Object> builder = new MapBuilder<K, Object>(key, value);
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
