package com.mymes.equip.vplc.machine;

import com.mymes.equip.vplc.Types.FrameFormat;
import com.mymes.equip.vplc.Types.MemoryType;
import com.mymes.equip.vplc.Types.PlcStatus;

import java.util.UUID;

import com.mymes.equip.vplc.VirtualPlcException;
import com.mymes.equip.vplc.postbox.Postbox;
import com.mymes.equip.vplc.postbox.PostboxListener;
import com.mymes.equip.vplc.postbox.PostboxManager;
import com.mymes.equip.vplc.postbox.PostboxMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VirtualPlcMessageDelegate implements PostboxListener<PostboxMessage>{

	private VirtualProgrammableController vplc;

	private Postbox<PostboxMessage> pbox;

	public VirtualPlcMessageDelegate (VirtualProgrammableController vplc) {
		this.vplc=vplc;
		this.vplc.setMessageDelegate(this);
		this.pbox=PostboxManager.getInstance().getPostbox("PBOX-"+vplc.getId(), this, true, 50);
	}

	/**
	 * vplc로 ClientChannelStatusChangeEventHandler를 전파한다. 
	 * vplc에서 이 핸드러를 통해서 포트당 연결가능한 커넥션 수를 전달해 주기 위함임
	 * 
	 * @param channelEventHandler
	 */
	public void dispatchChannelEventHandler(ClientChannelStatusChangeEventHandler channelEventHandler) {
		log.debug("VirtualPlcMessageDelegate.dispatchChannelEventHandler(ClientChannelStatusChangeEventHandler channelEventHandler)...");

		if(vplc!=null) {
			vplc.setChannelEventHandler(channelEventHandler);
		}
	}

	/**
	 * TC와 같은 다른 시스템으로부터의 읽기 쓰기 요청 메시지를 처리한다
	 * 비동기식으로 처리하기 위해서 Postbox에 넣는다. Postbox에서 onMessage를 호출해 준다
	 * 
	 * @param message
	 * @param callback
	 * @throws VirtualPlcException
	 */
	public void processMessage(Message message, ResponseMessageCallback responseMessageCallback) throws VirtualPlcException {
		log.debug("VirtualPlcMessageDelegate.processMessage(Message message, MessageCallback callback)...");

		if(vplc.getStatus()!=PlcStatus.RUNNING) {
			// 해당 클라이언트에게 warn 메시지를 전송한다
			MessageResponse msg=new MessageResponse();
			msg.setErrCode1((byte)0x00);
			msg.setErrCode2((byte)0x00);
			msg.setData(null);
			
			// VPLC가 RUNNING 상태가 아니라면, Client에게 사전에 약속된 ERROR 코드와 메시지를 응답한다
			// 에러코드와 이에 대한 세부 메시지 내용은 아직 미 구현 상태임
			responseMessageCallback.onResponse(msg);
			return;
		}

		// Postbox 메시지에 callback 함수를 넣어준다 (비동기 처리를 위함)
		message.setResponseMessageCallback(responseMessageCallback);
		this.pbox.postMessage(message);
	}

	@Override
	public boolean onMessage(PostboxMessage pboxMessage) {
		log.debug("VirtualPlcMessageDelegate.onMessage(PostboxMessage pboxMessage)..., MSG:\n{}", pboxMessage.toJson());

		MessageRequest request=(MessageRequest)pboxMessage;
		boolean ret=true;
		try {
			MessageResponse response=new MessageResponse();
			MemoryType memType=null;
			if(vplc.getMessageFrameFormat()==FrameFormat.BINARY || vplc.getMessageFrameFormat()==FrameFormat.UNKNOWN) {
				memType=MemoryType.toMemoryType(request.getDeviceCode1());
			} else if(vplc.getMessageFrameFormat()==FrameFormat.ASCII){
				memType=MemoryType.toMemoryType(new String(new byte[] { request.getDeviceCode1(), request.getDeviceCode2() }));
			}
			
			switch(request.getReqType()) {
			case READ:
				byte reads[]=vplc.read(request.getStartAddress(), request.getWordCount(), memType);
				response.setData(reads);
				break;
			case WRITE:
				vplc.write(request.getStartAddress(), request.getWordCount(), request.getData(), memType);
				response.setData(new byte[] {});
				break;
			default:
				break;
			}
			if(FrameFormat.BINARY==vplc.getMessageFrameFormat()) {
				response.setErrCode1((byte)0x00); response.setErrCode2((byte)0x00);				
			} else {
				String errCode="0000";
				byte[] bytes=errCode.getBytes();
				response.setErrCode1(bytes[0]); response.setErrCode1(bytes[1]);
				response.setErrCode1(bytes[2]); response.setErrCode1(bytes[3]);
			}
			response.setTransactionId(request.getTransactionId());
			response.setMessageId(UUID.randomUUID().toString());
			response.setClientChannelId(request.getClientChannelId());
			response.setFormat(vplc.getMessageFrameFormat());
			response.apply(request);

			// 메시지로부터 Callback 함수를 꺼낸 후에 - 요청에 대한 처리결과를 Client로 전송해 준다
			request.getResponseMessageCallback().onResponse(response);
		} catch (VirtualPlcException ve) {
			log.warn("Fail to handle pbox message: \n{}", pboxMessage.toJson());
			MessageError error=new MessageError();
			if(FrameFormat.BINARY==vplc.getMessageFrameFormat()) {
				error.setErrCode1((byte)0x00); error.setErrCode2((byte)0x01);				
			} else {
				String errCode="0001";
				byte[] bytes=errCode.getBytes();
				error.setErrCode1(bytes[0]); error.setErrCode1(bytes[1]);
				error.setErrCode1(bytes[2]); error.setErrCode1(bytes[3]);
			}
			ret=false;
			request.getResponseMessageCallback().onResponse(error);
		}
		return ret;
	}
	
	public VirtualProgrammableController getPlc() {
		return this.vplc;
	}
	
	public void startPbox() {
		this.pbox.doStart();
	}

	public void pausePbox() {
		this.pbox.doPause();
	}

	public void resumePbox() {
		this.pbox.doResume();
	}

	public void standbyPbox() {
		this.pbox.doStandby();
	}
	
	public void release() {
		this.standbyPbox();
		PostboxManager.getInstance().removePostbox(this.pbox.getPostboxName());
	}
}
