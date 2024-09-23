package com.mymes.equip.vplc.transport;

import com.mymes.equip.vplc.machine.Message;
import com.mymes.equip.vplc.machine.ResponseMessageCallback;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyResponseMessageCallbackListener implements ResponseMessageCallback {
	
	private MessageFrameHandler handler;
	
	public NettyResponseMessageCallbackListener(MessageFrameHandler handler) {
		this.handler=handler;
	}

	@Override
	public void onResponse(Message message) {
		log.debug("NettyCallbackListener.doCallback(Message message)...");

		Payload payload=new Payload();
		payload.setChannelId(message.getClientChannelId());
		payload.setRawMessage(message.toByteArray());
		payload.setMessage(message);
		
		handler.send(payload);
	}
}
