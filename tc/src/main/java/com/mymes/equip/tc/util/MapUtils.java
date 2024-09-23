package com.mymes.equip.tc.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class MapUtils {

	public static String mapToString(Map<?, ?> map) {
		String str = map.toString();
		str = StringUtils.strip(str, "{}");

		return str;
	}
	
	public static Map<String, String> stringToMap(String string) {	
		Map<String, String> map=new HashMap<>();
		
		if(StringUtils.isEmpty(string)) {
			return map;
		}

		String[] nominator=StringUtils.splitByWholeSeparator(string, ", ");		

		for (int index=0; index<nominator.length; index++) {
			String[] values=StringUtils.splitPreserveAllTokens(nominator[index], "=");
			if(values.length==2) {
				// in case that [=AUTOMATIC], values[0] = "", values[1]=AUTOMATIC. so, values[0].length is zero, skip it. changed by vince. 03.20.2014
				if(StringUtils.isNotEmpty(values[0])) {
					map.put(values[0], values[1]);
				}
			}
		}
		return map;
	}
	
	private static String camelHump(String str) {
		if(str!=null && (str=str.trim()).length()>0) {
			str=str.substring(0, 1).toUpperCase()+str.substring(1);
        }
		return str;	    
	}

	private static String convertFieldToAccessor(String filed) {
		return "get" + camelHump(filed);
    }

	public static Map<String, Object> toMap(List<Object> list, String keyField) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String accessor=convertFieldToAccessor(keyField);
        Map<String, Object> map=new HashMap<>();
        for (Iterator<?> iterator=list.iterator(); iterator.hasNext();) {
			Object object=(Object) iterator.next();
			Method method=object.getClass().getDeclaredMethod(accessor);
			String key=(String)method.invoke(object);
			map.put(key, object);
		}
        return map;
	}
}
