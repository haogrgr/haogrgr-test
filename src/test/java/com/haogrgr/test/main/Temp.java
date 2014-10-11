package com.haogrgr.test.main;

public class Temp {

	public static void main(String[] args) throws Exception {
		long count = 0;
		for (int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE - 1; i++) {
			byte x = (byte) (i & 0xff);
			byte y = (byte) i;
			if (x != y)
				System.out.println(x + ", " + y);
			count++;
		}
		long x = (long) Integer.MAX_VALUE + (long) Integer.MAX_VALUE;
		System.out.println(count + ", " + x);
	}
}
