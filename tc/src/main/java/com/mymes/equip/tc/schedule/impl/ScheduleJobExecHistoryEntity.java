package com.mymes.equip.tc.schedule.impl;

import java.util.Date;

import com.mymes.equip.tc.persist.PersistentEntity;
import com.mymes.equip.tc.schedule.ScheduleJobExecHistoryInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@Builder
@Entity
@Table(name="SCHEDULE_JOB_EXEC_HIST")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
public class ScheduleJobExecHistoryEntity extends PersistentEntity<ScheduleJobExecHistoryInfo> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SEQ_NO")
	private Long seqNo;
	
	@Column(name="JOB_NAME")
	private String jobName;
	
	@Column(name="EXEC_TIME")
	private Date executionTime;
	
	@Column(name="EXEC_RESULT")
	private String executionResult;
	
	@Column(name="NEXT_FIRE_TIME")
	private Date nextFireTime;
	
	@Column(name="REMARK")
	private String remark;
	
	public ScheduleJobExecHistoryEntity() {
		super();
	}
	
	@Override
	public ScheduleJobExecHistoryInfo info() {
		ScheduleJobExecHistoryInfo execHist=new ScheduleJobExecHistoryInfo();
		execHist.setSeqNo(this.getSeqNo());
		execHist.setJobName(this.getJobName());
		execHist.setExecutionTime(this.getExecutionTime());
		execHist.setExecutionResult(this.getExecutionResult());
		execHist.setNextFireTime(this.getNextFireTime());
		execHist.setRemark(this.getRemark());
		execHist.setCreatedBy(super.getCreatedBy());
		execHist.setCreatedDate(super.getCreatedDate());
		execHist.setUpdatedBy(super.getUpdatedBy());
		execHist.setUpdatedDate(super.getUpdatedDate());

		return execHist;
	}

	@Override
	public void from(ScheduleJobExecHistoryInfo info) {
		this.setSeqNo(info.getSeqNo());
		this.setJobName(info.getJobName());
		this.setExecutionTime(info.getExecutionTime());
		this.setExecutionResult(info.getExecutionResult());
		this.setNextFireTime(info.getNextFireTime());
		this.setRemark(info.getRemark());
		super.setCreatedBy(info.getCreatedBy());
		super.setCreatedDate(info.getCreatedDate());
		super.setUpdatedBy(info.getUpdatedBy());
		super.setUpdatedDate(info.getUpdatedDate());
	}

	@Override
	public String toPlainText() {
		return null;
	}
}
