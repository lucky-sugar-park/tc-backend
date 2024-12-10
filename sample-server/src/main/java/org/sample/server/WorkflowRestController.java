package org.sample.server;

import java.net.URI;
import java.util.List;

import org.camunda.community.rest.client.api.ProcessDefinitionApi;
import org.camunda.community.rest.client.api.TaskApi;
import org.camunda.community.rest.client.dto.TaskDto;
import org.camunda.community.rest.client.dto.TaskQueryDto;
import org.camunda.community.rest.client.invoker.ApiException;
import org.sample.server.Query.Qunit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class WorkflowRestController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private TaskApi taskApi;
	
	@Autowired
    private ProcessDefinitionApi processDefinitionApi;

	@GetMapping(value="/todo-list/{userId}")
	@CrossOrigin
	public TaskInfo[] todoList(@PathVariable("userId") String userId) {
		log.info("todoList is requested ...");
		
		TaskQueryDto tqdto=new TaskQueryDto();
		
		List<TaskDto> tasks=null;
		try {
			tasks=taskApi.queryTasks(1, 100, tqdto);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		
		URI uri=UriComponentsBuilder
				.fromUriString("http://localhost:8080").path("/engine-rest/task").encode().build().toUri();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		RequestEntity<Query> requestEntity=RequestEntity.post(uri).body(makeQuery(userId));
		
		ResponseEntity<TaskInfo[]> responseEntity=restTemplate.exchange(requestEntity, TaskInfo[].class);
		return responseEntity.getBody();
	}
	
	private Query makeQuery(String userId) {
		Query query=new Query();

		Qunit qunit=query.new Qunit();
		qunit.setName("assignee");
		qunit.setValue(userId);
		qunit.setOperator("eq");
		query.add(qunit);

		return query;
	}
}
