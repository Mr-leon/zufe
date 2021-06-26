package org.smartjq.mvc.admin.sys.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class T1 {
	public static void main(String[] args) {
		String str = "[{\"mid\":\"5e72b3a4ba5d4d26bfe2cdd94c18d80a\",\"midName\":\"龙虎人丹\",\"stockId\":\"e1678a821ad7400fb8ace9a93e9d5cb9\",\"stockName\":\"金桥总工会\",\"unit\":\"盒\",\"price\":\"10\",\"count\":\"111\"},{\"mid\":\"4c3c8fb9b1234ec39d8f11548ea93b00\",\"midName\":\"清凉舒\",\"stockId\":\"e1678a821ad7400fb8ace9a93e9d5cb9\",\"stockName\":\"金桥总工会\",\"unit\":\"盒\",\"price\":\"20\",\"count\":\"100\"}]";
		JSONArray arr = JSONArray.parseArray(str);
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			System.out.println(obj.getString("mid"));
		}
	}
}