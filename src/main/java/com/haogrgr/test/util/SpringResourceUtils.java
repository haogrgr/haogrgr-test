package com.haogrgr.test.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 使用Spring来加获取资源文件
 * 
 * <p>Author: desheng.tu</p>
 * <p>Date: 2014年8月12日</p>
 */
public class SpringResourceUtils {

    public static void main(String[] args) throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Resource[] resources = resolver.getResources("file://E:/mybatis/**/*.xml");

        for (Resource resource : resources) {
            System.out.println(resource);
        }
    }

}
