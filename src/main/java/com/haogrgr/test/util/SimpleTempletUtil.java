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
        Set<String> paramNames = getParamNames("dddd$$aaa$$$$bbb$$ccc$$", "$$");
        System.out.println(paramNames);
        
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("name", "haogrgr");
        context.put("order", new Object());
        String render = render("你好$$name$$, 您的订单号 $$order$$ 已经发货!", context);
        System.err.println(render);
    }
    
    /**
     * 使用context中对应的值替换templet中用$$包围的变量名(也是context的key)
     * @param templet 模板
     * @param context 用于替换模板中的变量
     * @return 例如  参数 : dddd$$aaa$$$$bbb$$ccc$$, $$, {<aaa, value1>, <bbb, value2>}  结果:ddddvalue1value2ccc$$
     */
    public static String render(String templet, Map<String, ?> context) {
        return render(templet, DEFAULT_SPLIT, context);
    }
    
    /**
     * 使用context中对应的值替换templet中用split包围的变量名(也是context的key)
     * @param templet 模板
     * @param split 用于标识变量名的标志
     * @param context 用于替换模板中的变量
     * @return 例如  参数 : dddd$$aaa$$$$bbb$$ccc$$, $$, {<aaa, value1>, <bbb, value2>}  结果:ddddvalue1value2ccc$$
     */
    public static String render(String templet, String split, Map<String, ?> context) {
        if(context == null || context.size() == 0){
            return templet;
        }
        if(templet == null || templet.trim().length() == 0){
            return null;
        }
        
        Set<String> paramNames = getParamNames(templet, split);

        for (String name : paramNames) {
            Object obj = context.get(name);
            if(obj == null){
                obj = "";
            }
            String regex = "\\Q" + split + name + split + "\\E";
            templet = templet.replaceAll(regex, obj.toString());
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
            
            String param = templet.substring(start, end);
            paramNames.add(param);
            //System.out.println(param + "===" + start + "===" + end);
            
            end = end + split.length();
        }
        return paramNames;
    }
    
}
