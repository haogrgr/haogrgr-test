package com.haogrgr.test.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherTest {

    public static void main(String[] args) throws Exception {
        Pattern compile = Pattern.compile("(.*)\\Q.\\E.*");
//        Pattern compile = Pattern.compile("(.*)");
        Matcher matcher = compile.matcher("1.5");
        
        //group 0 表示全部的匹配 group 1 表示正则中第一个分组的匹配结果 
        //eg : "(.*)\\Q.\\E.*" group 1就是(.*)的匹配结果
        if(matcher.matches()){
            int groupCount = matcher.groupCount();
            for (int i = 0; i <= groupCount; i++) {
                String value = matcher.group(i);
                System.out.println(value);
            }
        }
    }

}
