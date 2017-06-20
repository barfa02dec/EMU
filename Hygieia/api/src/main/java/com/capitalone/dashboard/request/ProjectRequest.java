package com.capitalone.dashboard.request;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProjectRequest {
	
	@NotNull
    @Size(min=1, message="Please provide projectName")
	private String projectName;
	@NotNull
    @Size(min=1, message="Please provide projectId")
	private String projectId;
	private boolean projectStatus;
	@NotNull
    @Size(min=1, message="Please provide businessUnit")
	private String businessUnit;
	@NotNull
    @Size(min=1, message="Please provide projectOwner")
	private String projectOwner;
	private String program;
	@NotNull
    @Size(min=1, message="Please provide client details")
	private String client;
	private Set<String> projectUsersList;
	
	
	
	public Set<String> getProjectUsersList() {
		return projectUsersList;
	}
	public void setProjectUsersList(Set<String> projectUsersList) {
		this.projectUsersList = projectUsersList;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public boolean isProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(boolean projectStatus) {
		this.projectStatus = projectStatus;
	}
	
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	public String getProjectOwner() {
		return projectOwner;
	}
	public void setProjectOwner(String projectOwner) {
		this.projectOwner = projectOwner;
	}
	
	
	

}
