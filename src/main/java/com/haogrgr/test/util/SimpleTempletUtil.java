package com.haogrgr.test.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 基于字符串替换的简单模板
 * <p>Description: 基于字符串替换的简单模板</p>
 * <p>Author: desheng.tu</p>
 * <p>Date: 2014年4月18日</p>
 */
public class SimpleTempletUtil {
    
    public static final String DEFAULT_SPLIT = "$$";
    
    public static void main(String[] args) {
        Set<String> paramNames = getParamNames("dddd$$aaaccc$$$$bbb$$ccc$$aaa$$", "$$");
        System.out.println(paramNames);
        
        Map<String, String> context = new HashMap<String, String>();
        context.put("aaaccc", "value1");
        context.put("bbb", "value2");
        String render = render("dddd$$aaaccc$$$$bbb$$ccc$$aaa$$", null );
        System.out.println(render);
    }
    
    /**
     * 使用context中对应的值替换templet中用$$包围的变量名(也是context的key)
     * @param templet 模板
     * @param context 用于替换模板中的变量
     * @return 例如  参数 : dddd$$aaa$$$$bbb$$ccc$$, $$, {<aaa, value1>, <bbb, value2>}  结果:ddddvalue1value2ccc$$
     */
    public static String render(String templet, Map<String, String> context) {
        return render(templet, DEFAULT_SPLIT, context);
    }
    
    /**
     * 使用context中对应的值替换templet中用split包围的变量名(也是context的key)
     * @param templet 模板
     * @param split 用于标识变量名的标志
     * @param context 用于替换模板中的变量
     * @return 例如  参数 : dddd$$aaa$$$$bbb$$ccc$$, $$, {<aaa, value1>, <bbb, value2>}  结果:ddddvalue1value2ccc$$
     */
    public static String render(String templet, String split, Map<String, String> context) {
        if(context == null || context.size() == 0){
            return templet;
        }
        
        Set<String> paramNames = getParamNames(templet, split);
        
        //TODO:可以为context添加一些默认的变量,比如当前时间
        
        for (String name : paramNames) {
            String value = context.get(name);
            value = value == null ? "" : value;
            String regex = "\\Q" + split + name + split + "\\E";
            templet = templet.replaceAll(regex, value);
        }

        return templet;
    }
    
    /**
     * 根据分割符从模板中取得变量的名字($$变量名$$) eg:
     * $$aaa$$$$bbb$$ccc$$ 返回   aaa,bbb
     * @param templet 模板
     * @param split 包围变量名的字符串
     * @return 模板中的变量名
     */
    public static Set<String> getParamNames(String templet, String split) {
        Set<String> paramNames = new HashSet<String>();
        
        int start = 0, end = 0;
        while (end < templet.length()) {
            start = templet.indexOf(split, end);
            if (start == -1) {
                break;
            }
            start = start + split.length();
            
            end = templet.indexOf(split, start);
            if (end == -1) {
                break;
            }
            paramNames.add(templet.substring(start, end));
            end = end + split.length();
        }
        return paramNames;
    }
    
}
