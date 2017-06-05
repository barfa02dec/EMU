package com.capitalone.dashboard.request;

import javax.validation.constraints.NotNull;

public class ProjectRequest {
	
	@NotNull
	private String projectName;
	@NotNull
	private String projectId;
	@NotNull
	private boolean projectActiveStatus;
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public boolean isProjectActiveStatus() {
		return projectActiveStatus;
	}

	public void setProjectActiveStatus(boolean projectActiveStatus) {
		this.projectActiveStatus = projectActiveStatus;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	

}
