package com.mymes.equip.tc.schedule.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mymes.equip.tc.Condition;
import com.mymes.equip.tc.Condition.PageInfo;
import com.mymes.equip.tc.Condition.SortDirection;
import com.mymes.equip.tc.persist.PersistentService;
import com.mymes.equip.tc.schedule.ScheduleConstants;
import com.mymes.equip.tc.schedule.ScheduleJobDetailInfo;
import com.mymes.equip.tc.schedule.ScheduleJobExecHistoryInfo;
import com.mymes.equip.tc.schedule.ScheduleJobExecHistoryService;
import com.mymes.equip.tc.schedule.ScheduleJobInfo;
import com.mymes.equip.tc.schedule.ScheduleJobService;
import com.mymes.equip.tc.schedule.ScheduleManager;
import com.mymes.equip.tc.schedule.TriggerStatusInfo;
import com.mymes.equip.tc.session.SessionContext;
import com.mymes.equip.tc.util.ListUtils;
import com.mymes.equip.tc.util.MapUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduleJobServiceImpl implements ScheduleJobService, PersistentService<ScheduleJobInfo, ScheduleJobEntity> {
	
	@Autowired
	private ScheduleManager scheduleJobManager;
	
	@Autowired
	private ScheduleJobRepository scheduleJobRepository;
	
	@Autowired
	private ScheduleJobDetailServiceImpl scheduleJobDetailService;
	
	@Autowired
	private ScheduleJobExecHistoryService scheduleJobExecHistoryService;
	
	// functions for schedule job
	//====================================================================================

	@Override
	public List<TriggerStatusInfo> getRunningScheduleJobs() {
		log.debug("");
		List<TriggerStatusInfo> triggerList = new ArrayList<>();
		List<String> triggerGroupNames = scheduleJobManager.getTriggerGroupNames();
		triggerGroupNames.forEach((triggerGroupName)->{
			List<String> triggerNames = scheduleJobManager.getTriggerNames(triggerGroupName);
			triggerNames.forEach((triggerName)->{
				Trigger trigger = scheduleJobManager.getTrigger(triggerName, triggerGroupName);
				
				TriggerStatusInfo triggerStatus = new TriggerStatusInfo();
				
				triggerStatus.setName(triggerName);
				triggerStatus.setGroupName(triggerGroupName);
				triggerStatus.setStatus(scheduleJobManager.getTriggerState(triggerName, triggerGroupName));
				String triggerType =  null;
				if(trigger.getClass().getSimpleName().contains("Cron")) {
					triggerType = "CRON";
				} else {
					triggerType = "SIMPLE";
				}
				triggerStatus.setType(triggerType);
				triggerStatus.setExecutionTime(trigger.getPreviousFireTime());
				triggerStatus.setNextFireTime(trigger.getNextFireTime());
				triggerList.add(triggerStatus);
			});
		});

		return triggerList;
	}

	@Override
	public void loadSchedules() {
		log.debug("ScheduleJobServiceImpl.loadSchedules() ...");
		int count=scheduleAll();
		log.info("Schedule jobs are loaded, count: {}", count);
	}
	
	@Override
	public void reloadSchedules() {
		log.debug("ScheduleJobServiceImpl.reloadSchedules() ...");
		scheduleJobManager.getJobGroupNames().forEach((jobGroupName)->{
			scheduleJobManager.getTriggerNames(jobGroupName).forEach((jobName)->{
				scheduleJobManager.deleteJob(jobName, jobGroupName);
				log.info("deleted job: {}" + jobName);
			});
		});
		log.info("Deleted all schedule jobs.");
		
		loadSchedules();
	}
	
	@Override
	public int startScheduleJobDatabaseStore(String harewareType) {
		log.debug("");
		// 서버 인스턴스 이름을 설정한 후에 다시 시작?? => 다중화 문제를 심각하게 고민해야 함
		return this.scheduleAll();
	}
	
	@Override
	public int scheduleAll() {
		log.debug("ScheduleJobServiceImpl.scheduleAll() ...");
		int count = 0;
		
		List<ScheduleJobInfo> scheduleJobs=this.searchScheduleJobs();
		for (Iterator<ScheduleJobInfo> iterator=scheduleJobs.iterator(); iterator.hasNext();) {
				
			ScheduleJobInfo scheduleJob=iterator.next();
			boolean result=schedule(scheduleJob);
			if(result) {
				count++;
			}
		}

		logScheduleJobs();
		return count;
	}

	@Override
	public boolean schedule(String scheduleJobName) {
		log.debug("");

		boolean result=false;
		
		ScheduleJobInfo scheduleJob=this.findScheduleJobByName(scheduleJobName);
		if(scheduleJob!=null) {
			result=schedule(scheduleJob);
		}else {
			log.warn("schedulerJob {}  does not be exsit.", scheduleJobName);
		}
		
		return result;
	}

	@Override
	public boolean schedule(ScheduleJobInfo scheduleJob) {
		log.debug("");

		boolean result=false;
		try {			
			if(scheduleJob.getUse()==true) {
				log.info("schedulerJob {} will be created.", scheduleJob.getName());

				// create jobDetail
				JobDetail jobDetail=createJobDetail(scheduleJob);

				// if trigger is existed, remove it first (trigger and job is mapped one by one)		
				Trigger trigger=this.scheduleJobManager.getTrigger(scheduleJob.getName(), scheduleJob.getGroupName());
				if(trigger!=null) {
					this.scheduleJobManager.deleteJob(scheduleJob.getName(), scheduleJob.getGroupName());
				}

				// create trigger
				trigger = this.createTrigger(scheduleJob, jobDetail);				

				// schedule
				log.info("schedulerJob {} willl be scheduled, state: {}, {}", scheduleJob.getName(), this.scheduleJobManager.getTriggerState(trigger), scheduleJob);
				result=this.scheduleJobManager.scheduleJob(jobDetail, trigger);
					
				// update state at the database
				String state=this.scheduleJobManager.getTriggerState(trigger);
				this.updateScheduleJobState(scheduleJob.getId(), state);
			}else {
				log.info("schedulerJob {}.used is set to False, can not schedule {}", scheduleJob.getName(), scheduleJob);
			}
		}catch (ClassNotFoundException e) {
			log.error("jobClass {} does not exist. schedule job: {}.", scheduleJob.getJobClass(), scheduleJob, e);
		}
		return result;
	}

	@Override
	public boolean unschedule(String schedulerJobName) {
		log.debug("");
		boolean result=false;
		
		ScheduleJobInfo scheduleJob=this.findScheduleJobByName(schedulerJobName);
		if(scheduleJob!=null) {
			result=this.unschedule(scheduleJob);
		} else {
			log.warn("scheduleJob {} does not exist.", schedulerJobName);
		}

		return result;
	}

	@Override
	public boolean unschedule(ScheduleJobInfo scheduleJob) {
		log.debug("");
		
		log.info("scheduleJob {} will be unscheduled.", scheduleJob.getName());
		boolean result=this.scheduleJobManager.unscheduleJob(scheduleJob.getName(), scheduleJob.getGroupName());
		
		String state=this.scheduleJobManager.getTriggerState(scheduleJob.getName(), scheduleJob.getGroupName());
		this.updateScheduleJobState(scheduleJob.getId(), state);

		return result;
	}
	
	@Override
	public boolean unscheduleByBatch(List<String> scheduleJobNames) {
		log.debug("");
		boolean result=true;
		for(String name:scheduleJobNames) {
			boolean success=unschedule(name);
			if(!success) result=false;
		}
		return result;
	}
	
	@Override
	public boolean reschedule(String scheduleJobName) {
		log.debug("");
		

		boolean result=false;
		ScheduleJobInfo scheduleJob=this.findScheduleJobByName(scheduleJobName);
		if(scheduleJob!=null) {			
			result=this.reschedule(scheduleJob);
		} else {
			log.warn("schedulerJob {} does not exist.", scheduleJobName);
			result=this.schedule(scheduleJobName);
		}
		
		return result;
	}

	@Override
	public boolean reschedule(ScheduleJobInfo scheduleJob) {
		log.debug("");
		
		boolean result=false;
		log.info("scheduleJob {}:{} will be rescheduled.", scheduleJob, scheduleJob.getName());
		result=this.schedule(scheduleJob);
		
		return result;
	}
	
	@Override
	public boolean rescheduleByBatch(List<String> scheduleJobNames) {
		log.debug("");
		boolean result=true;
		for(String name:scheduleJobNames) {
			boolean success=reschedule(name);
			if(!success) result=false;
		}
		return result;
	}
	
	@Override
	public boolean pause(String scheduleJobName) {
		log.debug("");
	
		boolean result=false;
		ScheduleJobInfo scheduleJob=this.findScheduleJobByName(scheduleJobName);
		if(scheduleJob!=null) {
			result=this.pause(scheduleJob);
		}else {
			log.warn("scheduleJob {} does not exist.", scheduleJobName);
		}
		
		return result;
	}

	@Override
	public boolean pause(ScheduleJobInfo scheduleJob) {
		log.debug("");
		
		boolean result=false;
		Trigger trigger=this.scheduleJobManager.getTrigger(scheduleJob.getName(), scheduleJob.getGroupName());
		if(trigger!=null) {
			log.info("schedulerJob {} will be paused.", scheduleJob.getName());
			result=this.scheduleJobManager.pauseTrigger(scheduleJob.getName(), scheduleJob.getGroupName());

			// update state			
			String state=this.scheduleJobManager.getTriggerState(scheduleJob.getName(), scheduleJob.getGroupName());
			this.updateScheduleJobState(scheduleJob.getName(), state);
		}else {
			log.warn("trigger {} does not exist.", scheduleJob.getName());
		}
		this.scheduleJobManager.logTriggers();
		
		return result;
	}
	
	@Override
	public boolean pauseByBatch(List<String> scheduleJobNames) {
		log.debug("");
		boolean result=true;
		for(String name:scheduleJobNames) {
			boolean success=pause(name);
			if(!success) result=false;
		}
		return result;
	}
	
	@Override
	public boolean resume(String scheduleJobName) {
		log.debug("");

		boolean result=false;
		
		ScheduleJobInfo jobInfo=this.findScheduleJobByName(scheduleJobName);
		if(jobInfo!=null) {
			result=this.resume(jobInfo);
		}else {
			log.warn("schedulerJob {} does not exist.", scheduleJobName);
		}
		
		return result;
	}

	@Override
	public boolean resume(ScheduleJobInfo scheduleJob) {
		log.debug("");

		boolean result=false;
		Trigger trigger=this.scheduleJobManager.getTrigger(scheduleJob.getName(), scheduleJob.getGroupName());
		if(trigger!=null) {
			log.info("schedulerJob {} will be resumed.", scheduleJob.getName());
			result=this.scheduleJobManager.resumeTrigger(scheduleJob.getName(), scheduleJob.getGroupName());
			// update state			
			String state=this.scheduleJobManager.getTriggerState(scheduleJob.getName(), scheduleJob.getGroupName());
			this.updateScheduleJobState(scheduleJob.getName(), state);
		}else {
			log.warn("trigger {} does not exist.", scheduleJob.getName());
		}
		// display currentTriggers
		this.scheduleJobManager.logTriggers();

		return result;
	}
	
	@Override
	public boolean resumeByBatch(List<String> scheduleJobNames) {
		log.debug("");
		boolean result=true;
		for(String name:scheduleJobNames) {
			boolean success=resume(name);
			if(!success) result=false;
		}
		return result;
	}

	@Override
	public boolean pauseAll() {
		log.debug("");

		boolean result=this.scheduleJobManager.pauseAll();
		this.updateScheduleJobsState();

		return result;
	}

	@Override
	public boolean resumeAll() {
		log.debug("");

		boolean result=this.scheduleJobManager.resumeAll();
		this.updateScheduleJobsState();

		return result;
	}
	
	@Override
	public boolean updateScheduleJobsState() {
		log.debug("");

		boolean result=true;
		
		List<ScheduleJobInfo> scheduleJobs=this.searchScheduleJobs();
		for (Iterator<ScheduleJobInfo> iterator=scheduleJobs.iterator(); iterator.hasNext();) {
			ScheduleJobInfo schedulerJob=(ScheduleJobInfo) iterator.next();

			String state=this.scheduleJobManager.getTriggerState(schedulerJob.getName(), schedulerJob.getGroupName());
			this.updateScheduleJobState(schedulerJob.getName(), state);
		}
		return result;
	}
	
	@Override
	public void logScheduleHistoryInfo() {
		String groupName="HISTORY";

		this.scheduleJobManager.logTriggerGroup(groupName);
		this.scheduleJobManager.logJobGroup(groupName);
	}

	@Override
	public void logScheduleLogInfo() {
		String groupName = "LOG";

		this.scheduleJobManager.logTriggerGroup(groupName);
		this.scheduleJobManager.logJobGroup(groupName);	
	}

	@Override
	public void logScheduleAwakeInfo() {
		String groupName = "AWAKE";

		this.scheduleJobManager.logTriggerGroup(groupName);
		this.scheduleJobManager.logJobGroup(groupName);
	}

	@Override
	public void logScheduleDefaultInfo() {
		String groupName = "DEFAULT";

		this.scheduleJobManager.logTriggerGroup(groupName);
		this.scheduleJobManager.logJobGroup(groupName);		
	}
	
	@Override
	public void logScheduleJobs() {
		log.info(this.scheduleJobManager.getJobsToPrettyFormat().toString() + this.scheduleJobManager.getTriggersToPrettyFormat().toString());
	}

	//======================================================================================
	// functions for schedule job's information 
	@Transactional
	@Override
	public void registerScheduleJob(ScheduleJobInfo scheduleJob) {
		log.debug("");
		ScheduleJobEntity entity=new ScheduleJobEntity();
		entity.from(scheduleJob);
		// jobDetail까지 모두 한번에 저장됨 (확인 필요)
		scheduleJobRepository.save(entity);
		
		scheduleJob.getJobDetails().forEach((jobDetail)->{
			jobDetail.setScheduleJobId(scheduleJob.getId());
			ScheduleJobDetailEntity detailEntity = new ScheduleJobDetailEntity();
			detailEntity.from(jobDetail);
			this.createScheduleJobDetail(jobDetail);
		});
	}
	
	@Override
	public List<ScheduleJobInfo> searchScheduleJobs(Condition cond) {
		log.debug("");
		ScheduleJobSpecs specs=new ScheduleJobSpecs();
		List<ScheduleJobEntity> scheduleJobEntityList=null;
		Specification<ScheduleJobEntity> specification=specs.createSpecification(cond);

		if(cond.getPageInfo()==null) {
			scheduleJobEntityList=scheduleJobRepository.findAll(specification);
		} else {
			PageInfo pInfo=cond.getPageInfo();
			Sort.Direction dir=pInfo.getSortDirection()==SortDirection.ASCENDING?Sort.Direction.ASC:Sort.Direction.DESC;
			PageRequest prequest=PageRequest.of(pInfo.getPage(), pInfo.getSize(), dir, pInfo.getSortBy());
			scheduleJobEntityList=scheduleJobRepository.findAll(specification, prequest).getContent();
		}

		return infos(scheduleJobEntityList);
	}

	@Override
	public List<ScheduleJobInfo> searchScheduleJobs(Condition cond, PageInfo pageInfo) {
		log.debug("");
		cond.setPageInfo(pageInfo);
		return searchScheduleJobs(cond);
	}

	@Override
	public List<ScheduleJobInfo> searchScheduleJobs() {
		log.debug("");
		return infos(scheduleJobRepository.findAll());
	}
	
	@Override
	public List<ScheduleJobInfo> searchScheduleJobs(PageInfo pageInfo) {
		log.debug("");
		Sort.Direction dir=pageInfo.getSortDirection()==SortDirection.ASCENDING?Sort.Direction.ASC:Sort.Direction.DESC;
		PageRequest prequest=PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), dir, pageInfo.getSortBy());
		return infos(scheduleJobRepository.findAll(prequest).getContent());
	}
	
	@Override
	public ScheduleJobInfo findScheduleJobById(String scheduleJobId) {
		log.debug("");
		return scheduleJobRepository.findById(scheduleJobId).get().info();
	}

	@Override
	public ScheduleJobInfo findScheduleJobByName(String scheduleJobName) {
		log.debug("");
		ScheduleJobEntity entity=this.findScheduleJobEntityByName(scheduleJobName, null);
		if(entity!=null) return entity.info();
		return null;
	}
	
	@Override
	public ScheduleJobInfo findScheduleJobByName(String scheduleJobName, String schedulerName) {
		log.debug("");
		ScheduleJobEntity entity=this.findScheduleJobEntityByName(scheduleJobName, schedulerName);
		if(entity==null) return null;
		return entity.info();
	}
	
	@Override
	@Transactional
	public void updateScheduleJob(ScheduleJobInfo scheduleJob) {
		log.debug("");
		ScheduleJobEntity jobEntity=scheduleJobRepository.findById(scheduleJob.getId()).get();
		jobEntity.from(scheduleJob);
		scheduleJobRepository.save(jobEntity);
		
		scheduleJob.getJobDetails().forEach((jobDetail)->{
			jobDetail.setScheduleJobId(scheduleJob.getId());
			ScheduleJobDetailEntity detailEntity = new ScheduleJobDetailEntity();
			detailEntity.from(jobDetail);
			this.createScheduleJobDetail(jobDetail);
		});
	}
	
	@Override
	@Transactional
	public void updateScheduleJobSchedulerName(String scheduleJobName, String schedulerName) {
		log.debug("");
		ScheduleJobEntity jobEntity=this.findScheduleJobEntityByName(scheduleJobName, null);
		jobEntity.setSchedulerName(schedulerName);
		scheduleJobRepository.save(jobEntity);
	}
	
	private ScheduleJobEntity findScheduleJobEntityByName(String scheduleJobName, String schedulerName) {
		ScheduleJobEntity probe=new ScheduleJobEntity();
		probe.setName(scheduleJobName);
		if(schedulerName!=null) {
			probe.setSchedulerName(schedulerName);
		}
		
		ExampleMatcher matcher = ExampleMatcher.matchingAny();
		matcher=matcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.exact());
		Example<ScheduleJobEntity> example=Example.of(probe, matcher);

		try {
			return scheduleJobRepository.findOne(example).get();
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	@Override
	@Transactional
	public void updateScheduleJobState(String scheduleJobId, String state) {
		log.debug("");
		ScheduleJobEntity jobEntity=scheduleJobRepository.findById(scheduleJobId).get();
		jobEntity.setTriggerState(state);
		scheduleJobRepository.save(jobEntity);
	}

	@Override
	@Transactional
	public void deleteScheduleJob(ScheduleJobInfo scheduleJob) {
		 log.debug("");
		 ScheduleJobEntity jobEntity=scheduleJobRepository.findById(scheduleJob.getId()).get();
		 scheduleJobRepository.delete(jobEntity);
	}
	
	@Override
	@Transactional
	public void deleteScheduleJobById(String scheduleJobId) {
		log.debug("");
		scheduleJobRepository.deleteById(scheduleJobId);
	}

	@Override
	@Transactional
	public void deleteScheduleJobByName(String scheduleJobName) {
		log.debug("");
		scheduleJobRepository.deleteByName(scheduleJobName);
	}
	
	@Override
	@Transactional
	public void deleteScheduldJobsByBatch(List<String> scheduleJobIds) {
		log.debug("");
		scheduleJobRepository.deleteByBatch(scheduleJobIds);
	}
	
	@Override
	@Transactional
	public void createScheduleJobDetail(ScheduleJobDetailInfo scheduleJobDetail) {
		log.debug("");
		scheduleJobDetailService.createScheduleJobDetail(scheduleJobDetail);
	}
	
	@Override
	public List<ScheduleJobDetailInfo> searchScheduleJobDetails(ScheduleJobInfo scheduleJob) {
		log.debug("");
		return scheduleJobDetailService.searchScheduleJobDetails(scheduleJob);
	}
	
	@Override
	public List<ScheduleJobDetailInfo> searchScheduleJobDetails(String scheduleJobId) {
		log.debug("");
		return scheduleJobDetailService.searchScheduleJobDetails(scheduleJobId);
	}
	
	public List<ScheduleJobDetailInfo> searchScheduleJobDetailsByJobName(String scheduleJobName) {
		log.debug("");
		String jobId=this.findScheduleJobByName(scheduleJobName).getId();
		return this.searchScheduleJobDetails(jobId);
	}
	
	@Override
	@Transactional
	public void updateScheduleJobDetail(ScheduleJobDetailInfo scheduleJobDetail) {
		log.debug("");
		scheduleJobDetailService.updateScheduleJobDetail(scheduleJobDetail);
	}

	@Override
	@Transactional
	public void deleteScheduleJobDetails(ScheduleJobInfo scheduleJob) {
		log.debug("");
		scheduleJobDetailService.deleteScheduleJobDetails(scheduleJob);
	}

	@Override
	@Transactional
	public void deleteScheduleJobDetails(String scheduleJobId) {
		log.debug("");
		scheduleJobDetailService.deleteScheduleJobDetails(scheduleJobId);
	}

	@Override
	@Transactional
	public void deleteScheduleJobDetail(ScheduleJobDetailInfo scheduleJobDetail) {
		log.debug("");
		scheduleJobDetailService.deleteScheduleJobDetail(scheduleJobDetail);
	}
	
	@Override
	@Transactional
	public void deleteScheduleJobDetail(String scheduleJobId, String entryKey) {
		log.debug("");
		scheduleJobDetailService.deleteScheduleJobDetail(scheduleJobId, entryKey);
	}

	@Override
	@Transactional
	public void saveScheduleJobExecHistory(ScheduleJobExecHistoryInfo scheduleJobExecHistory) {
		log.debug("");
		scheduleJobExecHistoryService.saveScheduleJobExecHistory(scheduleJobExecHistory);
	}
	
	@Override
	@Transactional
	public void deleteScheduleJobExecHistory(long seqNo) {
		log.debug("");
		scheduleJobExecHistoryService.deleteScheduleJobExecHistory(seqNo);
	}
	
	@Override
	@Transactional
	public void deleteScheduleJobExecHistory(String scheduleJobName) {
		log.debug("");
		scheduleJobExecHistoryService.deleteScheduleJobExecHistory(scheduleJobName);
	}
	
	@Override
	public List<ScheduleJobExecHistoryInfo> searchScheduleJobExecHistory() {
		log.debug("");
		return scheduleJobExecHistoryService.searchScheduleJobExecHistory();
	}
	
	@Override
	public List<ScheduleJobExecHistoryInfo> searchScheduleJobExecHistory(PageInfo pageInfo) {
		log.debug("");
		return scheduleJobExecHistoryService.searchScheduleJobExecHistory(pageInfo);
	}
	
	@Override
	public List<ScheduleJobExecHistoryInfo> searchScheduleJobExecHistory(Condition cond) {
		log.debug("");
		return scheduleJobExecHistoryService.searchScheduleJobExecHistory(cond);
	}
	
	@Override
	public List<ScheduleJobExecHistoryInfo> searchScheduleJobExecHistory(Condition cond, PageInfo pageInfo) {
		log.debug("");
		return scheduleJobExecHistoryService.searchScheduleJobExecHistory(cond, pageInfo);
	}
	
	@SuppressWarnings("unchecked")
	private JobDetail createJobDetail(ScheduleJobInfo scheduleJob) throws ClassNotFoundException {
		log.debug("");

		JobDataMap jobData=new JobDataMap();
		ScheduleJobEntity jobEntity=new ScheduleJobEntity();
		jobEntity.from(scheduleJob);
		List<ScheduleJobDetailInfo> scheduleJobDetails=this.searchScheduleJobDetails(scheduleJob.getName());

		for (Iterator<ScheduleJobDetailInfo> iterator=scheduleJobDetails.iterator(); iterator.hasNext();) {
			ScheduleJobDetailInfo scheduleJobDetail=(ScheduleJobDetailInfo) iterator.next();
			
			String valueType=StringUtils.defaultString(scheduleJobDetail.getEntryValueType());
			if(valueType.equals(ScheduleConstants.ENTRY_VAULE_TYPE_REF)) {
				Object obj = SessionContext.getGlobalSessionContext().getApplicationContext().getBean(scheduleJobDetail.getEntryValue());
				jobData.put(scheduleJobDetail.getEntryKey(), obj);
			}else if (valueType.equals(ScheduleConstants.ENTRY_VAULE_TYPE_LIST)) {
				jobData.put(scheduleJobDetail.getEntryKey(), ListUtils.stringToList(scheduleJobDetail.getEntryValue()));
			}else if (valueType.equals(ScheduleConstants.ENTRY_VAULE_TYPE_MAP)) {
				jobData.put(scheduleJobDetail.getEntryKey(), MapUtils.stringToMap(scheduleJobDetail.getEntryValue()));
			}else {
				jobData.put(scheduleJobDetail.getEntryKey(), scheduleJobDetail.getEntryValue());
			}
			log.info("schedulerJobDetail {" + scheduleJobDetail.getEntryKey() + "} is assigned as {}", scheduleJobDetail.getEntryKey(), scheduleJobDetail.getEntryValue());
		}
		
		String groupName=StringUtils.isNotEmpty(scheduleJob.getGroupName()) ? scheduleJob.getGroupName() : "UNDEFINED";
		if(scheduleJob.getJobClass()==null) {
			scheduleJob.setJobClass((Class<Job>)Class.forName(scheduleJob.getJobClassName()));
		}
		JobDetail jobDetail=JobBuilder.newJob()
				                      .ofType(scheduleJob.getJobClass())
                					  .withIdentity(scheduleJob.getName(), groupName)
                					  .usingJobData(jobData)
                					  .build();
		return jobDetail;
	}
	
	private Trigger createTrigger(ScheduleJobInfo schedulerJob, JobDetail jobDetail) {
		
		long startDelay=schedulerJob.getStartDelay();
		long repeatInterval=schedulerJob.getRepeatInterval();
		
		Trigger trigger=null;
		
		if(schedulerJob.getTriggerType().equalsIgnoreCase(ScheduleConstants.TRIGGER_TYPE_CRON)) {
			
			trigger=TriggerBuilder.newTrigger()
              				      .withIdentity(schedulerJob.getName(), schedulerJob.getGroupName())
              				      .startAt(new Date(Calendar.getInstance().getTimeInMillis() + startDelay))
              				      .withSchedule(CronScheduleBuilder.cronSchedule(schedulerJob.getCronExpression()))
              				      .forJob(jobDetail)
              				      .build();
		} else {
			trigger=TriggerBuilder.newTrigger()
                      		      .withIdentity(schedulerJob.getName(), schedulerJob.getGroupName())
                      		      .startAt(new Date(Calendar.getInstance().getTimeInMillis() + startDelay))
                      		      .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(repeatInterval).withRepeatCount(5))
                      		      .forJob(jobDetail)
                      		      .build();
		}
		
		return trigger;
	}
}
