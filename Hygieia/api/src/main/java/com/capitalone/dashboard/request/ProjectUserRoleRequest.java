package com.capitalone.dashboard.request;

import java.util.Set;

import javax.validation.constraints.NotNull;

import com.capitalone.dashboard.model.Dashboard;
import com.capitalone.dashboard.model.UserRole;

public class ProjectUserRoleRequest {
	@NotNull
	private String user;
	@NotNull
	private String projectId;
	@NotNull
	private Set<UserRole> userRoles;
	private Set<Dashboard> dashboardsToAssign;
	
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Set<UserRole> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	public Set<Dashboard> getDashboardsToAssign() {
		return dashboardsToAssign;
	}
	public void setDashboardsToAssign(Set<Dashboard> dashboardsToAssign) {
		this.dashboardsToAssign = dashboardsToAssign;
	}
	
	

}
