package com.capitalone.dashboard.rest;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.Sprint;
import com.capitalone.dashboard.service.SprintService;

@RestController
public class SprintController {
	
	private final SprintService sprintService;
	
	@Autowired
	public SprintController(SprintService sprintService) {
		this.sprintService = sprintService;
	}



	@RequestMapping(value = "/listAllSprints", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Sprint> getAllDefects(@RequestParam(value = "projectId", required = true) String projectId, @RequestParam(value = "projectName", required = true) String projectName) {
		List<Sprint> sprintList=sprintService.getAllSprints(projectId,projectName);
		Collections.sort(sprintList);
		sprintList.stream().limit(8);
		return sprintList;
	}
	
	@RequestMapping(value = "/sprintDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Sprint getAllDefects(@RequestParam(value = "sid", required = true) Long sid,@RequestParam(value = "projectId", required = true) String projectId) {
		return sprintService.getDetailedSprintDetails(sid,projectId);
	}

}
