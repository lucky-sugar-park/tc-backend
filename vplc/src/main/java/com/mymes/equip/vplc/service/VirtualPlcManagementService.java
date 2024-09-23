package com.mymes.equip.vplc.service;

import java.util.List;

import com.mymes.equip.vplc.Condition;
import com.mymes.equip.vplc.VirtualPlcException;
import com.mymes.equip.vplc.Types.PlcStatus;

public interface VirtualPlcManagementService {
	
	public void registerVirtualPlc (VirtualPlcInfo vplcInfo) throws VirtualPlcException;
	
	public void updateVirtualPlc (VirtualPlcInfo vplcInfo) throws VirtualPlcException;
	
	public void updateVirtualPlcPort (String vplcId, int startPort, int portCount, int portConnLimit) throws VirtualPlcException;
	
	public void updateVirtualPlcPortByName (String vplcName, int startPort, int portCount, int portConnLimit) throws VirtualPlcException;
	
	public VirtualPlcInfo updateVirtualPlcStatus(String vplcId, String vplcName, PlcStatus plcStatus, boolean isPublished) throws VirtualPlcException;
	
	public void publishVirtualPlc(String vplcId) throws VirtualPlcException;
	
	public void publishVirtualPlcByName(String vplcName) throws VirtualPlcException;
	
	public void releaseVirtualPlc(String vplcId) throws VirtualPlcException;
	
	public void releaseVirtualPlcByName(String vplcName) throws VirtualPlcException;
	
	public void deleteVirtualPlc (String vplcId) throws VirtualPlcException;
	
	public void deleteVirtualPlcByName(String vplcName) throws VirtualPlcException;
	
	public VirtualPlcInfo findVirtualPlc(String vplcId) throws VirtualPlcException;
	
	public VirtualPlcInfo findVirtualPlcByName(String vplcName) throws VirtualPlcException;
	
	public List<VirtualPlcInfo> searchAllVirtualPlc() throws VirtualPlcException;
	
	public List<VirtualPlcInfo> searchVirtualPlc(Condition cond) throws VirtualPlcException;
	
	public List<VirtualPlcHistoryInfo> searchVirtualPlcHistory(Condition cond) throws VirtualPlcException;
	
	public List<ConnectionClientHistoryInfo> searchConnectionClientHistory(Condition cond) throws VirtualPlcException;
	
	public List<ConnectionInfo> searchConnections(String vplcId) throws VirtualPlcException;
	
	public List<ConnectionInfo> searchConnectionsByVplcName(String vplcName) throws VirtualPlcException;
	
	public void saveConnectionClient(ConnectionClientInfo ccInfo) throws VirtualPlcException;
		
}
