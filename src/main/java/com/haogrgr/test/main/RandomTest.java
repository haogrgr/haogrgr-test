package com.haogrgr.test.main;

import java.util.Arrays;
import java.util.Random;

public class RandomTest {

    public static Random random = new Random();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            test();
        }
    }

    public static void test() {
        int[] peoples = new int[10];

        for (int i = 0; i < 200; i++) {
            peoples[random.nextInt(10)]++;
        }

        System.out.println(Arrays.toString(peoples) + " " + sum(peoples));

        int k = 15, y=1;
        for (int i = 0; i < peoples.length; i++) {
            if (i % 2 == 0) {
                peoples[i] += k;
            } else {
                int temp = peoples[i] - k;
                if(temp > y){
                    peoples[i] -= k;
                } else {
                    if (i != 0) {
                        peoples[i - 1] -= k;
                    }
                }
            }
        }

        System.out.println(Arrays.toString(peoples) + " " + sum(peoples));
        System.out.println();
    }

    public static int sum(int[] ints) {
        int count = 0;
        for (int i = 0; i < ints.length; i++) {
            count += ints[i];
        }
        return count;
    }
}
