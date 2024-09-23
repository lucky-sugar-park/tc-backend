package com.mymes.equip.tc.plc.connector;

import java.util.Properties;

import com.mymes.equip.tc.ToolControlException;
import com.mymes.equip.tc.plc.PlcReqAndResHolder;
import com.mymes.equip.tc.plc.PlcRequest;
import com.mymes.equip.tc.plc.PlcRequestCallback;
import com.mymes.equip.tc.plc.PlcResponse;

public interface ProgrammableControllerAdapter {
	
	public abstract long getId();

	public abstract String getName();
	
	public abstract void ready(long id, String name, Properties props) throws ToolControlException;
	
	public abstract void pause();
//	
	public abstract void resume();
//	
	public abstract void stop();
//	
	public abstract void start() throws ToolControlException;
	
	public abstract void pauseAsyncUse();
	
	public abstract void resumeAsyncUse();
	
	public abstract void destroy();
	
	/**
	 * PLC READ/WRITE 동기식으로 요청 
	 */
	public abstract void request(PlcRequest req, PlcResponse res) throws ToolControlException;
	
	/**
	 * PLC READ/WRITE 비동기식으로 요청
	 */
	public abstract void request(PlcReqAndResHolder reqAndResHolder, PlcRequestCallback reqCallback) throws ToolControlException;
	
	public abstract boolean testConnection() throws ToolControlException;
	
	public abstract PlcRequest newPlcRequestInstance();
	
	public abstract PlcResponse newPlcResponseInstance();	
}
