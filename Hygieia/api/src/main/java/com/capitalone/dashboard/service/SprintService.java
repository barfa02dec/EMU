package com.capitalone.dashboard.service;

import java.util.List;

import com.capitalone.dashboard.model.Sprint;

public interface SprintService {

	List<Sprint> getAllSprints(String projectId);

	Sprint getDetailedSprintDetails(Long sprintId,String projectId);

}
