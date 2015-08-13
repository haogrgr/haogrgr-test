package com.haogrgr.test.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.haogrgr.test.model.TestModel;

/**
 * json 工具类, 简单的封装, 其实就是吧json的异常换成了运行期异常...
 * 
 * @author desheng.tu
 * @date 2015年8月13日 下午7:03:38 
 *
 */
public class JsonUtils {

	private static ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		String json = toJson(Maps.of("1", "11", "2", "22"));
		System.out.println(json);

		Map<String, String> map = toObj(json, new TypeReference<Map<String, String>>() {});
		System.out.println(map);

		//==============

		json = toJson(Lists.newArrayList(1, 2, 3, 4, 5));
		System.out.println(json);

		List<Integer> list = toObj(json, new TypeReference<List<Integer>>() {});
		System.out.println(list);

		//==============

		json = toJson(new TestModel().setAge(1).setName("haogrgr"));
		System.out.println(json);

		TestModel bean = toBean(json, TestModel.class);
		System.out.println(MoreObjects.toStringHelper(bean).toString());
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
	 * json转换为obj, 并指定type, 如: new TypeReference<Map<String, Bean>>() {}
	 */
	public static <T> T toObj(String json, TypeReference<T> type) {
		try {
			return mapper.readValue(json, type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * json转换为map, 不带泛型信息
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, ?> toMap(String json) {
		try {
			return mapper.readValue(json, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * json转换为list
	 */
	public static List<?> toList(String json) {
		try {
			return mapper.readValue(json, List.class);
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
