package com.capitalone.dashboard.model;

import java.util.Set;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "project")
public class Project extends BaseModel {

	@Indexed(unique = true, name="index_Project_projectId")
	private String projectId;
	
	private String projectName;
	private boolean projectStatus;
	private String businessUnit;
	private String projectOwner;
	private String program;
	private String client;
	private Set<UserGroup> usersGroup;
	
	private String customerName;
	private String customerCode;
	private boolean automated;
		
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
	public boolean isAutomated() {
		return automated;
	}
	public void setAutomated(boolean automated) {
		this.automated = automated;
	}
}
