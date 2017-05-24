package com.capitalone.dashboard.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.model.Project;

public interface ProjectRepository extends PagingAndSortingRepository<Project, ObjectId>{
	
	Project findByProjectName(String projectName);

}
