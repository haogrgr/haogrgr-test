package com.haogrgr.test.server;

import java.util.List;

import com.haogrgr.test.dao.BaseMapper;
import com.haogrgr.test.pojo.PageInfo;

/**
 * 通用Service接口
 * 
 * @author desheng.tu
 * @date 2015年12月1日 下午4:39:52 
 * 
 * @param <T> Model类型
 * @param <PK> 主键类型
 */
public interface BaseService<T, PK> {

	/**
	 * 获取底层对应的Mapper类
	 */
	public BaseMapper<T, PK> getMapper();

	/**
	 * 根据主键查找记录, 返回对应记录
	 */
	public T findById(PK pk);

	/**
	 * 分页查询, 返回分页对象, 包含分页记录, 和总记录数
	 */
	public <M> PageInfo<M> findByPage(PageInfo<M> page);

	/**
	 * 分页查询, 返回分页列表
	 */
	public <M> List<M> findByPageList(PageInfo<M> page);

	/**
	 * 分页查询, 返回总记录数
	 */
	public <M> Integer findByPageCount(PageInfo<M> page);

	/**
	 * 根据主键批量查找对应的记录, 返回对应记录
	 */
	public List<T> load(List<PK> pks);

	/**
	 * 修改记录, 返回修改的记录数
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
	public Integer inserts(List<T> records);

	/**
	 * mysql批量插入记录, 按betchSize分批提交, 返回插入记录条数.
	 */
	public Integer inserts(List<T> records, int betchSize);

	/**
	 * 根据主键删除, 返回删除记录数
	 */
	public Integer delete(PK pk);

	/**
	 * 根据主键批量删除, 返回删除记录数, 注意: 联合主键, 不支持该操作
	 */
	public Integer deletes(List<PK> pks);

}
