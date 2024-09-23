package com.mymes.equip.tc.endpoints;

import com.mymes.equip.tc.Condition;

import lombok.Data;

@Data
public class JobDeletionInfo {

	private String deleteJobName;
	
	private Condition searchCondition;
}
