package org.sample.server;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	@JsonIgnore
	private boolean replyArrived;
	
	@JsonIgnore
	private boolean success;
	
	private int resultCode;
	
	private String resultDescription;
}