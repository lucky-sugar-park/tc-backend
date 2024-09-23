package com.mymes.equip.tc.sse;

import lombok.Data;

@Data
public abstract class PushEventPayload {

	public enum PushEventKind {

		AUDIT_MESSAGE("audit_message"),
		CONNECTED("connected"),
		COUNT("count");
		
		String name;
		
		private PushEventKind(String name) {
			this.name=name;
		}
		
		public String getName() {
			return this.name;
		}
	}
	
	private String eventId;

	private PushEventKind eventKind;
	
	private Object data;
	
	private String comment;
	
	private long reconnectTime;
	
	public abstract String toJson();
}