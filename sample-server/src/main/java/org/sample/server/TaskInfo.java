package org.sample.server;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskInfo {

	private String id;
	
	private String name;
	
	private String assignee;
	
	private Date created;
	
	private Date due;
	
	private Date followUp;
	
	private Date lastUpdated;
	
	private String delegationState;
	
	private String description;
	
	private String executionId;
	
	private String owner;
	
	private String parentTaskId;
	
	private Integer priority;
	
	private String processDefinitionId;
	
	private String processInstanceId;
	
	private String caseDefinitionId;
	
	private String caseInstanceId;
	
	private String caseExecutionId;
	
	private String taskDefinitionKey;
	
	private Boolean suspended;
	
	private String formKey;
	
	private String tenantId;
	
	private String taskState;
}
