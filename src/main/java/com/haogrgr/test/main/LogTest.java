package com.haogrgr.test.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haogrgr.test.log.LogUtils;

public class LogTest {
    
    private static final Logger logger = LoggerFactory.getLogger(LogTest.class);
    
    public static void main(String[] args) {
        LogUtils.info("这是在文件中的");
        logger.info("这个是在控制台的");
    }
    
}
