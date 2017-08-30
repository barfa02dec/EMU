package com.capitalone.dashboard.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.model.Project;

public interface ProjectRepository extends PagingAndSortingRepository<Project, ObjectId>{
	
	@Query(value = "{'projectId' : ?0}")
	Project findByProjectId(String projectName);
	
	@Query(value = "{'projectStatus' : ?0}")
	Iterable<Project> getAllActiveProjects(boolean status);

	@Query(value = "{ 'usersGroup.user' : ?0, 'projectStatus' : ?1 }")
	Iterable<Project> findByProjectUser(String user, boolean projectStatus);
		
	@Query(value = "{ 'projectId' : ?0, 'usersGroup.user' : ?1 }")
	Project getProject(String projectId, String user);
	
	
}
