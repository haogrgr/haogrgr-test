package com.haogrgr.test.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.haogrgr.test.pojo.PageInfo;

/**
 * 通用Mapper接口
 * 
 * @author desheng.tu
 * @date 2015年12月1日 下午4:15:29 
 * 
 * @param <T> 实体类型
 * @param <PK> 主键类型
 */
public interface BaseMapper<T, PK> {

	/**
	 * 根据主键查找记录, 返回对应记录
	 */
	public T findById(PK pk);

	/**
	 * 分页查询, 返回分页列表
	 */
	public List<T> findByPage(PageInfo<T> pageInfo);

	/**
	 * 分页查询, 返回总记录数
	 */
	public Integer findByPageCount(PageInfo<T> pageInfo);

	/**
	 * 根据主键批量查找对应的记录, 返回对应记录
	 */
	public List<T> load(@Param("pks") List<PK> pks);

	/**
	 * 修改记录
	 */
	public Integer update(T record);

	/**
	 * 查询所有记录
	 */
	public List<T> all();

	/**
	 * 查询总记录数
	 */
	public Integer count();

	/**
	 * 插入记录, 返回插入记录数(0 or 1)
	 */
	public Integer insert(T record);

	/**
	 * mysql批量插入记录, 返回插入记录条数.
	 */
	public Integer inserts(@Param("records") List<T> records);

	/**
	 * 根据主键删除, 返回删除记录数
	 */
	public Integer delete(PK pk);

	/**
	 * 根据主键批量删除, 返回删除记录数, 注意: 联合主键, 不支持该操作
	 */
	public Integer deletes(@Param("pks") List<PK> pks);

}
