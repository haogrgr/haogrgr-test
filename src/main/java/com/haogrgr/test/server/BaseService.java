package com.haogrgr.test.server;

import java.util.List;

import com.haogrgr.test.dao.BaseMapper;
import com.haogrgr.test.model.BaseModel;
import com.haogrgr.test.util.PageInfo;

public interface BaseService<T extends BaseModel, K> {
    
    public BaseMapper<T, K> getMapper();
    
    public T findById(K id);

    public List<T> findByPage(PageInfo<T> pageInfo);
    
    public List<T> findByPageList(PageInfo<T> pageInfo);
    
    public Integer findByPageCount(PageInfo<T> pageInfo);

    public List<T> all();

    public Integer count();

    public Integer insert(T obj);

    public Integer inserts(List<T> list);
    
    public Integer inserts(List<T> list, int betchSize);

    public Integer update(T obj);

    public Integer delete(K id);

    public Integer deletes(K[] ids);
    
}
