package com.haogrgr.test.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MultiLevelHashMap<K, V> implements Iterable<Map.Entry<K, V>> {

    private static final Integer SINGLE_MAP_SIZE = 262144;

    private Integer mapSize = 1;
    private HashMap<K, V> maps[];

    public static void main(String[] args) {
        MultiLevelHashMap<String, Integer> map = new MultiLevelHashMap<String, Integer>(16, 4);

        for (int i = 0; i < 512; i++) {
            map.put("" + i, i);
        }

        System.out.println(map);

        System.out.println(map.size());
    }

    @SuppressWarnings("unchecked")
    public MultiLevelHashMap(int totalSize, int singleMapSize) {
        int realTotalSize = 1;
        while (totalSize > realTotalSize) {
            realTotalSize = realTotalSize << 1;
        }

        mapSize = realTotalSize / singleMapSize;
        mapSize = mapSize == 0 ? 1 : mapSize;

        maps = new HashMap[mapSize];
        for (int i = 0; i < mapSize; i++) {
            maps[i] = new HashMap<K, V>(singleMapSize);
        }
    }

    public MultiLevelHashMap(int totalSize) {
        this(totalSize, SINGLE_MAP_SIZE);
    }

    public V get(K key) {
        int index = index(key, mapSize);
        V value = maps[index].get(key);
        return value;
    }

    public void put(K key, V value) {
        int index = index(key, mapSize);
        maps[index].put(key, value);
    }
    
    public int size(){
        int size = 0;
        for (HashMap<K, V> map : maps) {
            size = size + map.size();
        }
        return size;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("MultiLevelHashMap[\n");
        for (int i = 0; i < mapSize; i++) {
            result.append(maps[i].toString());
            result.append("\n");
        }
        result.append("]");
        return result.toString();
    }

    public static int index(Object obj, int tableSize) {
        int hash = hash(obj);
        int index = Math.abs(hash % tableSize);
        return index;
    }

    private static int hash(Object obj){
        int h = 0;
        h ^= obj.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
    
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new MultiLevelMapIterator();
    }

    private final class MultiLevelMapIterator implements Iterator<Map.Entry<K, V>> {

        int index = 0;
        Iterator<Map.Entry<K, V>> realIterators[];

        @SuppressWarnings("unchecked")
        public MultiLevelMapIterator() {
            this.realIterators = new Iterator[mapSize];
            for (int i = 0; i < mapSize; i++) {
                realIterators[i] = maps[i].entrySet().iterator();
            }
        }

        @Override
        public boolean hasNext() {
            if (index == realIterators.length) {
                return false;
            }

            int tempIndex = index;

            boolean hasNext = realIterators[tempIndex].hasNext();
            while (!hasNext && tempIndex < realIterators.length - 1) {
                hasNext = realIterators[++tempIndex].hasNext();
            }

            return hasNext;
        }

        @Override
        public Map.Entry<K, V> next() {
            Map.Entry<K, V> next = null;
            if (!hasNext()) {
                throw new IndexOutOfBoundsException("has no more next !");
            }

            int tempIndex = index;
            if (realIterators[tempIndex].hasNext()) {
                next = realIterators[tempIndex].next();
            } else {
                tempIndex++;
                while (!realIterators[tempIndex].hasNext()) {
                    tempIndex++;
                }
                next = realIterators[tempIndex].next();
                index = tempIndex;
            }

            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("不支持的操做");
        }
    }
}
