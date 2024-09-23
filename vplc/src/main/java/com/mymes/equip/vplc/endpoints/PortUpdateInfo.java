package com.mymes.equip.vplc.endpoints;

import lombok.Data;

@Data
public class PortUpdateInfo {

	private String vplcId;
	
	private String vplcName;
	
	private int startPort;
	
	private int portCount;
	
	private int portConnLimit;
}
