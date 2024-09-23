package com.mymes.equip.vplc.endpoints;

import lombok.Data;

@Data
public class VplcReadRequest {

	private String vplcId;
	
	private String vplcName;
	
	private String memoryType;
	
	private String frameFormat;
	
	private int startAddress;
	
	private int wordCount;
}
