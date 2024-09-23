package com.mymes.equip.tc.dispatcher;

import java.util.Date;
import java.util.Map;

import com.mymes.equip.tc.ToolControlException;
import com.mymes.equip.tc.Types.RequestType;
import com.mymes.equip.tc.interfs.InterfaceInfo;
import com.mymes.equip.tc.plc.PlcRequest;
import com.mymes.equip.tc.plc.PlcResponse;
import com.mymes.equip.tc.plc.connector.PlcAdapterManager;
import com.mymes.equip.tc.plc.connector.ProgrammableControllerAdapter;
import com.mymes.equip.tc.plc.service.PlcConnectorInfo;
import com.mymes.equip.tc.session.SessionContext;

import lombok.extern.slf4j.Slf4j;

//@Command(opcodes={"",""}, sync=true)
@Slf4j
public class GenericMessageCommand extends MessageCommand {
	
	private static PlcResponseService plcResponseService;

	static {
		plcResponseService=(PlcResponseService)SessionContext.getGlobalSessionContext().getBean(PlcResponseService.class);
	}

	public GenericMessageCommand() {
		super();
	}

	/**
	 * 1. opcode에 해당하는 인터페이스 정보를 가져온다
	 * 2. PLC connector를 통해서 READ 또는 WRITE 한다
	 * 3. READ/WRITE 결과를 가져와서 response에 넣는다
	 * 4. 로그로 남긴다
	 */
	@Override
	public void doExecution(TcMessageRequest req, TcMessageResponse resp) throws ToolControlException {
		log.debug("GenericMessageCommand.doExecution(TcMessageRequest req, TcMessageResponse resp)...");

		InterfaceInfo iInfo=super.findInterface(req.getOpcode());
		PlcConnectorInfo pInfo=super.findPlcConnector(iInfo.getPlcName());

		ProgrammableControllerAdapter plcAdapter=PlcAdapterManager.getPlcManager().get(pInfo.getName());
		if(plcAdapter==null) {
			throw new ToolControlException("No PLC for " + pInfo.getName() + " is not published.");
		}

		PlcRequest prequest=plcAdapter.newPlcRequestInstance();
		Map<String, Object> reqData=req.getData();
		prequest.setReqId(req.getTransactionId());
		prequest.setReqName(req.getOpcode());
		prequest.setReqType(RequestType.toRequestType(iInfo.getInterfaceType().name()));
		prequest.setFrameFormat(pInfo.getMessageFrameFormat());

		prequest.extractHeader(iInfo.getHeaderProps());
		prequest.extractData(iInfo.getDataProps(), iInfo.getDataLength(), reqData);

		PlcResponse presponse=plcAdapter.newPlcResponseInstance();
		InterfaceInfo riInfo=super.findInterface(iInfo.getReplyName());

		if(req.isShouldReply() && riInfo==null) {
			throw new ToolControlException();
		}

		presponse.setReplyInterfaceInfo(riInfo);
		plcAdapter.request(prequest, presponse);
		
		if(req.isShouldReply()) {
			resp.setResultCode(presponse.getResultCode());
			resp.setResultDescription(presponse.getResultDescription());
			resp.setData(presponse.getReplyData());

			resp.setChanged(checkIfChange(req.getOpcode(), presponse.getOrgRespAllData()));
		}

		log.debug("End of GenericMessageCommand's execution, transactionId: {}, messageId: {}", req.getTransactionId(), req.getMessageId());
	}
	
	private boolean checkIfChange(String opcode, byte[] originReply) {
		boolean changed=false;
		PlcResponseEntity response=plcResponseService.get(opcode);
		if(response==null) changed=true;
		else {
			byte[] before=null;//response.getReplyData();
			for(int i=0;i<originReply.length;i++) {
				if(before[i]!=originReply[i]) {
					changed=true;
					break;
				}
			}	
		}
		
		if(changed) {
			PlcResponseEntity changedPlcReply=new PlcResponseEntity();
			changedPlcReply.setOpcode(opcode);
			changedPlcReply.setReplyData(originReply);
			changedPlcReply.setTimestamp(new Date());
			changedPlcReply.setTimeToLive(-1L);

			plcResponseService.put(opcode, changedPlcReply);
		}
		
		return changed;
	}
}
