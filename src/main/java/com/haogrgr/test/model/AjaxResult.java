package com.haogrgr.test.model;

import java.io.Serializable;

public class AjaxResult implements Serializable {

    public static final String SUCC = "1";
    public static final String FAIL_BIZ = "0";
    public static final String FAIL_SYS = "-1";

    private static final long serialVersionUID = -8528217215968400487L;

    private String succ;//是否成功. 1:成功, 0:业务异常失败, -1:系统异常失败
    private String info;//提示消息
    private Object data;//返回数据
    private String time;//响应时间

    public AjaxResult() {
    }

    private AjaxResult(String succ, String info, Object data) {
        this.succ = succ;
        this.info = info;
        this.data = data;
        this.time = "" + System.currentTimeMillis();
    }

    public static AjaxResult succ(String info, Object data) {
        AjaxResult result = new AjaxResult(SUCC, info, data);
        return result;
    }
    
    public static AjaxResult succ(String info) {
        AjaxResult result = new AjaxResult(SUCC, info, null);
        return result;
    }

    public static AjaxResult failBiz(String info, Object data) {
        AjaxResult result = new AjaxResult(FAIL_BIZ, info, data);
        return result;
    }
    
    public static AjaxResult failBiz(String info) {
        AjaxResult result = new AjaxResult(FAIL_BIZ, info, null);
        return result;
    }
    
    public static AjaxResult failSys(String info, Object data) {
        AjaxResult result = new AjaxResult(FAIL_SYS, info, data);
        return result;
    }
    
    public static AjaxResult failSys(String info) {
        AjaxResult result = new AjaxResult(FAIL_SYS, info, null);
        return result;
    }

    public String getSucc() {
        return succ;
    }

    public void setSucc(String succ) {
        this.succ = succ;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "AjaxResult [succ=" + succ + ", info=" + info + ", data=" + data + ", time=" + time + "]";
    }

}
