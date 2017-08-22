package com.capitalone.dashboard.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProjectRequest {
	
	@NotNull
    @Size(min=3,max=40, message="projectName should be min=3,max=40 characters")
	private String projectName;
	@NotNull
    @Size(min=3,max=40, message="projectId should be min=3,max=40 characters")
	private String projectId;
	private boolean projectStatus;
	@NotNull
    @Size(min=3,max=40, message="businessUnit should be min=3,max=40 characters")
	private String businessUnit;
	@NotNull
    @Size(min=3,max=40, message="projectOwner should be min=3,max=40 characters")
	private String projectOwner;
	private String program;
	@NotNull
    @Size(min=3,max=40, message="client details should be min=3,max=40 characters")
	private String client;
	private String id;
	private boolean editorEnabled;
	private String user;
	public boolean isEditorEnabled() {
		return editorEnabled;
	}
	public void setEditorEnabled(boolean editorEnabled) {
		this.editorEnabled = editorEnabled;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
}
