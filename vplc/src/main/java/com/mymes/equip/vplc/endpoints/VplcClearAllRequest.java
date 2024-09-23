package com.mymes.equip.vplc.endpoints;

import lombok.Data;

@Data
public class VplcClearAllRequest {

private String vplcId;
	
	private String vplcName;
	
	private String memoryType;
	
	private String frameFormat;
	
	private int currentPosition;
}
