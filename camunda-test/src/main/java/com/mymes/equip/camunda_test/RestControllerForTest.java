package com.mymes.equip.camunda_test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/test")
public class RestControllerForTest {
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ProcessEngine processEngine;
	
	@GetMapping("/info")
	public void test() {
//		ProcessEngineRule engineRule = new ProcessEngineRule();

		List<ProcessDefinition> pdefList=repositoryService.createProcessDefinitionQuery().latestVersion().list();
		pdefList.forEach(pdef->{
			System.out.println("PROCESS DEFINITION KEY => " + pdef.getKey());
			System.out.println("PROCESS DEFINITION ID  => " + pdef.getId());
//			ProcessDefinitionEntity pdefEntity=(ProcessDefinitionEntity)pdef;
//			pdefEntity.getTaskDefinitions().forEach((name, task)->{
//				System.out.println("NAME=>" + name);
//			});
//			pdefEntity.getActivities().forEach(activity->{
//				System.out.println("activity=>" + activity.getName());
//			});
//			repositoryService.getBpmnModelInstance("").getDefinitions();
			processEngine.getTaskService().createTaskQuery().processDefinitionKey(pdef.getKey()).list().forEach(task->{
				System.out.println("TASK ID: " + task.getId());
				System.out.println("TASK NAME: " + task.getName());
				System.out.println("TASK STATUS: " + task.getTaskState());
				Map<String, Object> param=new HashMap<>();
				param.put("isHoliday", false);
				taskService.complete(task.getId(), param);
			});
//			repositoryService.getBpmnModelInstance(pdef.getKey()).
//			ProcessInstance pi=runtimeService.startProcessInstanceById(pdef.getId(), "aaaa");
//			System.out.println("Business Key: " + pi.getBusinessKey());
//			System.out.println("INSTANCE ID : " + pi.getProcessInstanceId());
//			ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().singleResult();
//			InputStream is=repositoryService.getProcessDiagram(pdef.getId());
			System.out.println("RESOURCE_NAME==>"+repositoryService.getDeploymentResourceNames(pdef.getDeploymentId()));
			// 그냥 xml 얻어오는 코드임
//			InputStream is=repositoryService.getResourceAsStream(pdef.getDeploymentId(), repositoryService.getDeploymentResourceNames(pdef.getDeploymentId()).get(0));
//			try {
//				FileOutputStream fos=new FileOutputStream(new File("D:\\test-diagram.png"));
//				fos.write(is.readAllBytes());
//				fos.flush();
//				fos.close();
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//			// 얘는 xml 파일임
//			is=repositoryService.getProcessModel(pdef.getId());
//			try {
//				System.out.println(new String(is.readAllBytes()));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		});
	}
}
