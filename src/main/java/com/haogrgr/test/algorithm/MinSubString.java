package com.haogrgr.test.algorithm;

import java.util.BitSet;

/**
 * 找一个字符串中包含全部出现字符的最小子串
 * 一个字符串中含有n个字符，其中有m个不同的字符，n>>m，
 * 用最少的时间和空间找到包含所有这m个字符的最短的子串，不考虑特殊字符，只考虑字母数字即可。
 * 例如：
 * abccbaddac, 返回：cbad
 * aabcadbbbcca，返回：bcad
 * 
 * 思路:
 * 问题可以分解为:
 * 1.求出字符中不同的各个字符
 * 2.寻找包含给定字符集合的最小子串 http://segmentfault.com/blog/code/1190000000458960
 */
public class MinSubString {

	public static void main(String[] args) {
		char[] seq = "aabcadbbbcca".toCharArray();

		//0-9 -> 48-57 , A-Z -> 65-90 , a-z -> 97-122 
		BitSet mark = new BitSet(123);
		for (int i = 1; i < seq.length; i++) {
			mark.set(seq[i]);
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mark.length(); i++) {
			if (mark.get(i)) {
				sb.append((char) i);
			}
		}
		System.out.println(sb.toString());

		System.out.println(minWindow(new String(seq), sb.toString()));

	}

	public static String minWindow(String S, String T) {
		// 1. build the hashmap
		int[] tmap = new int[256];
		int[] smap = new int[256];
		int t_count = T.length();
		for (int i = 0; i < T.length(); i++) {
			tmap[T.charAt(i)]++;
		}

		int slen = S.length();
		int left = 0, right = 0;
		int ml = 0, mr = 0, min = Integer.MAX_VALUE;
		int count = 0;
		boolean found = false;
		while (right < slen) {
			// 1. right++ until find the a window
			char c;
			while (right < slen && count < t_count) {
				c = S.charAt(right);
				if (smap[c] < tmap[c])
					count++;
				smap[c]++;
				right++;
			}
			if (count != t_count) {
				break;
			}

			// 2. left++ until broke the window
			while (left < right && count == t_count) {
				c = S.charAt(left);
				smap[c]--;
				if (smap[c] < tmap[c]) {
					count--;
					break;
				}
				left++;
			}

			// 3. window is [left, right)
			if (right - left < min) {
				min = right - left;
				ml = left;
				mr = right;
				found = true;
			}
			left++;
		}

		if (found == false)
			return "";

		return S.substring(ml, mr);
	}

}
