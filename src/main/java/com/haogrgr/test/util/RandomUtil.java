package com.haogrgr.test.util;

import java.util.Random;

public class RandomUtil {

	public static void main(String args[]) throws Exception {
		for (int i = 0; i < 100; i++) {
			System.out.println(genRandomStr(12));
		}
	}
	
	public static int random(int n){
		Random random = new Random();
		return random.nextInt(n);
	}
	
	public static String genRandomStr(int length){
		Random random = new Random();
		StringBuilder sb = new StringBuilder(length + 5);
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
