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
		if(projectRequest!=null){
			mapProjectRequestToPojectForCreateProject(projectRequest, project);
		}
		return projectRepository.save(project);
	}

	@Override
	public Project deactivateProject(ObjectId id) {
			
			Project project = projectRepository.findOne(id);
			if (project != null) {
				project.setProjectStatus(false);
				return projectRepository.save(project);
				
			}else
			{
				return null;
			}

	}

	@Override
	public Project updateProject(ProjectRequest projectRequest) {

			Project project=projectRepository.findByProjectIdAndProjectName(projectRequest.getProjectName());
			if(null!=project)
			{
				mapProjectRequestToPojectForUpdateProject(projectRequest, project);
				return projectRepository.save(project);

			}
			
			return null;
		
	}
	
	private Project mapProjectRequestToPojectForCreateProject(ProjectRequest request, Project project ){
		project.setProjectId(request.getProjectId());
		project.setProjectName(request.getProjectName());
		project.setProjectOwner(request.getProjectOwner());
		project.setProjectStatus(request.isProjectStatus());
		project.setClient(request.getClient());
		project.setBusinessUnit(request.getBusinessUnit());
		project.setProgram(request.getProgram());
		return project;
	}
	private Project mapProjectRequestToPojectForUpdateProject(ProjectRequest request, Project project ){
		project.setProjectId(request.getProjectId());
		project.setProjectName(request.getProjectName());
		project.setProjectOwner(request.getProjectOwner());
		project.setClient(request.getClient());
		project.setBusinessUnit(request.getBusinessUnit());
		project.setProgram(request.getProgram());
		return project;
	}

	@Override
	public Project getProject(ObjectId id) {
		
		return projectRepository.findOne(id);
	}

}
