package com.haogrgr.test.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class FixedLenPriorityQueue<E> {

    private int maxLen;
    private PriorityQueue<E> queue;
    private Comparator<E> comparator;

    public FixedLenPriorityQueue(int maxLen) {
        if (maxLen <= 0) {
            throw new IllegalArgumentException("maxLen 不能小于等于 0 !");
        }
        this.maxLen = maxLen;
        this.queue = new PriorityQueue<E>(maxLen + 1);
    }
    
    @SuppressWarnings("unchecked")
    public FixedLenPriorityQueue(int maxLen, Class<E> clazz) {
        if (maxLen <= 0) {
            throw new IllegalArgumentException("maxLen 不能小于等于 0 !");
        }
        
        Comparator<E> c = null;
        if(clazz.equals(Integer.class)){
            c = (Comparator<E>) new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1 - o2;
                }
            };
        }
        
        this.maxLen = maxLen;
        this.queue = new PriorityQueue<E>(maxLen + 1, c);
        this.comparator = c;
    }
    
    public FixedLenPriorityQueue(int maxLen, Comparator<E> comparator) {
        if (maxLen <= 0) {
            throw new IllegalArgumentException("maxLen 不能小于等于 0 !");
        }
        this.maxLen = maxLen;
        this.queue = new PriorityQueue<E>(maxLen + 1, comparator);
        this.comparator = comparator;
    }

    public void add(E e) {
        if (queue.size() < maxLen) {
            queue.offer(e);
        } else {
            E poll = queue.peek();
            if (comparator.compare(e, poll) > 0) {
                queue.poll();
                queue.offer(e);
            }
        }
    }
    
    @Override
    public String toString() {
        Object[] array = queue.toArray();
        Arrays.sort(array);
        String str = Arrays.toString(array);
        return str;
    }
    
    public static void main(String[] args) {
        FixedLenPriorityQueue<Integer> queue = new FixedLenPriorityQueue<Integer>(10, Integer.class);
        ArrayList<Integer> list = new ArrayList<Integer>();
        
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int nextInt = random.nextInt(10);
            queue.add(nextInt);
            list.add(nextInt);
        }
        
        System.out.println(queue);
        Collections.sort(list);
        System.out.println(list);
        
    }
    
}
