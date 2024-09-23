package com.mymes.equip.tc.plc.connector.melsec;
//package equip.tc.plc.connector.melsec;
//
//import java.util.Properties;
//
//import equip.tc.plc.PlcProperties;
//
//public class MelsecPlcProperties implements PlcProperties {
//
//	private Properties props;
//	
//	public MelsecPlcProperties() {
//		props=new Properties();
//	}
//	
//	@Override
//	public void addProp(String propName, String value) {
//		props.put(propName, value);
//	}
//
//	@Override
//	public void addProp(String propName, int value) {
//		props.put(propName, value);
//	}
//
//	@Override
//	public void addProp(String propName, long value) {
//		props.put(propName, value);
//	}
//
//	@Override
//	public void addProp(String propName, boolean value) {
//		props.put(propName, value);
//	}
//
//	@Override
//	public void addProp(String propName, Object value) {
//		props.put(propName, value);
//	}
//
//	@Override
//	public String getProp(String propName) {
//		return this.getProp(propName, null);
//	}
//
//	@Override
//	public String getProp(String propName, String defaultValue) {
//		String ret=props.getProperty(propName);
//		return ret==null?defaultValue:ret;
//	}
//
//	@Override
//	public int getPropAsInt(String propName) {
//		return this.getPropAsInt(propName, -1);
//	}
//
//	@Override
//	public int getPropAsInt(String propName, int defaultValue) {
//		Integer ret=(Integer)props.get(propName);
//		return ret==null?defaultValue:ret;
//	}
//
//	@Override
//	public long getPropAsLong(String propName) {
//		return this.getPropAsLong(propName, -1L);
//	}
//
//	@Override
//	public long getPropAsLong(String propName, long defaultValue) {
//		Long ret=(Long)props.get(propName);
//		return ret==null?defaultValue:ret;
//	}
//
//	@Override
//	public boolean getPropAsBoolean(String propName) {
//		return this.getPropAsBoolean(propName, false);
//	}
//
//	@Override
//	public boolean getPropAsBoolean(String propName, boolean defaultValue) {
//		Boolean ret=(Boolean)props.get(propName);
//		return ret==null?defaultValue:ret;
//	}
//
//	@Override
//	public Object getPropAsObj(String propName) {
//		return props.get(propName);
//	}
//}
