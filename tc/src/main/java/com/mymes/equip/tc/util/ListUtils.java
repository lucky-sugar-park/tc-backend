package com.mymes.equip.tc.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ListUtils {
	
	public static String listToString(List<String> list) {
		
		String str = list.toString();
		str = StringUtils.strip(str, "[]");
		
		return str;
	}
	
	public static List<String> stringToList(String string) {
		
		List<String> list = new ArrayList<>();
		
		if(StringUtils.isEmpty(string)) {
			return list;
		}
		
		String[] nominator = StringUtils.splitByWholeSeparator(string, ", ");		
		for (int index = 0; index < nominator.length; index++) {
			list.add(nominator[index]);
		}
		
		return list;
	}
}
