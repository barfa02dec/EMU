package com.capitalone.dashboard.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.DefectAggregation;
import com.capitalone.dashboard.request.DefectSummaryRequest;
import com.capitalone.dashboard.service.DefectSummeryService;
@RestController
public class DefectSummaryController {
	
	private final DefectSummeryService defectSummeryService;
	@Autowired
	public DefectSummaryController(DefectSummeryService defectSummeryService) {
		this.defectSummeryService = defectSummeryService;
	}
	
	@RequestMapping(value="/defectSummary", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Iterable<DefectAggregation> getDefectSummery(){
		return defectSummeryService.getAllDefectDetails();
	}
	
	@RequestMapping(value="/defectSummary/{projectId}/{projectName}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public DefectAggregation getDefectSummeryByProjectId(@PathVariable("projectId") String projectId, @PathVariable("projectName") String projectName){
		return defectSummeryService.findByProjectId(projectId, projectName);
	}
	
	
	@RequestMapping(value="/defectSummary/{metricsProjectId}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public DefectAggregation getDefectSummeryByMetricsProjectId(@PathVariable("metricsProjectId") String metricsProjectId){
		return defectSummeryService.findByMetricsProjectId(metricsProjectId);
	}
	
	@RequestMapping(value="/defectSummary", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public DefectAggregation createDefectDefectAggregationMetrics(@Valid @RequestBody DefectSummaryRequest defectSummery){
		return defectSummeryService.create(defectSummery);
	}
}
