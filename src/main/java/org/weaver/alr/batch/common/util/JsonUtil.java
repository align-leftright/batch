package org.weaver.alr.batch.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class JsonUtil {
	
	private static Gson gson = new GsonBuilder().create();
	
	public static <T> Object fromJson(String json, Class<T> classOfT){
		T result = gson.fromJson(json, classOfT);
		return result;
	}
	
	public static String toJson(Object object){
		return gson.toJson(object);
	}
	
	public static JsonElement toJsonElement(Object object){
		return gson.toJsonTree(object);
	}
	
}
