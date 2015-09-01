package com.haogrgr.test.util;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;

/**
 * Guava的BloomFilter包装
 * 
 * TODO: 包装思路
 * 
 * @see http://ifeve.com/volatile-array-visiblity/
 * 
 * @author desheng.tu
 * @date 2015年8月30日 下午2:05:41
 * 
 */
public final class HBBloomFilter<T> {

	private volatile BloomFilter<T> filter;

	/**
	 * 创建HBBloomFilter, 插入数为10w, 错误率为0.0001(万分之一) 时, 大概占用250kb内存, 12个hash函数
	 * @param funnel 方便Hash
	 * @param expectedInsertions 预计要插入的记录数
	 * @param fpp 允许的错误率
	 */
	public HBBloomFilter(Funnel<? super T> funnel, int expectedInsertions, double fpp) {
		this.filter = BloomFilter.create(funnel, expectedInsertions, fpp);
	}

	/**
	 * 将元素加入BloomFilter
	 * @return true:可能是第一次put, false:一定是第一次put
	 */
	public boolean put(T t) {
		final BloomFilter<T> temp = filter;
		boolean result = temp.put(t);
		filter = temp;
		return result;
	}

	/**
	 * 查看元素是否存在
	 * @return true:可能包含t, false:一定不包含t
	 */
	public boolean mightContain(T t) {
		return filter.mightContain(t);
	}

}
