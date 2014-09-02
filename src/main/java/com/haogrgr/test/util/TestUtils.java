package com.haogrgr.test.util;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanUtils;

import com.haogrgr.test.model.PageInfo;

public class TestUtils {
    
    public static void main(String[] args) {
        TestUtils.beanCopyCode(PageInfo.class, "info", "xxx");
    }
    
    public static void beanCopyCode(Class<?> srcClz, String srcName, String destName){
        StringBuilder sb = new StringBuilder();
        
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(srcClz);
        for (PropertyDescriptor pd : pds) {
            if(pd.getWriteMethod() != null && pd.getReadMethod() != null){
                sb.append(srcName).append(".").append(pd.getWriteMethod().getName())
                .append("(" + destName + ".").append(pd.getReadMethod().getName())
                .append("())").append("\n");
            }
        }
        
        System.out.println(sb);
    }
    
    
    
}
