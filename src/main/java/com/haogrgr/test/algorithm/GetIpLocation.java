package com.haogrgr.test.algorithm;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.math.LongRange;

import com.google.common.base.Stopwatch;

/**
 * 题目: 有一个文件, 内容如下, 内容为IP段和其所对应的区域名, IP段不交叉, 大概有50w行
 * 
 * 127.0.0.1 127.0.1.1 localhost1 
 * 127.0.3.1 127.0.4.1 localhost2 
 * ....
 * 
 * 要求: 实现给定IP, 获取到所在IP段对应的区域名
 * 
 * 思路: IP 127.0.0.1 可以转换为  Long(127_000_000_001)
 *      这样, IP(127.0.0.1-127.0.1.1) 可以转化为Long(127_000_000_001-127_000_001_001)
 *      这样, 可以将转换后的Long范围的结束数字放入TreeMap, 如
 *      {127.0.1.1=localhost1, 127.0.4.1=localhost2}
 *      
 *      查询时, 如 127.0.2.1, 通过TreeMap的ceilingEntry, 可以得到127.0.4.1=localhost2
 *      然后判断ip是否在ip段中, 比如这里就不在
 *      
 *      如果是127.0.0.6, 那么通过ceilingEntry得到的是127.0.1.1, 然后判断ip是否在ip段中, 比如这里就在段中
 *      
 * 总结: 把IP可以转化为Long, 然后通过TreeMap排序的特性, 将IP段中的上边界放入TreeMap中,
 *      查找时, 找出IP最接近的IP段的上边界, 然后再判断是否在IP段中.
 *      
 * @author desheng.tu
 * @date 2015年8月3日 下午3:48:14
 *
 */
public class GetIpLocation {

	public static void main(String[] args) {
		//初始化map, 为1000-1100, 2000-2100 ... 
		TreeMap<Long, RangeValue<String>> map = new TreeMap<>();
		for (long i = 1000, j = 0, step = 100; j < 500000; i = i + 1000, j++) {
			map.put(i + step, new RangeValue<String>(i, i + step, i + " - " + (i + step)));
		}

		System.out.println(map.size());

		Stopwatch watch = Stopwatch.createStarted();
		long count = 0;
		for (long i = 1; i < 10000; i++) {
			Entry<Long, RangeValue<String>> tail = map.ceilingEntry(i);
			if (tail != null && tail.getValue().containsLong(i)) {
				System.out.println(tail.getValue().getValue() + ",   " + i);
				count++;
			}
		}

		System.out.println(watch);
		System.out.println(count + ", 预测应该是909");
	}

	private static final class RangeValue<T> {

		private LongRange range;
		private T value;

		public RangeValue(Long start, Long end, T value) {
			this.range = new LongRange(start, end);
			this.value = value;
		}

		public boolean containsLong(Long i) {
			return range.containsLong(i);
		}

		public T getValue() {
			return value;
		}

	}
}
