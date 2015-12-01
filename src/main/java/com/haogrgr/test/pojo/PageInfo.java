package com.haogrgr.test.pojo;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 分页 实体类 （封装分页请求和结果数据）
 */
public class PageInfo<T> implements Serializable {

	private static final long serialVersionUID = 5367449251268716436L;

	public static final int DEFAULT_PAGE_SIZE = 10;

	/** 分页信息 */
	private int pageSize = DEFAULT_PAGE_SIZE; // 每页记录条数
	private int pageNo = 1; // 页码 从1开始

	/** 查询参数 **/
	private Map<String, Object> params; //查询条件

	/** 结果数据 */
	private Integer total; // 总记录数
	private List<T> rows; // 当前页显示数据
	private Map<String, Object> result;//其他的要显示的数据

	public PageInfo() {
		super();
	}

	public PageInfo(Integer pageNo, Integer pageSize) {
		this.pageNo = pageNo != null ? pageNo : 1;
		this.pageSize = pageSize != null ? pageSize : PageInfo.DEFAULT_PAGE_SIZE;
	}

	/**
	 * 添加查询参数
	 * @param key 属性名
	 * @param value 属性值(为空则不添加)
	 * @return this
	 */
	public PageInfo<T> addParam(String key, Object value) {
		if (value != null) {
			if (this.params == null) {
				this.params = new HashMap<String, Object>(8);
			}
			this.params.put(key, value);
		}
		return this;
	}

	/**
	 * 添加非空字符串查询参数
	 * @param key 属性名
	 * @param value 属性值(为空则不添加)
	 * @return this
	 */
	public PageInfo<T> addParamIfNotBlank(String key, String value) {
		if (value != null && value.trim().length() > 0) {
			addParam(key, value);
		}
		return this;
	}

	/**
	 * 添加满足条件的查询参数
	 * @param key 属性名
	 * @param value 属性值(为空则不添加)
	 * @param exp 条件, 如果为true则添加, 否则不添加
	 * @return this
	 */
	public PageInfo<T> addParam(String key, Object value, Boolean exp) {
		if (exp) {
			addParam(key, value);
		}
		return this;
	}

	/**
	 * 添加结果
	 * @param key 属性名
	 * @param value 属性值(为空则不添加)
	 * @return this
	 */
	public PageInfo<T> addResult(String key, Object value) {
		if (value != null) {
			if (this.result == null) {
				this.result = new HashMap<String, Object>(8);
			}
			this.result.put(key, value);
		}
		return this;
	}

	/**
	 * 获取分页begin参数 limit #{offset}, #{pageSize}
	 */
	@JsonIgnore
	public int getOffset() {
		Integer begin = (pageNo - 1) * pageSize;
		return begin;
	}

	/** 
	 * 获取mysql分页语句  ' limit getOffset(), getPageSize() '
	 */
	public String getLimitString() {
		return new StringBuilder(20).append(" limit ").append(getOffset()).append(", ").append(getPageSize())
				.append(" ").toString();
	}

	//get set

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	@JsonIgnore
	public Map<String, Object> getParams() {
		return params == null ? Collections.emptyMap() : params;
	}

	public void setParams(Map<String, Object> params) {
		Objects.requireNonNull(params);
		this.params = params;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public Map<String, Object> getResult() {
		return result == null ? Collections.emptyMap() : result;
	}

	public void setResult(Map<String, Object> result) {
		Objects.requireNonNull(result);
		this.result = result;
	}

	@Override
	public String toString() {
		return "PageInfo [pageSize=" + pageSize + ", pageNo=" + pageNo + ", params=" + params + "]";
	}

}
