package com.haogrgr.test.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * log4j配置文件中指定该工具类的日志输出到文件.
 * <logger name="com.haogrgr.test.log" additivity="false" >
 *      <level value="info" />
 *      <appender-ref ref="file"/>
 *      <appender-ref ref="stdout"/>
 * </logger>
 * <p>Description: 用于打印输出到文件的日志</p>
 * <p>Author: desheng.tu</p>
 * <p>Date: 2014年4月7日</p>
 */
public class LogUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);
    
    public static void error(String info, Throwable t){
        logger.error(info, t);
    }
    
}
