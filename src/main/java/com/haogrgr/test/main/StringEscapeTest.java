package com.haogrgr.test.main;

import org.apache.commons.lang.StringEscapeUtils;

public class StringEscapeTest {

    public static void main(String[] args) throws Exception {
        String unescape = StringEscapeUtils.unescapeJava("\u0049");
        System.out.println(unescape);
        
    }

}
