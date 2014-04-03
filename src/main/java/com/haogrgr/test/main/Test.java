package com.haogrgr.test.main;

public class Test {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            test();
        }
    }

    public static void test() {
        long start = System.currentTimeMillis();

        int n = 100000000;
        boolean[] primes = new boolean[n + 1];

        for (int i = 2; i <= n; ++i) {
            primes[i] = true;
        }
        
        int j,m;
        for (j = 2; j <= n; ++j) {
            if (primes[j]) {
                for (m = 2; j * m <= n; ++m) {
                    primes[j * m] = false;
                }
            }
        }

        for (int k = 2; k <= n; ++k) {
            if (primes[k]) {
                //                System.out.println(k + "是素数");
            }
        }

        long cost = System.currentTimeMillis() - start;
        System.out.println("cost:" + cost);
    }
}
