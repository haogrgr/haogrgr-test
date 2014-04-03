package com.haogrgr.test.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherTests {

    public static void main(String[] args) throws Exception {
        Pattern compile = Pattern.compile("(.*)\\Q.\\E.*");
//        Pattern compile = Pattern.compile("(.*)");
        Matcher matcher = compile.matcher("1.5");
        
        if(matcher.matches()){
            int groupCount = matcher.groupCount();
            for (int i = 0; i <= groupCount; i++) {
                String value = matcher.group(i);
                System.out.println(value);
            }
        }
    }

}
