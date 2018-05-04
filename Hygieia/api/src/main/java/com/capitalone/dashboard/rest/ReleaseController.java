package com.capitalone.dashboard.rest;

import java.util.ArrayList;
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
	public List<Release> getReleases(
			@RequestParam(name="projectId") String projectId, 
			@RequestParam(value = "noOfReleaseToShow", required = true) int noOfReleaseToShow){
		
		List <Release> releasesfromDB = (List<Release>) releaseService.getReleases(projectId);
		Collections.sort(releasesfromDB);

		List<Release> releases = new ArrayList<Release>();
		releasesfromDB.stream().limit(noOfReleaseToShow).forEach(release -> releases.add(release));
		
		return releases;
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/releases/details")
	public Release getReleaseDetail(@RequestParam(name="releaseId") Long releaseId,
			@RequestParam(name="projectId") String projectId)
	{
		return releaseService.getReleaseDetails(projectId, releaseId);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/releases")
	public Release createReleaseMetrics(@RequestBody ReleaseMetricsRequest req){
		return releaseService.create(req);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/releases")
	public Release updateReleaseMetrics(@RequestBody ReleaseMetricsRequest req){
		return releaseService.update(req);
	}
}
