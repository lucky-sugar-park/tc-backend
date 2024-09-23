package com.mymes.equip.tc.schedule;

import java.util.Date;

import com.mymes.equip.tc.AbstractInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TriggerStatusInfo extends AbstractInfo {

	private String name;
	
	private String groupName;
	
	private String status;
	
	private String type;
	
	private String hardwareType;
	
	private Date executionTime;
	
	private Date nextFireTime;

	@Override
	public String toPlainText() {
		return new StringBuilder().toString();
	}
}
