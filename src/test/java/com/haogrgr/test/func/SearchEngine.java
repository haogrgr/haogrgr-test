package com.haogrgr.test.func;

import java.util.List;

/**
 * 搜索引擎
 */
public interface SearchEngine {

	/**
	 * 搜索
	 * @param query 搜索关键字
	 * @return 搜索结果
	 */
	List<String> search(String query);
	
}
