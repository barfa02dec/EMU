package com.capitalone.dashboard.service;

import com.capitalone.dashboard.model.Project;

public interface ProjectService {
	
	Iterable<Project> all();
	
	String create(String projectName);

}
