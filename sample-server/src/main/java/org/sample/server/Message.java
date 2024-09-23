package org.sample.server;

import java.util.Map;

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
}
