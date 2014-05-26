package com.haogrgr.test.exception;

/**
 * 业务异常,用于代表业务逻辑的异常 
 * <p>Description: 业务异常</p> 
 * <p>Author: desheng.tu</p> 
 * <p>Date: 2014年5月26日</p>
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 8890097205411532278L;

    private String msg;

    public BizException() {
    }

    public BizException(String message) {
        super(message);
        this.msg = message;
    }

    public BizException(String message, String debug) {
        super(message + "@" + debug);
        this.msg = message;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
        this.msg = message;
    }

    public BizException(String message, Throwable cause, String debug) {
        super(message + "@" + debug, cause);
        this.msg = message;
    }

    public String getMsg() {
        return this.msg;
    }

}
