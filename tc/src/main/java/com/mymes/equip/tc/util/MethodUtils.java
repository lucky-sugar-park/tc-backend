package com.mymes.equip.tc.util;

import java.lang.reflect.Method;

public class MethodUtils {

	public static Method findMethod(Class<?> clazz, String prefix, String name) {
		String updatedName=name.substring(0, 1).toUpperCase()+name.substring(1);
		String nname=prefix+updatedName;
		
		Method methods[]=clazz.getMethods();
		for(int i=0; i<methods.length; i++) {
			if(methods[i].getName().equals(nname)) return methods[i];
		}
		return null;
	}
}
