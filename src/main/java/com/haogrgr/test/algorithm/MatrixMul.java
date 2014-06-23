package com.haogrgr.test.algorithm;

import java.util.Arrays;

/**
 * 矩阵乘法 <p>Description: 矩阵乘法</p> <p>Author: desheng.tu</p> <p>Date:
 * 2014年5月27日</p>
 */
public class MatrixMul {

    public static void main(String[] args) {
        int[][] a = { 
                { 1, 2 }, 
                { 3, 4 } 
                };

        int[][] b = { 
                { 5, 6 }, 
                { 7, 8 } 
                };
        
        int length = 2;
        int[][] c = new int[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                for (int k = 0; k < length; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        
        int[][] d = { 
                { 19, 22 }, 
                { 43, 50 } 
                };
        
        System.out.println(Arrays.toString(c[0]));
        System.out.println(Arrays.toString(c[1]));
        System.out.println(Arrays.toString(d[0]));
        System.out.println(Arrays.toString(d[1]));
    }

}
