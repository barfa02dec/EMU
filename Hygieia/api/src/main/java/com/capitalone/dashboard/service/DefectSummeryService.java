package com.capitalone.dashboard.service;

import com.capitalone.dashboard.model.DefectAggregation;
import com.capitalone.dashboard.request.DefectSummaryRequest;

public interface DefectSummeryService {

	

	Iterable<DefectAggregation> getAllDefectDetails();

	DefectAggregation findByProjectId(String id, String projectName);
	
	DefectAggregation findByMetricsProjectId(String metricsProjectId);
	
	DefectAggregation create(DefectSummaryRequest request);
	
}
