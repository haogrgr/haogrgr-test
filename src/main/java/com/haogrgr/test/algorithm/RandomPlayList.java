package com.haogrgr.test.algorithm;

import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.math.RandomUtils;

import com.haogrgr.test.util.MapBuilder;

/**
 * 百度面试题目:
 * 假设张三的mp3里有1000首歌，现在希望设计一种随机算法来随机播放。
 * 与普通随机模式不同的是，张三希望每首歌被随机到的改了吧是与一首歌的豆瓣评分（0~10分）成正比的，
 * 如朴树的《平凡之路》评分为8.9分，逃跑计划的《夜空中最亮的星》评分为9.5分，
 * 则希望听《平凡之路》的概率与《夜空中最亮的星》的概率比为89:95。
 * 现在我们已知这1000首歌的豆瓣评分： 
 * （1）请设计一种随机算法来满足张三的需求。 
 * （2）请写代码实现自己的算法
 * 
 * 思路:
 * 1000首歌的总评分 * 10 = 总分数
 * 创建一个数组长度为总分数
 * 将1000首歌根据 分数*10 分配到总分数数组里
 * 随机一个数,为索引,取对应的歌曲
 * 
 * 优化:
 * 创建数组没必要,浪费内存,采用连续区间,区间用treemap表示, 如:{0=平凡之路, 89=平凡之路, (89+95=184)=夜空中最亮的星}
 * 这里就有三个区间 0-89为平凡之路, 89 - 184为夜空中最亮的星
 * 使用TreeMap.tailMap.firstKey根据random[0-184]数找到最近的区间
 * TreeMap.tailMap(random)返回的是map中key大于或等于random的视图, firstKey就是获取第一个大于random的key
 */
public class RandomPlayList {

	public static void main(String[] args) {
		Map<String, Integer> list = MapBuilder.make("平凡之路", 89).put("夜空中最亮的星", 95).build("浮夸", 90);

		TreeMap<Integer, Entry<String, Integer>> map = new TreeMap<>();

		Integer left = 0;
		for (Entry<String, Integer> entry : list.entrySet()) {
			left = left + entry.getValue();
			if (left == entry.getValue()) {
				map.put(0, entry);
			}
			map.put(left, entry);
		}
		System.out.println(map);

		Integer random = RandomUtils.nextInt(left + 1);
		SortedMap<Integer, Entry<String, Integer>> tailMap = map.tailMap(random);
		Integer first = tailMap.firstKey();
		
		System.out.println(random + ", " + first);
		System.out.println(map.get(first));

	}

}
