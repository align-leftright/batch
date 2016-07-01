package org.weaver.alr.batch.common.util;

import com.google.gson.Gson;

public class JsonUtil {
	
	private static Gson gson = new Gson();
	
	public static <T> Object fromJson(String json, Class<T> classOfT){
		T result = gson.fromJson(json, classOfT);
		return result;
	}
	
	public static String toJson(Object object){
		return gson.toJson(object);
	}
	
}
