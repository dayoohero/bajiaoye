package com.mobileAdHome.core;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/* 
* @author johncon 
* 创建日期 2008-9-10 
* json日期值处理器 
*/ 
public class JsonDateValueProcessor implements JsonValueProcessor { 

	private String format = "yyyy-MM-dd HH:mm:ss"; 
	
	public JsonDateValueProcessor() { 
	}

	public Object processArrayValue(Object arg0, JsonConfig arg1) {
		
		return null;
	}

	public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
		
		return null;
	}
}

