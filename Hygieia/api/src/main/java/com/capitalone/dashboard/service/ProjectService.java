package com.capitalone.dashboard.service;

import org.bson.types.ObjectId;

import com.capitalone.dashboard.model.Project;
import com.capitalone.dashboard.model.UserRole;
import com.capitalone.dashboard.request.ProjectRequest;
import com.capitalone.dashboard.request.ProjectUserRoleRequest;

public interface ProjectService {
	
	Project create(ProjectRequest project);

	Project updateProject(ProjectRequest project);
	
	Iterable<Project> getProjectsOwnedByUser(String username);

	Project createProjectUserRoleMapping(ProjectUserRoleRequest projectUserRoleRequest);
	
	Project deactivateProject(ObjectId id);

	Iterable<Project> getActiveprojects();
	
	Project getProject(ObjectId id);
	
	Iterable<UserRole> getActiveprojectRolesOfUser( String projectId ,String user);
	
	String disassociatedUserFromProject(String user, String projectId);
	
	String createGlobalDeliveryUser(String user);
	
	String createAdditionalSysAdmins(String user);
	
	String purgeUser(String user);
}
