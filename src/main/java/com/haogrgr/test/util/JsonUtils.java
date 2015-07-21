package com.haogrgr.test.util;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.haogrgr.test.model.TestModel;

public class JsonUtils {

	private static ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) {
		String json = toJson(MapBuilder.makeO("aaaa", "bbbb").build("ccccc", "ddddd"));
		System.out.println(json);

		Map<String, String> map = toMap(json, String.class);
		System.out.println(map);

		//==============

		json = toJson(Lists.newArrayList(1, 2, 3, 4, 5));
		System.out.println(json);

		List<Integer> list = toList(json, Integer.class);
		System.out.println(list);

		//==============

		json = toJson(new TestModel().setAge(1).setName("haogrgr"));
		System.out.println(json);

		TestModel bean = toBean(json, TestModel.class);
		System.out.println(bean);
	}

	/**
	 * 对象转换为json
	 * @param obj 要转换为json的对象
	 */
	public static String toJson(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * json转换为map, 返回的map中key为String类型, value为vclass类型
	 */
	public static <V> Map<String, V> toMap(String json, Class<V> vclass) {
		try {
			return mapper.readValue(json, new TypeReference<Map<String, V>>() {});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * json转换为list, 返回的list类型为clazz
	 */
	public static <T> List<T> toList(String json, Class<T> clazz) {
		try {
			return mapper.readValue(json, new TypeReference<List<T>>() {});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * json转换为指定类型的bean
	 */
	public static <T> T toBean(String json, Class<T> clazz) {
		try {
			return mapper.readValue(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
