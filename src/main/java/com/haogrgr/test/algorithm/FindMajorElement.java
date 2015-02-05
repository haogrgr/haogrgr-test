package com.haogrgr.test.algorithm;

/**
 * 题目:
 * Given an array of size n, find the majority element. The majority element is the element that appears more than n/2 times
 * You may assume that the array is non-empty and the majority element always exist in the array.
 * input: {1,2,3,3,3,4,3}  output: 3
 * 
 * 思路:
 * major元素满足这2个条件之一(or)
 * 1.结尾最少出现一次 比如[1,2,2,3,3,2,2]
 * 其中2和3都是连续出现2次,这中情况,比如是2结尾
 * 
 * 2.连续出现次数最多 比如 [1,2,2,2,3,3,1]
 * 如果不是出现在结尾,那么major元素2必须出现次数大于元素3
 */
public class FindMajorElement {

	public static void main(String[] args) {
		System.out.println(majorityElement2(new int[] { 2, 2, 2, 1, 4, 2, 1 }));
	}

	static int majorityElement2(int[] num) {
		int major = num[0];
		int count = 1; //连续出现次数,如果不连续,减减, 如果是major元素, 且major没有出现在结尾, 在减减为0之时, 必定会出现一次major,
		for (int i = 1; i < num.length; i++) {
			if (num[i] == major) {
				count++;
			} else {
				count--;
				if (count == 0) {
					major = num[i];
					count = 1;
				}
			}
		}
		return major;
	}

}
