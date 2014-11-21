package com.haogrgr.test.main;
import sun.misc.Contended;

/**
 * Note, to make @Contended work, you must specify "-XX:-RestrictContended" to enable it in the VM arguments.
 * Besides, you can use "-XX:FieldPaddingWidth" to control the padding size (default is 128bytes)
 */
@SuppressWarnings("restriction")
public final class FalseSharingTest {
    public final static int NUM_THREADS = 4;

    public final static long ITERATIONS = 500L * 1000L * 1000L;

    private static VolatilePaddingLong[] paddingLongs = new VolatilePaddingLong[NUM_THREADS];

    private static VolatileNativeLong[] nativeLongs = new VolatileNativeLong[NUM_THREADS];

    private static ContendedLongs contendedLongs = new ContendedLongs();

    static {
        // init
        for (int i = 0; i < paddingLongs.length; i++) {
            paddingLongs[i] = new VolatilePaddingLong();
        }

        for (int i = 0; i < nativeLongs.length; i++) {
            nativeLongs[i] = new VolatileNativeLong();
        }
    }

    public static void main(final String[] args) throws Exception {
        System.out.println("========= With Padding ==========");
        final long start = System.nanoTime();
        runPaddingLongTest();
        System.out.println(String.format("duration = %,dns", (System.nanoTime() - start)));
        System.out.println();
        System.out.println("========= Without Padding ========");
        final long start2 = System.nanoTime();
        runNativeLongTest();
        System.out.println(String.format("duration = %,dns", (System.nanoTime() - start2)));
        System.out.println();
        System.out.println("========= With Contended Annotation ========");
        final long start3 = System.nanoTime();
        runContendedLongTest();
        System.out.println(String.format("duration = %,dns", (System.nanoTime() - start3)));
    }

    private static void runPaddingLongTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new PaddingLongRunner(i));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
    }

    private static void runNativeLongTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new NativeLongRunner(i));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
    }

    private static void runContendedLongTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new ContendedLongRunner(i));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
    }

    public final static class VolatilePaddingLong {
        public volatile long value = 0L;
        public long p1, p2, p3, p4, p5, p6; // padding
    }

    public final static class VolatileNativeLong {
        public volatile long value = 0L;
    }

    public final static class ContendedLongs {
        @Contended
        public volatile long value1;

        @Contended
        public volatile long value2;

        @Contended
        public volatile long value3;

        @Contended
        public volatile long value4;
    }

    public final static class PaddingLongRunner implements Runnable {

        public PaddingLongRunner(int threadIdx) {
            this.threadIdx = threadIdx;
        }

        private final int threadIdx;

        @Override
        public void run() {
            long i = ITERATIONS + 1;
            while (0 != --i) {
                paddingLongs[threadIdx].value = i;
            }
        }
    }

    public final static class NativeLongRunner implements Runnable {

        public NativeLongRunner(int threadIdx) {
            this.threadIdx = threadIdx;
        }

        private final int threadIdx;

        @Override
        public void run() {
            long i = ITERATIONS + 1;
            while (0 != --i) {
                nativeLongs[threadIdx].value = i;
            }
        }
    }

    public final static class ContendedLongRunner implements Runnable {

        public ContendedLongRunner(int threadIdx) {
            this.threadIdx = threadIdx;
        }

        private final int threadIdx;

        @Override
        public void run() {
            switch (threadIdx) {
                case 0:
                    doRunForContendedValue1();
                    break;
                case 1:
                    doRunForContendedValue2();
                    break;
                case 2:
                    doRunForContendedValue3();
                    break;
                case 3:
                    doRunForContendedValue4();
                    break;
                default:
                    throw new RuntimeException();
            }
        }

        public void doRunForContendedValue1() {
            long i = ITERATIONS + 1;
            while (0 != --i) {
                contendedLongs.value1 = i;
            }
        }

        public void doRunForContendedValue2() {
            long i = ITERATIONS + 1;
            while (0 != --i) {
                contendedLongs.value2 = i;
            }
        }

        public void doRunForContendedValue3() {
            long i = ITERATIONS + 1;
            while (0 != --i) {
                contendedLongs.value3 = i;
            }
        }

        public void doRunForContendedValue4() {
            long i = ITERATIONS + 1;
            while (0 != --i) {
                contendedLongs.value4 = i;
            }
        }

    }
}