package org.sample.server;

import java.util.ArrayList;
import java.util.List;

public class TaskInfoList {

	List<TaskInfo> taskInfos;
	
	public TaskInfoList() {
		setTaskInfos(new ArrayList<>());
	}

	public List<TaskInfo> getTaskInfos() {
		return taskInfos;
	}

	public void setTaskInfos(List<TaskInfo> taskInfos) {
		this.taskInfos = taskInfos;
	}
}
