package com.capitalone.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.model.DefectAggregation;
import com.capitalone.dashboard.repository.DefectAggregationRepository;
@Service
public class DefectSummeryServiceImpl implements DefectSummeryService {
	
	private final DefectAggregationRepository aggregationRepository;

	
	@Autowired
	public DefectSummeryServiceImpl(DefectAggregationRepository aggregationRepository) {
		this.aggregationRepository = aggregationRepository;
	}



	@Override
	public Iterable<DefectAggregation> getAllDefectDetails() {
			return aggregationRepository.findAll();
	}



	@Override
	public DefectAggregation findByProjectId(String id, String projectName) {
		return aggregationRepository.findByProjectIdAndName(id,projectName);
	}
	


}
