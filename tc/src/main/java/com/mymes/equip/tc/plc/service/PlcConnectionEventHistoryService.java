package com.mymes.equip.tc.plc.service;

import java.util.Date;

import com.mymes.equip.tc.Condition;
import com.mymes.equip.tc.persist.PageResponse;

public interface PlcConnectionEventHistoryService {
	
	public void savePlcConnectionEventHistory(PlcConnectionEventHistInfo pcehInfo);
	
	public PageResponse<PlcConnectionEventHistInfo> searchPlcConnectionEventHist(Condition cond);

	public long deleteByTimestamp(Date timestamp);
}
