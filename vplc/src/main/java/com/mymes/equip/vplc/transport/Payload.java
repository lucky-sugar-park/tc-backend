package com.mymes.equip.vplc.transport;

import com.mymes.equip.vplc.machine.Message;

import lombok.Data;

@Data
public class Payload {
	
	private String transactionId;
	
	private String messageId;
	
	private long timestamp;
	
	private String channelId;

	private byte[] rawMessage;

	private Message message;
	
	public String forString() {
		return message.forString();
	}
}
