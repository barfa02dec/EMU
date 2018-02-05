package com.capitalone.dashboard.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.capitalone.dashboard.model.Defect;

public interface DefectService {

	List<Defect> getDefects(ObjectId collectorId, String defectId);

	List<Defect> getDefects();

	List<Defect> getDefectByStatus(String defectStatus);

	List<Defect> defectBySeverity(String defectSeverity);

	List<Defect> defectByAge(Integer defectAge);
}
