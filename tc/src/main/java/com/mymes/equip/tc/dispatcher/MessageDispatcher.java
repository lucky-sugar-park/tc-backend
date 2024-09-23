package com.mymes.equip.tc.dispatcher;

import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import com.mymes.equip.tc.ToolControlException;
import com.mymes.equip.tc.msg.ReplyCallback;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessageDispatcher {

	@PostConstruct
	public void onCreated() {
		log.info("MessageDispatcher.onCreated()...");
	}

	/**
	 * 결과를 return 하지 않음 (예, WRITE 타입일 경우 선택적으로 결과를 받지 않을 수 있음)
	 * 처리 결과를 WebHook으로 전달할 경우에는 이 함수를 호출할 수도 있다
	 * 
	 * @param req TcMessageRequest
	 * @throws ToolControlException
	 */
	public void dispatch(TcMessageRequest req) throws ToolControlException {
		log.debug("MessageDispatcher.dispatch(TcMessageRequest req)...");

		if(!check(req)) {
			throw new ToolControlException("Bad request message: \n" + req.forString());
		}

		String opcode=req.getOpcode();
		log.debug("opcode: {}, transactionId: {}, messageId: {}.", opcode, req.getTransactionId(), req.getMessageId());

		if(req.isPushResult() && req.getPushUrl()!=null) {
			req.setShouldReply(true);
		}

		MessageCommand command=createMessageCommand(opcode);
		if(req.isSync()) {
			command.execute(req);
			return;
		}
		Executors.newCachedThreadPool().execute(()->{
			try {
				command.execute(req);
			} catch (ToolControlException e) {
				log.warn("");
			}
		});
	}

	/**
	 * TcMessageResponse 객체에 결과를 담아줌
	 * 
	 * @param req TcMessageRequest
	 * @param res TcMessageResponse
	 * @throws ToolControlException
	 */
	public void dispatch(TcMessageRequest req, TcMessageResponse res) throws ToolControlException {
		log.debug("MessageDispatcher.dispatch(TcMessageRequest req, TcMessageResponse res)...");

		if(!check(req)) {
			throw new ToolControlException("Bad request message: \n" + req.forString());
		}

		String opcode=req.getOpcode();
		log.debug("opcode: {}, transactionId: {}, messageId: {}.", opcode, req.getTransactionId(), req.getMessageId());

		MessageCommand command=createMessageCommand(opcode);
		log.debug("Command fetched: {}", command.toString());

		command.execute(req, res);
	}
	
	public void dispatch(TcMessageRequest req, ReplyCallback<TcMessageResponse> replyCallback) throws ToolControlException {
		log.debug("MessageDispatcher.dispatch(TcMessageRequest req, ReplyCallback<TcMessageResponse> replyCallback, long timeout)...");
		
		Executors.newCachedThreadPool().execute(()->{
			TcMessageResponse res=new TcMessageResponse();
			try {
				dispatch(req, res);
				replyCallback.callbackReply(res);
			} catch (Exception e) {
				replyCallback.callbackFailure(e);
			}
		});
	}
	
	private boolean check(TcMessageRequest req) {
		return true;
	}
	
	private MessageCommand createMessageCommand(String opcode) throws ToolControlException {
		return MessageCommandFactory.getFactory().getMessageCommand(opcode);
	}
}
