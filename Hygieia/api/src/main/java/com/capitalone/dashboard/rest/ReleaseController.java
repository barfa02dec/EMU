package com.capitalone.dashboard.rest;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.Release;
import com.capitalone.dashboard.request.ReleaseMetricsRequest;
import com.capitalone.dashboard.service.ReleaseService;
@RestController
public class ReleaseController {
	
	private final ReleaseService releaseService;
	@Autowired
	public ReleaseController(ReleaseService releaseService) {
		this.releaseService = releaseService;
	}
	@RequestMapping(method=RequestMethod.GET,value="/releases")
	public List<Release> getAllReleasesForProject(@RequestParam(name="projectId") String projectId, @RequestParam(name="projectName") String projectName){
		List <Release> releaseList=(List<Release>) releaseService.getAllReleases(projectId, projectName);
		Collections.sort(releaseList);
		releaseList.stream().limit(8);
		return releaseList;
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/releases/details")
	public Release getReleaseDetailsWithID(@RequestParam(name="releaseId") Long releaseId,@RequestParam(name="projectId") String projectId)
	{
		return releaseService.getDetailedReleaseDetails(releaseId, projectId);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/releases")
	public Release createReleaseMetrics(@RequestBody ReleaseMetricsRequest req){
		return releaseService.create(req);
	}
}
