package com.example.demo.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ResponseUtils {
	
	public static Map<String,Object> putSuccess (Object data, String msg){
		return resultMap(true, data, msg);
	}
	
	public static Map<String,Object> putFail (Object data, String msg){
		return resultMap(false, data, msg);
	}
	
	private static Map<String,Object> resultMap (boolean isSuccess, Object data, String msg){
		Map<String, Object> result = new HashMap<>();
		if(!isSuccess) {
			result.put("result", false);
			result.put("msg", msg);
			if(data != null) result.put("data", data);
		}else {
			result.put("result", true);
			if(msg != null)result.put("access_token", msg);
			result.put("data", data);
		}
		return result;
	}

}
