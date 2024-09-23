package com.mymes.equip.tc.dispatcher;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.mymes.equip.tc.ToolControlException;
import com.mymes.equip.tc.interfs.InterfaceInfo;
import com.mymes.equip.tc.interfs.InterfaceService;
import com.mymes.equip.tc.msg.Message;
import com.mymes.equip.tc.msg.audit.ExchangedMessageAuditInfo;
import com.mymes.equip.tc.msg.audit.ExchangedMessageAuditService;
import com.mymes.equip.tc.plc.service.PlcConnectorInfo;
import com.mymes.equip.tc.plc.service.PlcConnectorManagementService;
import com.mymes.equip.tc.session.SessionContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class MessageCommand {
	
	private static RestTemplate restTemplate=new RestTemplate();
	
	protected ApplicationContext applicationContext;
	
	protected InterfaceService interfaceService;
	
	private ExchangedMessageAuditService exchangedMessageAuditService;
	
	private PlcConnectorManagementService plcService;
	
	public MessageCommand() {
		applicationContext=SessionContext.getGlobalSessionContext().getApplicationContext();
		interfaceService=(InterfaceService)applicationContext.getBean(InterfaceService.class);
		exchangedMessageAuditService=(ExchangedMessageAuditService)applicationContext.getBean(ExchangedMessageAuditService.class);
		plcService=(PlcConnectorManagementService)applicationContext.getBean(PlcConnectorManagementService.class);
	}
	
	/**
	 * 동기식으로 처리햐며, 처리 결과를 넘겨주지 않음 
	 * - 다만, 결과를 따로 push 받기를 원하는 경우에는 지정한 URL로 push 해 줌
	 * 
	 * @param req
	 * @throws ToolControlException
	 */
	public void execute(TcMessageRequest req) throws ToolControlException {
		log.debug("MessageCommand.execute(TcMessageRequest req) is called.");
		this.saveMessageAudit(req);

		TcMessageResponse response=new TcMessageResponse();
		doExecution(req, response);

		if(req.isPushResult() && req.getPushUrl()!=null) {
			req.setShouldReply(true);
			
			response.setTransactionId(req.getTransactionId());
			response.setMessageId(Message.generateMessageId());
			response.setTimestamp(new Date().getTime());
			response.setSender(req.getReceiver());
			response.setReceiver(req.getSender());
			response.setOpcode(req.getOpcode());
			
			HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    HttpEntity<TcMessageResponse> sendResultEntity=new HttpEntity<>(response, headers);
		    
		    // send result to given URL
			restTemplate.put(req.getPushUrl(), sendResultEntity);
			// save exchanged message audit
			this.saveMessageAudit(response);
		}
	}
	
	/**
	 * 동기식으로 처리하며, 처리 결과를 response에 담아줌
	 * 
	 * @param request
	 * @param response
	 * @throws ToolControlException
	 */
	public void execute(TcMessageRequest request, TcMessageResponse response) throws ToolControlException {
		log.debug("MessageCommand.execute(TcMessageRequest request, TcMessageResponse response) is called.");
		
		this.saveMessageAudit(request);
		
		doExecution(request, response);
		
		response.setTransactionId(request.getTransactionId());
		response.setMessageId(Message.generateMessageId());
		response.setTimestamp(new Date().getTime());
		response.setSender(request.getReceiver());
		response.setReceiver(request.getSender());
		response.setOpcode(request.getOpcode());

		this.saveMessageAudit(response);
	}
	
//	/**
//	 * 비동기 식으로 처리하며, 처리 결과를 담아서 callback 함수를 호출해 줌.
//	 * RestController에서는 사용하는 것이 의미 없음 (다른 방식으로 command를 실행할 때에 사용하는 것이 의미 있음)
//	 * 
//	 * @param request
//	 * @param replyCallback
//	 * @throws ToolControlException
//	 */
//	public void execute(TcMessageRequest request, ReplyCallback<TcMessageResponse> replyCallback) throws ToolControlException {
//		log.debug("MessageCommand.execute(TcMessageRequest request, SyncReplyCallback<TcMessageResponse> replyCallback) is called.");
//		this.saveMessageAudit(request);
//
//		// 비동기로 실행함
//		Executors.newCachedThreadPool().execute(()->{
//			log.debug("");
//			try {
//				TcMessageResponse response=new TcMessageResponse();
//				
//				doExecution(request, response);
//
//				response.setTransactionId(request.getTransactionId());
//				response.setMessageId(Message.generateMessageId());
//				response.setTimestamp(new Date().getTime());
//				response.setSender(request.getReceiver());
//				response.setReceiver(request.getSender());
//
//				replyCallback.callbackReply(response);
//				
//				this.saveMessageAudit(response);
//			} catch (ToolControlException e) {
//				log.warn("", e);
//			}
//		});
//	}
	
	protected InterfaceInfo findInterface(String opcode) {
		return this.interfaceService.findInterfaceByName(opcode);
	}
	
	protected PlcConnectorInfo findPlcConnector(String plcName) {
		return this.plcService.findPlcConnectorByName(plcName);
	}
	
	private ExchangedMessageAuditInfo toMessageAudit(Message message) {
		ExchangedMessageAuditInfo auditMsg=new ExchangedMessageAuditInfo();
		auditMsg.setTransactionId(message.getTransactionId());
		auditMsg.setMessageId(message.getMessageId());
		auditMsg.setName(message.getOpcode());
		if(message instanceof TcMessageRequest) {
			auditMsg.setMessageType("REQUEST");
		} else {
			auditMsg.setMessageType("RESPONSE");	
		}
		auditMsg.setTimestamp(new Date(message.getTimestamp()));
		auditMsg.setSender(message.getSender());
		auditMsg.setReceiver(message.getReceiver());
		auditMsg.setDescription("");
		auditMsg.setDataMap(message.getData());

		return auditMsg;
	}
	
	private void saveMessageAudit(Message exchangedMessage) {
		log.debug("");
		exchangedMessageAuditService.writeAudit(toMessageAudit(exchangedMessage));
	}
	
	public abstract void doExecution(TcMessageRequest req, TcMessageResponse resp) throws ToolControlException;
}
