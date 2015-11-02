package com.haogrgr.test.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.haogrgr.test.util.PageInfo;

public interface BaseMapper<T, K> {

    public T findById(K id);
    
    public List<T> findByPage(PageInfo<T> pageInfo);

    public Integer findByPageCount(PageInfo<T> pageInfo);
    
    public Integer update(T obj);
    
    public List<T> all();

    public Integer count();

    public Integer insert(T obj);

    public Integer inserts(@Param("list") List<T> list);

    public Integer delete(K id);

    public Integer deletes(@Param("ids") K[] ids);
    
    public List<T> load(@Param("ids") K[] ids);

}
