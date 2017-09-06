package com.capitalone.dashboard.service;

import com.capitalone.dashboard.model.Sprint;

public interface SprintService {

	Iterable<Sprint> getAllSprints(String projectId);

	Sprint getDetailedSprintDetails(Long sprintId,String projectId);

}
