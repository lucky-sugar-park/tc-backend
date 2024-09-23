package com.mymes.equip.tc.plc.service;

import java.util.Date;

import com.mymes.equip.tc.AbstractInfo;
import com.mymes.equip.tc.Types.PlcConnectorInfoStatus;
import com.mymes.equip.tc.Types.PlcConnectorOperStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@AllArgsConstructor
public class PlcConnectionEventHistInfo extends AbstractInfo {
	
	private long id;
	
	private long plcConnectorId;
	
	private String plcConnectorName;
	
	private String eventType;
	
	private PlcConnectorInfoStatus status1;
	
	private PlcConnectorOperStatus status2;
	
	private String cause;
	
	private Date timestamp;
	
	private String description;
	
	private String eventJson;
	
	public PlcConnectionEventHistInfo() {
		super();
	}
	
	public void setStatus1(PlcConnectorInfoStatus status1) {
		this.status1 = status1;
	}
	
	public void setStatus2(PlcConnectorOperStatus status2) {
		this.status2 = status2;
	}
	
	public void setStatus1(String status1) {
		this.status1 = status1 == null || "".equals(status1.trim()) ? PlcConnectorInfoStatus.UNKNOWN : PlcConnectorInfoStatus.valueOf(status1); 
	}
	
	public void setStatus2(String status2) {
		this.status2 = status2 == null || "".equals(status2.trim()) ? PlcConnectorOperStatus.UNKNOWN : PlcConnectorOperStatus.valueOf(status2);
	}
	
	@Override
	public String toPlainText() {
		return null;
	}
}
