package com.haogrgr.test.model;

import java.util.Date;

public abstract class BaseModel<K> implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    protected K id;
    protected Date modifyTime;
    protected Date createTime;
    
    public BaseModel() {
    }
    
    public BaseModel(K id){
        this.id = id;
    }
    
    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
