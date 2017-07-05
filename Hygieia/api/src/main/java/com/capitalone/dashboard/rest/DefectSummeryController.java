package com.capitalone.dashboard.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.DefectAggregation;
import com.capitalone.dashboard.service.DefectSummeryService;
@RestController
public class DefectSummeryController {
	
	private final DefectSummeryService defectSummeryService;
	@Autowired
	public DefectSummeryController(DefectSummeryService defectSummeryService) {
		this.defectSummeryService = defectSummeryService;
	}
	
	@RequestMapping(value="/getDefectSummery", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Iterable<DefectAggregation> getDefectSummery(){
		return defectSummeryService.getAllDefectDetails();
	}
	
	@RequestMapping(value="/getDefectSummery/{projectId}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public DefectAggregation getDefectSummeryByProjectId(@PathVariable("projectId") String projectId){
		return defectSummeryService.findByProjectId(projectId);
	}
	
	

}
