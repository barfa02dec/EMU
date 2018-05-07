package com.capitalone.dashboard.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProjectRequest {
	
	@NotNull
    @Size(min=3,max=40, message="Project Name should be min=3 & max=50 characters")
	private String projectName;
	
	@NotNull
    @Size(min=3,max=40, message="Project Id should be min=3 & max=40 characters")
	private String projectId;
	
	@NotNull
    @Size(min=3,max=40, message="Business Unit should be min=3 & max=20 characters")
	private String businessUnit;
	
	@NotNull
    @Size(min=3,max=40, message="Project Owner should be min=3 & max=50 characters")
	private String projectOwner;

	@NotNull
    @Size(min=3,max=40, message="Client should be min=3 & max=40 characters")
	private String client;

	private String id;
	private boolean projectStatus;
	private String program;
	private boolean editorEnabled;
	private String user;
	private String customerName;
	private String customerCode;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
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
