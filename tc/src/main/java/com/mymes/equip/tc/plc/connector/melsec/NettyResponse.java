package com.mymes.equip.tc.plc.connector.melsec;

import lombok.Data;

@Data
public class NettyResponse {

	private boolean arrived;
	
	private byte[] data;
}
