package com.mymes.equip.tc.schedule;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ScheduleConfig {

	@Autowired
    private ScheduleTriggerListener triggerListener;

    @Autowired
    private ScheduleJobListener jobListener;

    @Bean
	public JobFactory jobFactory(ApplicationContext applicationContext) {
		log.info("Creating JobFactory...");
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}
    
    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) {
    	return schedulerFactoryBean.getScheduler();
    }
    
    @Bean
	public SchedulerFactoryBean schedulerFactoryBean(ApplicationContext applicationContext, DataSource dataSource) throws IOException {
    	log.info("Creating ScheduleFactoryBaen...");
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		schedulerFactory.setQuartzProperties(quartzProperties());
	    schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
	    schedulerFactory.setAutoStartup(true);
	    schedulerFactory.setJobFactory(jobFactory(applicationContext));
	    schedulerFactory.setDataSource(dataSource);
	    schedulerFactory.setGlobalTriggerListeners(triggerListener);
	    schedulerFactory.setGlobalJobListeners(jobListener);
	    schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
	    return schedulerFactory;
	}
    
    public Properties quartzProperties() throws IOException {
	    PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
	    propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
	    propertiesFactoryBean.afterPropertiesSet();
	    return propertiesFactoryBean.getObject();
	}
}
