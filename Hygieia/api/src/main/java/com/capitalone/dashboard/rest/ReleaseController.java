package com.capitalone.dashboard.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.Release;
import com.capitalone.dashboard.service.ReleaseService;
@RestController
public class ReleaseController {
	
	private final ReleaseService releaseService;
	@Autowired
	public ReleaseController(ReleaseService releaseService) {
		this.releaseService = releaseService;
	}
	@RequestMapping(method=RequestMethod.GET,value="/projectReleaseList")
	public Iterable<Release> getAllReleasesForProject(@RequestParam(name="projectId") String projectId){
		return releaseService.getAllReleases(projectId);
	}
	@RequestMapping(method=RequestMethod.GET,value="/releaseDetails")
	public Release getReleaseDetailsWithID(@RequestParam(name="releaseId") Long releaseId)
	{
		return releaseService.getDetailedReleaseDetails(releaseId);
	}
	
	
}
