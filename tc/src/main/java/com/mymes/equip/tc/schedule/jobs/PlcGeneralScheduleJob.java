package com.mymes.equip.tc.schedule.jobs;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import org.quartz.JobExecutionContext;

import com.mymes.equip.tc.ToolControlException;
import com.mymes.equip.tc.dispatcher.MessageDispatcher;
import com.mymes.equip.tc.dispatcher.TcMessageRequest;
import com.mymes.equip.tc.dispatcher.TcMessageResponse;
import com.mymes.equip.tc.interfs.InterfaceInfo;
import com.mymes.equip.tc.interfs.InterfaceService;
import com.mymes.equip.tc.msg.Message;
import com.mymes.equip.tc.schedule.ScheduleJob;
import com.mymes.equip.tc.webhook.WebHook;
import com.mymes.equip.tc.webhook.WebHookManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlcGeneralScheduleJob extends ScheduleJob {

	private MessageDispatcher messageDispatcher;

	public PlcGeneralScheduleJob() {
		super();
		messageDispatcher=(MessageDispatcher)applicationContext.getBean(MessageDispatcher.class);
	}

	/**
	 * context내의 정보에 대한 job을 실행하고 job 실행결과를 context에 담는다
	 * PLC 관련 대부분의 Job은 요구되는 파라미터만 달리하면 이 클래스 하나로 모두 만족할 수 있다
	 * 
	 * 요청정보: opcode, READ | WRITE, header 정보, body 정보, binary | ascii, opcode
	 * 응답정보: header 정보, body 정보, opcode, 응답을 전달해 줄 수 있는 webhook 정보 
	 * 
	 * @param context JobExecutionContext job 구동에 필요한 정보를 담고 있고, job 실행결과를 담을 객체
	 */
	@Override
	public void executeJob(JobExecutionContext context) throws ToolControlException {
		log.debug("PlcGeneralScheduleJob.executeJob(JobExecutionContext context)...");

		String interfaceName=(String)context.getMergedJobDataMap().get("interfaceName");
		InterfaceService interfaceService=(InterfaceService)applicationContext.getBean(InterfaceService.class);
		InterfaceInfo iInfo=interfaceService.findInterfaceByName(interfaceName);

		TcMessageRequest tcMessageReq=new TcMessageRequest();
		tcMessageReq.setTransactionId(Message.generateTransactionId());
		tcMessageReq.setMessageId(Message.generateMessageId());
		tcMessageReq.setOpcode(iInfo.getName());
		tcMessageReq.setSender("Internal-Schedule-Job");
		tcMessageReq.setReceiver("ToolControl-System");
		tcMessageReq.setTimestamp(new Date().getTime());
		tcMessageReq.setShouldReply(true);
		tcMessageReq.setPushResult(true);

		context.getJobDetail().getJobDataMap().forEach((key, value)->{
			tcMessageReq.addData(key, value);
		});

		TcMessageResponse tcMessageRes=new TcMessageResponse();
		messageDispatcher.dispatch(tcMessageReq, tcMessageRes);
		context.setResult(tcMessageRes);
		
		if(iInfo.isReply()) {
			InterfaceInfo rInfo=interfaceService.findInterfaceByName(iInfo.getReplyName());
			context.put("replyTemplate", rInfo);
			List<String> webHookNameList=rInfo.getWebHookNameList();
			if(webHookNameList!=null) {
				webHookNameList.forEach(webHookName->{
					// 각 Hook에 대해서 비동기식으로 전송해 준다
					Executors.newCachedThreadPool().execute(()->{
						WebHook<Message> webhook=WebHookManager.getManager().getWebHook(webHookName);
						if(webhook!=null) {
							try {
								tcMessageRes.setReceiver(webhook.getName()+":"+webhook.getUrl());
								webhook.hook(tcMessageRes);
							} catch (Throwable t) {
								log.warn("");
							}
						}
					});
				});
			}
		}		
	}
}
