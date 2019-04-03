package com.pemila.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * 
 * @author 月在未央
 * @date 2018年10月28日下午5:35:02	
 */
public class JSON {
	/**
	 *
	 * @param:传入对象，json字符串
	 * @return:Object
	 */
	/**
	 * json转换成对象
	 * @param clazz class
	 * @param jsonStr str
	 * @param <T> t
	 * @return t
	 */
	public static <T> T  toObj(Class<T> clazz,String jsonStr) {
		ObjectMapper mapper = new ObjectMapper();
		T t = null;
	    try {
			t = mapper.readValue(jsonStr, clazz);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return t;
	}
	/**
	 * 对象转换成json
	 * @param obj 传入对象
	 * @return String json字符串
	 */
	public static String toJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}



