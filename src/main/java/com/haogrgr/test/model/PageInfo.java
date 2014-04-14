package com.haogrgr.test.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, Object> paramMap = new HashMap<String, Object>(); //查询条件
    private Object paramObj; //查询对象

    /** 结果数据 */
    private Integer total; // 总记录数
    private List<T> rows; // 当前页显示数据

    private boolean plugin = false; //是否走分页插件
    
    private String sqlId; //用于标识具体执行的分页标识
    
    public PageInfo() {
        super();
    }
    
    public PageInfo(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo != null ? pageNo : 1;
        this.pageSize = pageSize != null ? pageSize : PageInfo.DEFAULT_PAGE_SIZE;
    }
    
    public PageInfo(Integer pageNo, Integer pageSize, Object paramObj) {
        this.pageNo = pageNo != null ? pageNo : 1;
        this.pageSize = pageSize != null ? pageSize : PageInfo.DEFAULT_PAGE_SIZE;
        this.paramObj = paramObj;//防止sql配置文件中空判断时报空指针
    }
    /**
     * 获取分页begin参数 limit {begin}, {end}
     */
    @JsonIgnore
    public int getBegin() {
        Integer begin = (pageNo - 1) * pageSize;
        if (begin < 0) {
            begin = 0;
        }
        return begin;
    }

    /**
     * 获取分页end参数 limit {begin}, {end}
     */
    @JsonIgnore
    public int getEnd() {
        if (pageSize <= 0) {
            return DEFAULT_PAGE_SIZE;
        } else {
            return pageSize;
        }
    }

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
    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
    
    public void addParam(String key, Object value){
        this.paramMap.put(key, value);
    }
    
    @JsonIgnore
    public Object getParamObj() {
        return paramObj;
    }

    public void setParamObj(Object paramObj) {
        this.paramObj = paramObj;
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


    @JsonIgnore
    public boolean isPlugin() {
        return plugin;
    }

    public void setPlugin(boolean plugin) {
        this.plugin = plugin;
    }
    
    @JsonIgnore
    public String getSqlId() {
        return sqlId;
    }

    public void setSqlId(String type) {
        this.sqlId = type;
    }

}
