package com.capitalone.dashboard.service;

import com.capitalone.dashboard.model.DefectAggregation;

public interface DefectSummeryService {

	

	Iterable<DefectAggregation> getAllDefectDetails();

	DefectAggregation findByProjectId(String id);
	

}
