package com.capitalone.dashboard.service;

import java.util.List;

import com.capitalone.dashboard.model.Sprint;
import com.capitalone.dashboard.request.SprintMetrcisRequest;

public interface SprintService {

	List<Sprint> getAllSprints(String projectId, String emuProjectId);

	Sprint getDetailedSprintDetails(Long sprintId,String projectId);
	
	Sprint create(SprintMetrcisRequest re);

}
