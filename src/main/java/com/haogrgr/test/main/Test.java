package com.haogrgr.test.main;

import java.nio.charset.Charset;

public class Test {

    public static void main(String[] args) {
        Charset forName = Charset.forName("GB2312");
        System.out.println(forName);
    }

}
