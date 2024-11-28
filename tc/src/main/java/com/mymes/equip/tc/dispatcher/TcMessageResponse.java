package com.mymes.equip.tc.dispatcher;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mymes.equip.tc.msg.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@AllArgsConstructor
public class TcMessageResponse extends Message {
	
	public TcMessageResponse() {
		super();
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private boolean replyArrived;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private boolean success;

	private boolean changed;

	private int resultCode;

	private String resultDescription;

}
