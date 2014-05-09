package com.haogrgr.test.algorithm;

/**
 * 题目:上阶梯
 * 假设A上台阶，一次可以跨1层，2层，3层.. 或m层，
 * 其中，m和n都是正整数，并且 m <= n, m <= 10, n <= 50
 * 问A上n层台阶，有多少种走法？
 * 
 * 例如:n=5, 时 有 (1,2,2),(2,3),(1,4),(5)
 */
public class GoUpstair {

    /**
     * @param n 台阶数
     * @param m 一次可以走的台阶数
     */
    public static void foo(int n, int m) {
        /**
         * 每个数组元素表示当前数组下标所对应的台阶数下的所有可以到达此台阶的方法总数
         */
        long[] f = new long[n + 1];
        /**
         * 对初始条件进行初始化，注意：当上第m层台阶的时候，它的方法总数是： f[n] = f[n-1]+f[n-2]+……+f[n-(m-1)]
         */
        f[0] = 1;f[1] = 1;f[2] = 2;
        
        //f[3] = f[0] + f[1] + f[2] == 4; 
        //计算f[n-1]
        for (int i = 3; i <= m; i++) {
            for (int j = 0; j < i; j++) {
                f[i] = f[i] + f[j];
            }
        }
        
        //计算f[n]
        for (int i = m + 1; i < f.length; i++) {
            for (int j = i - 1; j >= i - m; j--) {
                f[i] = f[i] + f[j];
            }
        }

        for (int i = 1; i < n; i++) {
            System.out.println("爬第  " + i + " 层台阶的方法数为：  " + f[i]);
        }
        
//        System.out.println("爬第  " + f.length + " 层台阶的方法数为：  " + f[f.length - 2]);
    }

    public static void main(String[] args) {
        // 表示有n层台阶，每次可以走1个台阶，可以走2个台阶，可以走3个台阶……可以走m个台阶 
        foo(1000, 1000);
    }

}
