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
 * @param <PK> 主键类型
 */
public abstract class BaseServiceSupport<T, PK> implements BaseService<T, PK> {

	/**
	 * Spring4以上版本可以使用泛型注入
	 */
	public abstract BaseMapper<T, PK> getMapper();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public T findById(PK pk) {
		Objects.requireNonNull(pk, "主键为空");
		T record = getMapper().findById(pk);
		return record;
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <M> PageInfo<M> findByPage(PageInfo<M> page) {
		Objects.requireNonNull(page);

		List<M> rows = getMapper().findByPage(page);
		page.setRows(rows);

		Integer total = getMapper().findByPageCount(page);
		page.setTotal(total);

		return page;
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <M> List<M> findByPageList(PageInfo<M> page) {
		Objects.requireNonNull(page);
		List<M> rows = getMapper().findByPage(page);
		return rows;
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <M> Integer findByPageCount(PageInfo<M> page) {
		Objects.requireNonNull(page);
		Integer total = getMapper().findByPageCount(page);
		return total;
	}

	public List<T> load(List<PK> pks) {
		Assert.isTrue(Objects.requireNonNull(pks).size() == 0, "pks不能为空");
		return getMapper().load(pks);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer update(T record) {
		Objects.requireNonNull(record);
		Integer update = getMapper().update(record);
		return update;
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<T> all() {
		return getMapper().all();
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Integer count() {
		return getMapper().count();
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer insert(T record) {
		Objects.requireNonNull(record);
		Integer insert = getMapper().insert(record);
		return insert;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer inserts(List<T> records) {
		Assert.isTrue(Objects.requireNonNull(records).size() == 0, "records不能为空");
		Integer inserts = getMapper().inserts(records);
		return inserts;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer inserts(List<T> records, int betchSize) {
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
				inserts = inserts + getMapper().inserts(list);
			}
		}
		else {//直接插入
			inserts = getMapper().inserts(records);
		}
		return inserts;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer delete(PK pk) {
		Objects.requireNonNull(pk);
		Integer delete = getMapper().delete(pk);
		return delete;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer deletes(List<PK> pks) {
		Assert.isTrue(Objects.requireNonNull(pks).size() == 0, "pks不能为空");
		Integer deletes = getMapper().deletes(pks);
		return deletes;
	}

}
