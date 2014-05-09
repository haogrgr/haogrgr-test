package com.haogrgr.test.util;

import java.security.MessageDigest;

public class StringUtils {

    public static void main(String[] args) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        
        messageDigest.update("admin".getBytes("UTF-8"));
        
        byte[] digest = messageDigest.digest();
        
        System.out.println(byteToHex(digest));
        
        System.out.println("21232f297a57a5a743894a0e4a801fc3".equals(byteToHex(digest)));
    }
    
    public static String byteToHex(byte[] bytes){
        if(bytes == null){
            return null;
        }
        
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            int hex = bytes[i] & 0xff;
            if(hex < 16){
                sb.append("0");
            }
            sb.append(Integer.toString(hex, 16));
        }
        return sb.toString();
    }
    
}
