package com.capitalone.dashboard.request;

import javax.validation.constraints.NotNull;

public class ProjectRequest {
	
	@NotNull
	private String projectName;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	

}
