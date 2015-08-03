package com.haogrgr.test.util;

import java.util.Random;

/**
 * 生成随机数,随机字符串的工具类
 * Description: 生成随机数,随机字符串的工具类
 * Author: desheng.tu
 * Date: 2014年4月23日
 */
public class RandomUtil {

    public static void main(String args[]) throws Exception {
        for (int i = 0; i < 100; i++) {
            System.out.println(genRandomStr(12));
        }
    }
    
    /**
     * 获取0 - n(不包括n)
     * @param n 0-n范围
     * @return 随机数
     */
    public static int random(int n){
        Random random = new Random();
        return random.nextInt(n);
    }
    
    /**
     * 生成指定长度的随机字符串(数字+小写字母组合)
     * @param length 随机串的长度
     * @return 随机串
     */
    public static String genRandomStr(int length){
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length + 2);
        int index = 0;
        while(index < length){
            int r = random.nextInt(2);
            switch (r) {
                case 0:
                    char n = (char)(random.nextInt(10) + 48);
                    sb.append(n);
                    break;
                case 1:
                    char a = (char)(random.nextInt(26) + 97);
                    sb.append(a);
                    break;
                case 2:
                    char A = (char)(random.nextInt(26) + 65);
                    sb.append(A);
                    break;
                default:
                break;
            }
            index++;
        }
        
        return sb.toString();
    }
    
}
