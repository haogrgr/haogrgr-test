package com.haogrgr.test.algorithm;

/**
 * 利用移位实现四则运算
 * <p>Description: 利用移位实现四则运算</p>
 * <p>Author: desheng.tu</p>
 * <p>Date: 2014年6月23日</p>
 */
public class Arithmetic {

    public static void main(String[] args) {
        //如果len是2的幂 那么  a % len === a & (len - 1) 常用于hashcode计算,如ThreadLocalMap
        System.out.println(add(Integer.MAX_VALUE, 1) == (Integer.MAX_VALUE + 1));
        System.out.println(sub(Integer.MAX_VALUE, Integer.MAX_VALUE) == 0);
        System.out.println(sub(Integer.MIN_VALUE, Integer.MIN_VALUE) == 0);
        System.out.println(sub(100, -100) == 200);
    }
    
    public static int add(int a, int b){
        //原理:  
        //a ^ b 结果为   (a + b) 但是丢失了进位值
        //a & b 结果为   要进位的位置为1,其他位置为0  右移一位后表示进位
        //由于  (a ^ b) ^ (a & b << 1)还是有可能会丢失进位值,
        //所以需要递归, 直到 没有进位(即: a & b == 0 , 既  (a & b) << 1 == 0)
        //这时,a ^ b就是最终结果了.
        return b == 0 ? a : add(a ^ b, (a & b) << 1);
    }
    
    public static int sub(int a, int b){
        //原理:
        //a - b == a + (-b)
        //b按位取反得到的是(-b - 1) 即  ~b == (b ^ (-1)) == (-b - 1)
        //所以 -b == ~b + 1,  a - b == a + (~b + 1)
        return add(a, add(~b, 1));
    }
}
