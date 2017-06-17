package com.capitalone.dashboard.model;

import java.util.Set;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "project")
public class Project extends BaseModel {
	@Indexed(unique = true)
	private String projectName;
	private String projectId;
	private boolean projectStatus;
	private String BusinessUnit;
	private String ProjectOwner;
	private String program;
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
	public String getBusinessUnit() {
		return BusinessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		BusinessUnit = businessUnit;
	}
	public String getProjectOwner() {
		return ProjectOwner;
	}
	public void setProjectOwner(String projectOwner) {
		ProjectOwner = projectOwner;
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
	
	
	
	
	

}
