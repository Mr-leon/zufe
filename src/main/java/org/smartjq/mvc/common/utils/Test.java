package org.smartjq.mvc.common.utils;

import java.util.Arrays;

import com.google.gson.Gson;

public class Test {
	public static void main(String[] args) {
		Gson gson = new Gson();
		/* 把java中数组序列化为json形式 */
		String[] src = { "a\"", "b", "c" };
		String json = gson.toJson(src);
		System.out.println("json : " + json);

		/* 把json反序列化为java中的数组 */
		String[] fromJson = gson.fromJson(json, String[].class);
		System.out.println("java : " + Arrays.toString(fromJson));
	}
}
