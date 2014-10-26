package com.mobileAdHome.core;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

/**
 * JSON操作类
 * 暂时仅仅支持将java对象转储成JSON格式的字符串
 * 
 * 原来用的 Simple Json 中的JSONValue.toJSONString 不能渲染POJO对象, 无论是使用 元注释还是实现接口 * 或多或少的都存在移植依赖性的问题.
 * 而POLO对象会用在多个地方,诸如: 视图/数据库表映射[DAO]/服务层 等等
 * 
 * 如果对象属于复合对象的话(非基本数据类型)可能存在效率问题,建议不使用多层次的POLO类
 * 
 * @author KenXu
 * 
 */
public final class JsonSpread {
		
	@SuppressWarnings("unchecked")
	public static String toJSONString(Object value) {
		if (value == null) return "null";

		if (value instanceof Number) return value.toString();
		if (value instanceof Boolean) return value.toString();
		
		if (value instanceof String)
			return "\"" + escape((String) value) + "\"";
		
		if (value instanceof Double) {
			if (((Double) value).isInfinite() || ((Double) value).isNaN())
				return "null";
			else
				return value.toString();
		}

		if (value instanceof Float) {
			if (((Float) value).isInfinite() || ((Float) value).isNaN())
				return "null";
			else
				return value.toString();
		}
						
		if (value instanceof Map) return map2Json((Map) value);
		
		if (value instanceof Collection) return coll2Json((Collection) value);
		
		if (value.getClass().isArray()) return array2Json(value);
		
		return pojo2Json(value,true);
	}
	
	static String array2Json(Object array) {
		if (null == array) return "null" ;
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		
		// 此处减1是为了下面的 逗号 追加
		int len = Array.getLength(array) - 1;
		if (len > -1){
			int i ;
			for (i = 0; i < len; i++) {
				sb.append(toJSONString(Array.get(array, i))).append(", ");
			}
			sb.append(toJSONString(Array.get(array, i)));
		}
		
		sb.append(']');
		return sb.toString();
	}
	
	static String coll2Json(Collection<?> coll) {
		if (null == coll) return "null" ;
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		for (Iterator<?> it = coll.iterator(); it.hasNext();) {
			sb.append(toJSONString(it.next()));
			if (it.hasNext())
				sb.append(", ");
		}
		sb.append(']');
		return sb.toString();
	};

	static String pojo2Json(Object obj,boolean includeParentClass) {
		Class<?> type = obj.getClass();
		Field[] fields = type.getDeclaredFields();
		Class<?> parentType = type.getSuperclass();
		if(includeParentClass){
			Field[] parentFields = parentType.getDeclaredFields();
			Field[] totalFields = (Field[])ArrayUtils.addAll(fields,parentFields);
			fields = totalFields;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers()))
				continue;
			String name = f.getName();
			f.setAccessible(true);
			Object value = null;
			try {
				value = f.get(obj);
			} catch (Exception e) {
				value = null;
			}
			map.put(name, value);
		}
		type = null;fields = null;
		return map2Json(map);
	}
	
	@SuppressWarnings("unchecked")
	static String map2Json(Map<String, Object> map) {
		if (null == map) return "null" ;
		
		StringBuffer sb = new StringBuffer();
		sb.append('{');
		for (Iterator<?> it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry=(Map.Entry)it.next();
			
			String key = (String)entry.getKey();
			if (null == key) continue ;
			
			sb.append('\"');
			escape(key,sb);
			sb.append('\"').append(':').append(toJSONString(entry.getValue()));
			
			if (it.hasNext())
				sb.append(", ");
		}
		sb.append('}');
		return sb.toString();
	};

	/**
	 * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters (U+0000 through U+001F).
	 * @param s
	 * @return
	 */
	public static String escape(String s){
		if(s==null)
			return null;
        StringBuffer sb = new StringBuffer();
        escape(s, sb);
        return sb.toString();
    }	
	
	/**
	 * @param s - Must not be null.
	 * @param sb
	 */
	static void escape(String s, StringBuffer sb) {
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				// Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if ((ch >= '\u0000' && ch <= '\u001F')
						|| (ch >= '\u007F' && ch <= '\u009F')
						|| (ch >= '\u2000' && ch <= '\u20FF')) {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
	}

}