package org.weaver.alr.batch.model;

import java.util.List;

public class SettingVO {
	
	private List<TaskVO> tasks;
	
	private ReloadVO reload;

	public List<TaskVO> getTasks() {
		return tasks;
	}

	public ReloadVO getReload() {
		return reload;
	}

	public void setTasks(List<TaskVO> tasks) {
		this.tasks = tasks;
	}

	public void setReload(ReloadVO reload) {
		this.reload = reload;
	}
	
	
	
	

}
