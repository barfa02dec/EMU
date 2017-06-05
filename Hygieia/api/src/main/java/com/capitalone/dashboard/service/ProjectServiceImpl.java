package com.capitalone.dashboard.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.model.Project;
import com.capitalone.dashboard.repository.ProjectRepository;
import com.capitalone.dashboard.request.ProjectRequest;

@Service
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;

	@Autowired
	public ProjectServiceImpl(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	@Override
	public Iterable<Project> all() {

		return projectRepository.findAll(new Sort(Sort.Direction.ASC, "projectName"));
	}

	@Override
	public Project create(ProjectRequest projectRequest) {
		Project project=new Project();
		project.setProjectName(projectRequest.getProjectName());
		project.setProjectActiveStatus(projectRequest.isProjectActiveStatus());
		project.setProjectId(projectRequest.getProjectId());
		return projectRepository.save(project);

	}

	@Override
	public Project deactivateProject(ObjectId id) {
			
			Project project = projectRepository.findOne(id);
			if (project != null) {
				project.setProjectActiveStatus(false);
				return projectRepository.save(project);
				
			}else
			{
				return null;
			}

	}

	@Override
	public Project updateProject(ProjectRequest projectRequest) {

			Project project=projectRepository.findByIdAndName(projectRequest.getProjectId(), projectRequest.getProjectName());
			if(null==project){
				project= new Project();
				project.setProjectName(projectRequest.getProjectName());
				project.setProjectId(projectRequest.getProjectId());
				project.setProjectActiveStatus(projectRequest.isProjectActiveStatus());
				return projectRepository.save(project);
			}
			if(null!=projectRequest.getProjectName())
			{
				project.setProjectName(projectRequest.getProjectName());
			}
			
			if(null!=projectRequest.getProjectId()){
				project.setProjectId(projectRequest.getProjectId());

			}
			return projectRepository.save(project);
		

	
	}

}
