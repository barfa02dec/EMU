package com.capitalone.dashboard.service;

import java.util.List;

import com.capitalone.dashboard.model.DefectAggregation;
import com.capitalone.dashboard.request.DefectSummaryRequest;

public interface DefectSummaryService {

	Iterable<DefectAggregation> getAllDefectDetails();

	DefectAggregation findByProjectId(String id, String projectName);
	
	List<DefectAggregation> findByMetricsProjectId(String metricsProjectId);
	
	DefectAggregation create(DefectSummaryRequest request);
	
	DefectAggregation update(DefectSummaryRequest request);
}
