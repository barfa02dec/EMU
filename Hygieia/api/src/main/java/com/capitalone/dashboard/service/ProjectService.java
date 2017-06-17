package com.capitalone.dashboard.service;

import java.util.Set;

import org.bson.types.ObjectId;

import com.capitalone.dashboard.model.Project;
import com.capitalone.dashboard.request.ProjectRequest;

public interface ProjectService {
	
	Iterable<Project> all();
	
	Iterable<Project> getProjectsOwnedByUser(String username);
	
	Project create(ProjectRequest project);
	
	Project updateProject(ProjectRequest project);

	Project deactivateProject(ObjectId id);
	
	Project getProject(ObjectId id);
	
	Project createUsers(String projectName, Set<String> users);

}
