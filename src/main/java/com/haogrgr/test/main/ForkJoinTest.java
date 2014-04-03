package com.haogrgr.test.main;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTest {

    public static void main(String[] args) throws Exception {
        int start = 0, end = 1000000000;
        ForkJoinPool pool = new ForkJoinPool();
        long startt = System.currentTimeMillis();
        Future<Integer> submit = pool.submit(new Calculator(start, end));
        System.out.println(submit.get());
        
        System.out.println(System.currentTimeMillis() - startt);
        
        startt = System.currentTimeMillis();
        int sum = 0;
        for (int i = start; i <= end; i++) {
            sum += i;
        }
        System.out.println(sum);
        
        System.out.println(System.currentTimeMillis() - startt);
    }

}

class Calculator extends RecursiveTask<Integer> {

    private static final long serialVersionUID = -2090462446283352261L;

    private static final int THRESHOLD = 50000000;

    private int start;
    private int end;

    public Calculator(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        Integer sum = 0;
        if ((end - start) < THRESHOLD) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            int middle = (start + end) / 2;
            Calculator left = new Calculator(start, middle);
            Calculator right = new Calculator(middle + 1, end);
            left.fork();
            right.fork();
            sum = left.join() + right.join();
        }
        return sum;
    }

}
