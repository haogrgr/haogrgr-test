package com.haogrgr.test.main;

import java.util.Random;

public class CacheMissTest {

    public static void main(String[] args) {
        long start, end;
        int[] array = new int[10100000];
        int[] array1 = new int[10100000];
        int[] array2 = new int[10100000];
        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < array.length; i++) {
            int temp = r.nextInt(10100000);
            array[i] = temp;
            array1[i] = temp;
            array2[i] = temp;
        }
        
        for (long i = 0; i < 1000000000; i++) {
            int temp = (int)i % 10000000;
            swap(array, temp, temp + 2);
        }
        
        start = System.currentTimeMillis();
        for (long i = 0; i < 1000000000; i++) {
            int temp = (int)i % 8;
            swap(array1, temp, temp + 2);
        }
        end = System.currentTimeMillis();
        System.out.println(end - start);
        
        start = System.currentTimeMillis();
        for (long i = 0; i < 1000000000; i++) {
            int temp = (int)i % 10000000;
            swap(array2, temp, temp + 100000);
        }
        end = System.currentTimeMillis();
        System.out.println(end - start);

    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
