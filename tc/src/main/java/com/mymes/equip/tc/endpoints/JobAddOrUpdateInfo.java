package com.mymes.equip.tc.endpoints;

import com.mymes.equip.tc.Condition;
import com.mymes.equip.tc.schedule.ScheduleJobInfo;

import lombok.Data;

@Data
public class JobAddOrUpdateInfo {

	private ScheduleJobInfo scheduleJob;

	private Condition searchCondition;
}
