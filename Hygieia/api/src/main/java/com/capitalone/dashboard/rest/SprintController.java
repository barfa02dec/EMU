package com.capitalone.dashboard.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.Sprint;
import com.capitalone.dashboard.request.SprintMetrcisRequest;
import com.capitalone.dashboard.service.SprintService;

@RestController
public class SprintController {
	
	private final SprintService sprintService;
	
	@Autowired
	public SprintController(SprintService sprintService) {
		this.sprintService = sprintService;
	}

	@RequestMapping(value = "/sprints", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Sprint> getAllDefects(
			@RequestParam(value = "projectId", required = true) String projectId,
			@RequestParam(value = "projectName", required = true) String projectName,
			@RequestParam(value = "noOfSprintToShow", required = true) int noOfsprintsToShow) {

		List<Sprint> sprintsInDB = sprintService.getSprints(projectId,
				projectName);
		Collections.sort(sprintsInDB);

		List<Sprint> sprints = new ArrayList<Sprint>();
		sprintsInDB.stream().limit(noOfsprintsToShow)
				 .forEach(sprint -> sprints.add(sprint));
		
		return sprints;
	}
	
	@RequestMapping(value = "/sprints/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Sprint getAllDefects(@RequestParam(value = "sid", required = true) Long sid,@RequestParam(value = "projectId", required = true) String projectId) {
		return sprintService.getSprintDetails(sid,projectId);
	}
	
	@RequestMapping(value = "/sprints", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Sprint createSprintMetrics(@RequestBody SprintMetrcisRequest re){
		return sprintService.create(re);
	}
	
}
