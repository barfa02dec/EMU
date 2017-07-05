package com.capitalone.dashboard.service;

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
	public Iterable<Sprint> getAllSprints(String projectId) {
		return repository.findByProjectId(projectId);
	}

	@Override
	public Sprint getDetailedSprintDetails(Long sprintId) {
		return repository.findBySprintId(sprintId);
	}
	
}
