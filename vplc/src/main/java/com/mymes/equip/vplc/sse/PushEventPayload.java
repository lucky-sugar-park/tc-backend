package com.mymes.equip.vplc.sse;

import lombok.Data;

@Data
public abstract class PushEventPayload {

	public enum PushEventKind {

		VPLC_STATUS_CHANGED("vplc_status_changed"),
		VPLC_MEM_VALUE_UPDATED("vplc_mem_value_updated"),
		VPLC_STARTED("vplc_started"),
		VPLC_STOPPED("vplc_stopped"),
		VPLC_PAUSED("vplc_paused"),
		VPLC_RESUMED("vplc_resumed"),
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