package com.haogrgr.test.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haogrgr.test.dao.BaseMapper;
import com.haogrgr.test.model.BaseModel;
import com.haogrgr.test.util.ExpUtil;
import com.haogrgr.test.util.PageInfo;

public abstract class BaseServiceSupport<T extends BaseModel, K> implements BaseService<T, K> {

	/**
	 * Spring4以上版本可以使用泛型注入,但是,不太喜欢
	 */
    public abstract BaseMapper<T, K> getMapper();

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public T findById(K id) {
        ExpUtil.throwExp(id == null, "ID为空");
        T result = getMapper().findById(id);
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<T> findByPage(PageInfo<T> pageInfo) {
        ExpUtil.throwExp(pageInfo == null, "参数为空");
        List<T> page = getMapper().findByPage(pageInfo);
        Integer count = getMapper().findByPageCount(pageInfo);
        pageInfo.setRows(page);
        pageInfo.setTotal(count);
        return page;
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<T> findByPageList(PageInfo<T> pageInfo) {
        ExpUtil.throwExp(pageInfo == null, "参数为空");
        List<T> page = getMapper().findByPage(pageInfo);
        pageInfo.setRows(page);
        return page;
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer findByPageCount(PageInfo<T> pageInfo) {
        ExpUtil.throwExp(pageInfo == null, "参数为空");
        Integer count = getMapper().findByPageCount(pageInfo);
        return count;
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
    public Integer insert(T obj) {
        ExpUtil.throwExp(obj == null, "参数为空");
        if (obj.getModifyTime() == null) {
            obj.setModifyTime(new Date());
        }
        if (obj.getCreateTime() == null) {
            obj.setCreateTime(new Date());
        }
        Integer insert = getMapper().insert(obj);
        return insert;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer inserts(List<T> list) {
        ExpUtil.throwExp(list == null, "参数为空");
        for (T obj : list) {
            if (obj.getModifyTime() == null) {
                obj.setModifyTime(new Date());
            }
            if (obj.getCreateTime() == null) {
                obj.setCreateTime(new Date());
            }
        }
        Integer inserts = 0;
        if (list.size() > 0) {
            inserts = getMapper().inserts(list);
        }
        return inserts;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer inserts(List<T> list, int betchSize) {
        Integer inserts = 0;
        if (list.size() < 0) {
            return 0;
        }

        ExpUtil.throwExp(list == null, "参数为空");
        for (T obj : list) {
            if (obj.getModifyTime() == null) {
                obj.setModifyTime(new Date());
            }
            if (obj.getCreateTime() == null) {
                obj.setCreateTime(new Date());
            }
        }
        if (list.size() > betchSize) {//分页批量插入
            int page = list.size() / betchSize;
            if (page * betchSize < list.size()) {
                page++;
            }
            for (int i = 0; i < page; i++) {
                ArrayList<T> temp = new ArrayList<T>(betchSize + 3);
                int end = (i + 1) * betchSize;
                for (int j = i * betchSize; j < end && j < list.size(); j++) {
                    temp.add(list.get(j));
                }
                inserts = inserts + getMapper().inserts(temp);
            }
        } else {
            inserts = getMapper().inserts(list);
        }
        return inserts;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer update(T obj) {
        ExpUtil.throwExp(obj == null, "参数为空");
        ExpUtil.throwExp(obj.getId() == null, "ID为空");
        if(obj.getModifyTime() == null){
            obj.setModifyTime(new Date());
        }
        Integer update = getMapper().update(obj);
        return update;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer delete(K id) {
        ExpUtil.throwExp(id == null, "ID为空");
        Integer delete = getMapper().delete(id);
        return delete;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer deletes(K[] ids) {
        ExpUtil.throwExp(ids == null, "参数为空");
        for (K id : ids) {
            ExpUtil.throwExp(id == null, "ID为空");
        }
        Integer count = getMapper().deletes(ids);
        return count;
    }

    public Map<Integer, T> toMap(List<T> list) {
        ExpUtil.throwExp(list == null, "列表为Null");
        Map<Integer, T> map = new HashMap<Integer, T>(list.size() + 1);
        for (T entity : list) {
            map.put(entity.getId(), entity);
        }
        return map;
    }
    
    public Integer[] getIds(List<T> list){
        ExpUtil.throwExp(list == null, "列表为Null");
        Integer[] ids = new Integer[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ids[i] = list.get(i).getId();
        }
        return ids;
    }
}
