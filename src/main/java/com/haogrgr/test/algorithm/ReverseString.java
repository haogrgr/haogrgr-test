package com.haogrgr.test.algorithm;

/**
 * 使用递归实现倒序字符串(不是倒序输出,是返回倒序后的字符串)
 * 
 * @date 2015年3月23日 下午5:49:17
 * @author haogrgr
 */
public class ReverseString {

	public static void main(String[] args) {
		System.out.println(reverse("123456789"));
	}
	
	public static String reverse(String input){
		if(input == null || input.length() == 0){
			throw new IllegalArgumentException("参数为空!");
		}else if(input.length() == 1){
			return input;
		}else if(input.length() == 2){
			return input.charAt(1) + "" + input.charAt(0);
		}else {
			int len = input.length() - 2;
			return reverse(input.substring(len)) + reverse(input.substring(0, len));
		}
	}
	
}
