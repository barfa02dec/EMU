package com.capitalone.dashboard.model;

import java.util.Set;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "project")
public class Project extends BaseModel {
	private String projectName;
	@Indexed(unique = true)
	private String projectId;
	private boolean projectStatus;
	private String BusinessUnit;
	private String ProjectOwner;
	private String program;
	private String client;
	private Set<UserGroup> usersGroup;
	private String createdBy;
	private String updatedBy;
	private String createdOn;
	private String updatedOn;
	
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
	public Set<UserGroup> getUsersGroup() {
		return usersGroup;
	}
	public void setUsersGroup(Set<UserGroup> usersGroup) {
		this.usersGroup = usersGroup;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	
	
}
