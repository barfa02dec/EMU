package com.capitalone.dashboard.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.DefectAggregation;
import com.capitalone.dashboard.request.DefectSummaryRequest;
import com.capitalone.dashboard.service.DefectSummaryService;
@RestController
public class DefectSummaryController {
	
	private final DefectSummaryService defectSummaryService;
	@Autowired
	public DefectSummaryController(DefectSummaryService defectSummaryService) {
		this.defectSummaryService = defectSummaryService;
	}
	
	@RequestMapping(value="/defectSummary", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Iterable<DefectAggregation> getDefectSummary(){
		return defectSummaryService.getAllDefectDetails();
	}
	
	@RequestMapping(value="/defectSummary/{projectId}/{projectName}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public DefectAggregation getDefectSummaryByProjectId(@PathVariable("projectId") String projectId, @PathVariable("projectName") String projectName){
		return defectSummaryService.findByProjectId(projectId, projectName);
	}

	@RequestMapping(value="/defectSummary/{metricsProjectId}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public DefectAggregation getDefectSummaryByMetricsProjectId(@PathVariable("metricsProjectId") String metricsProjectId){
		List<DefectAggregation> summaryrecords = defectSummaryService.findByMetricsProjectId(metricsProjectId);
		return CollectionUtils.isEmpty(summaryrecords) ? null : summaryrecords.get(summaryrecords.size()-1);
	}
	
	@RequestMapping(value="/defectSummary", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public DefectAggregation createDefectAggregationMetrics(@Valid @RequestBody DefectSummaryRequest defectSummary){
		return defectSummaryService.create(defectSummary);
	}

	@RequestMapping(value="/defectSummary", method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_VALUE)
	public DefectAggregation updateDefectAggregationMetrics(@Valid @RequestBody DefectSummaryRequest defectSummary){
		return defectSummaryService.update(defectSummary);
	}
}
