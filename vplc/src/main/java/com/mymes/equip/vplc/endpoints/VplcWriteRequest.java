package com.mymes.equip.vplc.endpoints;

import lombok.Data;

@Data
public class VplcWriteRequest {

	private String vplcId;
	
	private String vplcName;
	
	private String memoryType;
	
	private String frameFormat;
	
	private int startAddress;
	
	private int wordCount;
	
	/**
	 * data.length()==wordCount<<1
	 */
	private String data;
}
