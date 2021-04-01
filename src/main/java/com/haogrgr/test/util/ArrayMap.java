package com.haogrgr.test.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 定长数组存放的MAP实现, 查找效率低, 空间浪费小, 不能去重, 仅仅实现MAP接口, 没有MAP语义
 *
 * @author shengde.tds
 * @since 2021-03-08 08:21:27
 */
public class ArrayMap<K, V> extends AbstractMap<K, V> {

    private List<Entry<K, V>> array;

    public ArrayMap(int size) {
        array = new ArrayList<>(size);
    }

    @Override
    public V put(K key, V value) {
        array.add(new ArrayMapEntry<>(key, value));
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<Entry<K, V>>() {
            @Override
            public Iterator<Entry<K, V>> iterator() {
                return array.iterator();
            }

            @Override
            public int size() {
                return array.size();
            }
        };
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    protected static class ArrayMapEntry<K, V> implements Map.Entry<K, V> {
        protected K key;
        protected V value;

        @Override
        public V setValue(V v) {
            value = v;
            return v;
        }
    }
}