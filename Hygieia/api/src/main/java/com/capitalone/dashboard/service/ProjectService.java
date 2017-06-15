package com.capitalone.dashboard.service;

import org.bson.types.ObjectId;

import com.capitalone.dashboard.model.Project;
import com.capitalone.dashboard.request.ProjectRequest;

public interface ProjectService {
	
	Iterable<Project> all();
	
	Project create(ProjectRequest project);
	
	Project updateProject(ProjectRequest project);

	Project deactivateProject(ObjectId id);
	
	Project getProject(ObjectId id);

}
