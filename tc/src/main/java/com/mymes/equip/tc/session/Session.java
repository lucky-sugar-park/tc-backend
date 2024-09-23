package com.mymes.equip.tc.session;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Session {

	private String id;
	
	private Date timestamp;
	
	private Map<String, Object> attributes;
	
	public Session() {
		this.attributes=new HashMap<>();
	}
	
	public void addAttribute(String key, Object value) {
		this.attributes.put(key, value); 
	}
	
	public void removeAttribute(String key) { 
		this.attributes.remove(key); 
	}
	
	public Object getAttribute(String key) { 
		return attributes.get(key); 
	}
}
