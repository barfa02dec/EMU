package com.capitalone.dashboard.service;

import java.util.Set;

import org.bson.types.ObjectId;

import com.capitalone.dashboard.model.Project;
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
	
	/*Iterable<Project> all();
	
	
	

	
	
	
	
	*/

}
