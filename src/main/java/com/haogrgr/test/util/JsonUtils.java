package com.haogrgr.test.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        String json = JsonUtils.fromMap(MapBuilder.makeO("aaaa", "bbbb").build("ccccc", "ddddd"));
        System.out.println(json);

        System.out.println(JsonUtils.forMap(json));
        
        ArrayList<String> list = new ArrayList<String>();
        list.add("aaaaa");
        list.add("bbbbb");
        
        json = JsonUtils.fromList(list);
        System.out.println(json);
        System.out.println(JsonUtils.forList(json));
        
    }
    
    public static String toJson(Object o){
    	try {
			return mapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public static Map<String, Object> forMap(String json) {
        try {
            return mapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("转换为map失败!");
        }
    }

    public static List<Object> forList(String json) {
        try {
            return mapper.readValue(json, List.class);
        } catch (Exception e) {
            throw new RuntimeException("转换为list失败!");
        }
    }

    public static String fromMap(Map<?, ?> map) {
        StringWriter sw = new StringWriter();
        try {
            mapper.writeValue(sw, map);
        } catch (Exception e) {
            throw new RuntimeException("转换为Json失败!");
        }
        return sw.toString();
    }
    
    public static String fromList(List<?> list) {
        StringWriter sw = new StringWriter();
        try {
            mapper.writeValue(sw, list);
        } catch (Exception e) {
            throw new RuntimeException("转换为Json失败!");
        }
        return sw.toString();
    }
}
