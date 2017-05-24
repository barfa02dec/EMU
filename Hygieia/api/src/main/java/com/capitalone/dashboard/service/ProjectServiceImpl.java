package com.capitalone.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.model.Project;
import com.capitalone.dashboard.repository.ProjectRepository;

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
	public String create(String projectName) {

		Project project = new Project();
		project.setProjectName(projectName);
		try {

			projectRepository.save(project);
			return "Project is created";
		} catch (DuplicateKeyException e) {
			return "Project already exists";
		}

	}

	@Override
	public void delete(String projectName) {

		Project project = projectRepository.findByProjectName(projectName);
		if (project != null) {
			projectRepository.delete(project);
		}

	}

}
