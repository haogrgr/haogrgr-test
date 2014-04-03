package com.haogrgr.test.main;

import java.util.Random;

public class BubbleSort {
     
    public int[] sort(int[] a) {
        long icount=0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; ++j) {//两个代码就是这里有不同
                if (a[i] > a[j]) {
                    int temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                    icount++;
                }
            }
        }
        System.out.println("执行交换次数:"+icount);
        return a;
    }
    
    public int[] sort2(int[] a) {
        long icount=0;
        for (int i = 0; i < a.length; i++) {
            for (int j = i; j < a.length; ++j) {//两个代码就是这里有不同
                if (a[i] > a[j]) {
                    int temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                    icount++;
                }
            }
        }
        System.out.println("执行交换次数:"+icount);
        return a;
    }
     
    public static void main(String[] args) {
        int length = 100000;
        int i[]=new int[length];
        int icopy[]=new int[length];
        Random r=new Random(System.currentTimeMillis());
        for (int j = 0; j < length; j++) {
            int temp=(int)(r.nextInt());
            i[j]=temp;
            icopy[j]=temp;
        }
        BubbleSort bubble=new BubbleSort();
        long start1=System.currentTimeMillis();
        bubble.sort(i);
        long stop1=System.currentTimeMillis();
        System.out.println(stop1-start1);
        
        long start2=System.currentTimeMillis();
        bubble.sort2(icopy);
        long stop2=System.currentTimeMillis();
        System.out.println(stop2-start2);
        System.out.println();
    }
 
}