package com.mymes.equip.tc.msg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Data;

@Data
public class Message {

	private String transactionId;

	private String messageId;

	private String opcode;

	private long timestamp;

	private Map<String, Object> data;

	private String sender;

	private String receiver;
	
	public Message() {
		data=new HashMap<>();
	}

	public void addData(String name, Object value) {
		data.put(name, value);
	}

	public static String generateTransactionId() {
		return generateId("TXR");
	}

	public static String generateMessageId() {
		return generateId("MSG");
	}

	public static String generateId(String prefix) {
		return prefix+"-"+UUID.randomUUID().toString();
	}

	public String forString() {
		StringBuffer sb = new StringBuffer();
		sb.append("============ Start of message ===========\n");
		sb.append("transactionId: " + transactionId + "\n");
		sb.append("messageId: " + messageId + "\n");
		sb.append("opcode: " + opcode + "\n");
		sb.append("timestamp: " + new SimpleDateFormat("yyyy-MM-dd hh:MM:ssS").format(new Date()) + "\n");
		sb.append("sender: " + sender + "\n");
		sb.append("receiver: " + receiver + "\n");
		sb.append("data: " + data + "\n");
		sb.append("============ Eeend of message ===========\n");
		return sb.toString();
	}
}
