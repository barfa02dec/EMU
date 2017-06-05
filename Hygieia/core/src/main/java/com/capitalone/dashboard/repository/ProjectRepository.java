package com.capitalone.dashboard.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.model.Project;

public interface ProjectRepository extends PagingAndSortingRepository<Project, ObjectId>{
	
	@Query(value = "{'projectId' : ?0, projectName' : ?1}")
	Project findByIdAndName(String projectId, String  projectName);

}
