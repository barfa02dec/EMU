package com.capitalone.dashboard.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.capitalone.dashboard.model.Defect;
import com.capitalone.dashboard.repository.DefectRepository;

@Service
public class DefectServiceImpl implements DefectService {
	
	private final DefectRepository defectRepository;
	
	@Autowired
	public DefectServiceImpl(DefectRepository defectRepository) {
		this.defectRepository = defectRepository;
	}

	@Override
	public List<Defect> getDefects(ObjectId collectorId, String defectId) {
		List<Defect> defects = null;
		
		if ((StringUtils.isEmpty(collectorId) && StringUtils.isEmpty(defectId))) {
			throw new IllegalStateException("No records found");
		}
		else {
			defects = defectRepository.findById(collectorId);
		}
		return defects;
	}

	@Override
	public List<Defect> getDefects() {
		return (List<Defect>) defectRepository.findAll();
	}

	@Override
	public List<Defect> getDefectByStatus(String defectStatus) {
		List<Defect> defects = null;
		if((StringUtils.isEmpty(defectStatus))) {
			throw new IllegalStateException("No records found");
		}
		else {
			defects = defectRepository.findByStatus(defectStatus); 
		}
		return defects;
	}

	@Override
	public List<Defect> defectBySeverity(String defectSeverity) {
		List<Defect> defects = null;
		if((StringUtils.isEmpty(defectSeverity))){
			throw new IllegalStateException("No records found");
		}
		else
		{
			defects = defectRepository.findBySeverity(defectSeverity);
		}
		return defects;
	}

	@Override
	public List<Defect> defectByAge(Integer defectAge) {
		List<Defect> defects = null;
		if((StringUtils.isEmpty(defectAge))){
			throw new IllegalStateException("No records found");
		}
		else
		{
			defects = defectRepository.findByAge(defectAge);
		}
		return defects;
	}
}
