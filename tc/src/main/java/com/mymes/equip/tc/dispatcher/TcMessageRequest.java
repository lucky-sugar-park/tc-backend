package com.mymes.equip.tc.dispatcher;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class TcMessageRequest extends Message {

	public TcMessageRequest () {
		super();
	}
	
	private boolean sync;

	/**
	 * URL 주소에 따라서 setting 됨
	 * "/api/v1/ifsvc/request"일 경우에는 false가 되고, "/api/v1/ifsvc/request-and-reply"일 경우에는 true가 됨
	 * URL이 "/api/v1/ifsvc/request-and-reply"일 경우에는 pushResult false여야 하고, pushUrl은 null 값 이어야 함
	 */
	@JsonIgnore
	private boolean shouldReply;

	/**
	 * shouldReply가 false일 경우에, push 여부는 옵션 사항임
	 */
	private boolean pushResult;

	/**
	 * shouldReply가 false일 경우에, push 여부는 옵션 사항임
	 */
	private String pushUrl;
}