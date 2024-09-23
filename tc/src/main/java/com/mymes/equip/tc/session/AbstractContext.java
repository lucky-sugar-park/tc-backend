package com.mymes.equip.tc.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class AbstractContext {

	/**
	 * 어플리케이션의 글로벌 속성을 저장하기 위한 변수
	 */
	private static Map<String, Object> globalAttributes = new HashMap<String, Object>();
	
	/**
	 * 어플리케이션의 쓰레드 특화된 속성을 저장하기 위한 변수
	 */
	private Map<String, Object> localAttributes;
	
	/**
	 * 생성자. localAttributes를 초기화 한다.
	 */
	protected AbstractContext() { 
		localAttributes = new HashMap<String, Object>(); 
	}

	public static void setGlobalAttribute(String key, Object value){ 
		globalAttributes.put(key, value); 
	}
	
	public static Object getGlobalAttribute(String key){ 
		return globalAttributes.get(key); 
	}
	
	public static Object removeGlobalAttribute(String key){ 
		return globalAttributes.remove(key); 
	}
	
	public static void clearGlobalAttributes(){ 
		globalAttributes.clear(); 
	}
	
	public void setLocalAttribute(String key, Object value) { 
		localAttributes.put(key, value); 
	}
	
	public Object getLocalAttribute(String key) { 
		return localAttributes.get(key); 
	}
	
	public void clearLocalAttributes() { 
		localAttributes.clear();
	}
	
	public boolean containsLocalAttributeKey(Object key) { 
		return localAttributes.containsKey(key); 
	}
	
	public boolean containsLocalAttributeValue(Object value) { 
		return localAttributes.containsValue(value); 
	}
	
	public boolean isEmptyLocalAttributes() { 
		return localAttributes.isEmpty(); 
	}
	
	public Set<String> keyLocalAttributeSet() { 
		return localAttributes.keySet(); 
	}
	
	public Object removeLocalAttribute(Object key) { 
		return localAttributes.remove(key); 
	}
	
	public int sizeOfLocalAttributes() { 
		return localAttributes.size(); 
	}
	
	public Collection<Object> valuesOfLocalAttribute() { 
		return localAttributes.values(); 
	}
}
