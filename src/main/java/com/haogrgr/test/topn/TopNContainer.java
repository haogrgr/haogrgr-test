package com.haogrgr.test.topn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

public class TopNContainer<T> implements Iterable<T> {

    private int maxLen;
    private List<T> tops;
    private Comparator<T> comparator;

    public TopNContainer(int maxLen, Comparator<T> comparator) {
        this.maxLen = maxLen;
        this.tops = new ArrayList<T>(maxLen + 1);
        this.comparator = comparator;
    }

    public void add(T e) {
        if (tops.size() >= maxLen) {
            if (comparator.compare(e, tops.get(0)) > 0) {
                int index = 0;
                while (index < tops.size() - 1 && comparator.compare(e, tops.get(index + 1)) > 0) {
                    index++;
                }
                if (index == 0) {
                    tops.set(0, e);
                } else {
                    tops.remove(0);
                    tops.add(index, e);
                }
            }
            return;
        }
        if (tops.size() < maxLen - 1) {
            tops.add(e);
            return;
        }
        if (tops.size() == maxLen - 1) {
            tops.add(e);
            Collections.sort(tops, comparator);
            return;
        }
    }
    
    @Override
    public Iterator<T> iterator() {
        return tops.iterator();
    }
    
    @Override
    public String toString() {
        return tops.toString();
    }

    public static TopNContainer<Entry<String, Integer>> getTopNContainer(int topSize){
        TopNContainer<Entry<String, Integer>> tops = new TopNContainer<Entry<String,Integer>>(topSize, new Comparator<Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o1.getValue() - o2.getValue();
            }
        });
        return tops;
    }
    
    public static void main(String[] args) {
        TopNContainer<Integer> tops = new TopNContainer<Integer>(10, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        ArrayList<Integer> list = new ArrayList<Integer>();

        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int nextInt = random.nextInt(10);
            tops.add(nextInt);
            list.add(nextInt);
        }

        System.out.println(tops);
        Collections.sort(list);
        System.out.println(list);

    }

}
