package com.capitalone.dashboard.rest;

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
	public Iterable<Sprint> getAllDefects(@RequestParam(value = "projectId", required = true) String projectId) {
		return sprintService.getAllSprints(projectId);
	}
	
	@RequestMapping(value = "/sprintDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Sprint getAllDefects(@RequestParam(value = "sid", required = true) Long sid) {
		return sprintService.getDetailedSprintDetails(sid);
	}

}
