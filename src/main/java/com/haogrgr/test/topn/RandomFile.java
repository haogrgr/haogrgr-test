package com.haogrgr.test.topn;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomFile {
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 5;
    private static final int STR_NUM = 20000 * 1000;
    private static final String FILEPATH = "C:/tmp/src.txt";
    private static final Random random = new Random(System.currentTimeMillis());
    private static final Random random_char = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws IOException {
        long begin = System.currentTimeMillis();
        BufferedWriter bw = new BufferedWriter(new FileWriter(FILEPATH));
        for (int i = 0; i < STR_NUM; i++) {
            bw.write(getRandom());
            bw.newLine();
            if (i % 200 == 0) {
                bw.flush();
            }
        }
        for (int i = 1; i <= 100; i++) {
            for (int j = 0; j < i; j++) {
                bw.write("thetop" + i);
                bw.newLine();
            }
        }
        bw.close();
        long end = System.currentTimeMillis();
        System.out.println(STR_NUM + ":" + (end - begin));
    }

    private static String getRandom() {
        StringBuilder sb = new StringBuilder();
        int size = random.nextInt(MAX_LENGTH - MIN_LENGTH) + MIN_LENGTH;
        for (int i = 0; i <= size; i++) {
            int index = random_char.nextInt(chars_length);
            sb.append(source_chars[index]);
        }
        return sb.toString();
    }

    private static final char[] source_chars = new char[('9' - '0' + 1) + ('Z' - 'A' + 1) + ('z' - 'a' + 1)];
    private static final int chars_length = source_chars.length;
    static {
        int i = 0;
        for (char c = '0'; c <= '9'; c++) {
            source_chars[i++] = c;
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            source_chars[i++] = c;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            source_chars[i++] = c;
        }
    }
}
