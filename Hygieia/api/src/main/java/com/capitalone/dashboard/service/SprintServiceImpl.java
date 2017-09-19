package com.capitalone.dashboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.capitalone.dashboard.model.Sprint;
import com.capitalone.dashboard.repository.SprintRepository;
@Service
public class SprintServiceImpl implements SprintService {

	private final SprintRepository repository;
	
	@Autowired
	public SprintServiceImpl(SprintRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Sprint> getAllSprints(String projectId) {
		return (List<Sprint>) repository.findByProjectId(projectId);
	}

	@Override
	public Sprint getDetailedSprintDetails(Long sprintId,String projectId) {
		return repository.findBySprintId(sprintId,projectId);
	}
	
}
