package com.mymes.equip.tc.schedule;

import com.mymes.equip.tc.AbstractInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ScheduleJobDetailInfo extends AbstractInfo {

	private String oper;
	
	private String scheduleJobId;
	
	private String entryKey;
	
	private String entryValue;
	
	private String entryValueType;
	
	private String description;
	
	@Override
	public String toPlainText() {
		return null;
	}	
}