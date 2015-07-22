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
        System.out.println(MapBuilder.of(1, "a"));
        System.out.println(MapBuilder.of(1, "a", 2, "b"));
        System.out.println(MapBuilder.of(1, "a", 2, "b", 3, "c"));
        System.out.println(MapBuilder.of(1, "a", 2, "b", 3, "c", 4, "d"));
        System.out.println(MapBuilder.of(1, "a", 2, "b", 3, "c", 4, "d", 5, "e"));
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
    
    public static <K, V> Map<K, V> of(K key, V value){
    	Map<K, V> map = new HashMap<>(4);
    	map.put(key, value);
    	return map;
    }
    
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2){
    	Map<K, V> map = new HashMap<>(4);
    	map.put(key1, value1);
    	map.put(key2, value2);
    	return map;
    }
    
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3){
    	Map<K, V> map = new HashMap<>(8);
    	map.put(key1, value1);
    	map.put(key2, value2);
    	map.put(key3, value3);
    	return map;
    }
    
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4){
    	Map<K, V> map = new HashMap<>(8);
    	map.put(key1, value1);
    	map.put(key2, value2);
    	map.put(key3, value3);
    	map.put(key4, value4);
    	return map;
    }
    
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5){
    	Map<K, V> map = new HashMap<>(8);
    	map.put(key1, value1);
    	map.put(key2, value2);
    	map.put(key3, value3);
    	map.put(key4, value4);
    	map.put(key5, value5);
    	return map;
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
