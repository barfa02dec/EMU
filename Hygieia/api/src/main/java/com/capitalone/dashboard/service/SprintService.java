package com.capitalone.dashboard.service;

import java.util.List;

import com.capitalone.dashboard.model.Sprint;
import com.capitalone.dashboard.request.SprintMetricsRequest;

public interface SprintService {

	List<Sprint> getSprints(String projectId);

	Sprint getSprintDetails(String projectId, Long sprintId);
	
	Sprint create(SprintMetricsRequest re);
	
	Sprint update(SprintMetricsRequest re);

}
