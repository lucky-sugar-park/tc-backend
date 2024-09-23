package com.mymes.equip.vplc.machine;

import com.mymes.equip.vplc.Types.FrameFormat;
import com.mymes.equip.vplc.postbox.PostboxMessage;

import lombok.Data;

@Data
public abstract class Message implements PostboxMessage {
	
	private String transactionId;
	
	private String messageId;
	
	private String clientChannelId;
	
	private FrameFormat format;
	
	private byte[] originalData;
	
	// subHeader1 + subHeader2 = binary 타입일 경우 2 바이트, ascii 타입일 경우 4 바이트
	// 요청: 5000/0x50,0x00, 응답: D000/0xD0,0x00
	private byte subHeader1;
	
	private byte subHeader2;
	
	private byte subHeader3;
	
	private byte subHeader4;
	
	private byte netNumber1;
	
	private byte netNumber2;
	
	private byte plcNumber1;
	
	private byte plcNumber2;
	
	private byte opponentIONumber1;
	
	private byte opponentIONumber2;
	
	private byte opponentIONumber3;
	
	private byte opponentIONumber4;
	
	private byte opponentNumber1;
	
	private byte opponentNumber2;
	
	private byte dataLen1;
	
	private byte dataLen2;
	
	private byte dataLen3;
	
	private byte dataLen4;
	
	private ResponseMessageCallback responseMessageCallback;

	public abstract String forString();
	
	public abstract void extract(byte[] data);
	
	public abstract byte[] toByteArray();
}
