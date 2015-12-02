package com.haogrgr.test.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.haogrgr.test.dao.BaseMapper;
import com.haogrgr.test.pojo.PageInfo;

/**
 * 通用Service类基类, 子类需实现getMapper方法
 * 
 * @author desheng.tu
 * @date 2015年12月1日 下午4:49:35 
 * 
 * @param <T> Model类型
 * @param <K> 主键类型
 */
public abstract class BaseServiceSupport<T, K> implements BaseService<T, K> {

	/**
	 * Spring4以上版本可以使用泛型注入
	 */
	public abstract BaseMapper<T, K> getMapper();

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public T getById(K id) {
		Objects.requireNonNull(id, "主键为空");
		T record = getMapper().getById(id);
		return record;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<T> getByIds(List<K> ids) {
		Assert.isTrue(Objects.requireNonNull(ids).size() == 0, "ids不能为空");
		return getMapper().getByIds(ids);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<T> getByPojo(T pojo) {
		Objects.requireNonNull(pojo);
		return getMapper().getByPojo(pojo);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <M> PageInfo<M> getByPage(PageInfo<M> page) {
		Objects.requireNonNull(page);

		List<M> rows = getMapper().getByPageList(page);
		page.setRows(rows);

		Integer total = getMapper().getByPageCount(page);
		page.setTotal(total);

		return page;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <M> List<M> getByPageList(PageInfo<M> page) {
		Objects.requireNonNull(page);
		List<M> rows = getMapper().getByPageList(page);
		return rows;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <M> Integer getByPageCount(PageInfo<M> page) {
		Objects.requireNonNull(page);
		Integer total = getMapper().getByPageCount(page);
		return total;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<T> getAll() {
		return getMapper().getAll();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Integer getAllCount() {
		return getMapper().getAllCount();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer modify(T record) {
		Objects.requireNonNull(record);
		Integer update = getMapper().modify(record);
		return update;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer modefySelective(T record) {
		Objects.requireNonNull(record);
		Integer update = getMapper().modefySelective(record);
		return update;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer save(T record) {
		Objects.requireNonNull(record);
		Integer insert = getMapper().save(record);
		return insert;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer saveBatch(List<T> records) {
		Assert.isTrue(Objects.requireNonNull(records).size() == 0, "records不能为空");
		Integer inserts = getMapper().saveBatch(records);
		return inserts;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer saveBatch(List<T> records, int betchSize) {
		Assert.isTrue(betchSize > 0, "betchSize不能小于0");
		Assert.isTrue(Objects.requireNonNull(records).size() == 0, "records不能为空");

		Integer inserts = 0;
		if (records.size() > betchSize) {//分页批量插入
			int pageSize = records.size() / betchSize;
			if (pageSize * betchSize < records.size()) {
				pageSize++;
			}

			for (int i = 0; i < pageSize; i++) {
				ArrayList<T> list = new ArrayList<T>(betchSize);
				int end = (i + 1) * betchSize;
				for (int j = i * betchSize; j < end && j < records.size(); j++) {
					list.add(records.get(j));
				}
				inserts = inserts + getMapper().saveBatch(list);
			}
		}
		else {//直接插入
			inserts = getMapper().saveBatch(records);
		}
		return inserts;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer delById(K id) {
		Objects.requireNonNull(id);
		Integer del = getMapper().delById(id);
		return del;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer delByIds(List<K> ids) {
		Assert.isTrue(Objects.requireNonNull(ids).size() == 0, "ids不能为空");
		Integer dels = getMapper().delByIds(ids);
		return dels;
	}

}
