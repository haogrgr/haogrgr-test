package com.haogrgr.test.algorithm;

/**
 * 利用移位实现四则运算
 * <p>Description: 利用移位实现四则运算</p>
 * <p>Author: desheng.tu</p>
 * <p>Date: 2014年6月23日</p>
 */
public class Arithmetic {

    public static void main(String[] args) {
        System.out.println(add(Integer.MAX_VALUE, 1) == (Integer.MAX_VALUE + 1));
        System.out.println(sub(Integer.MAX_VALUE, Integer.MAX_VALUE) == 0);
        System.out.println(sub(Integer.MIN_VALUE, Integer.MIN_VALUE) == 0);
        System.out.println(sub(100, -100) == 200);
    }
    
    public static int add(int a, int b){
        return b == 0 ? a : add(a ^ b, (a & b) << 1);
    }
    
    public static int sub(int a, int b){
        return add(a, add(~b, 1));
    }
}
