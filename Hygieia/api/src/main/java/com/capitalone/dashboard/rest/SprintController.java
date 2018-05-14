package com.capitalone.dashboard.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.Sprint;
import com.capitalone.dashboard.request.SprintMetricsRequest;
import com.capitalone.dashboard.service.SprintService;

@RestController
public class SprintController {
	
	private final SprintService sprintService;
	
	@Autowired
	public SprintController(SprintService sprintService) {
		this.sprintService = sprintService;
	}

	@RequestMapping(value = "/sprints", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Sprint> getSprints(
			@RequestParam(value = "projectId", required = true) String projectId,
			@RequestParam(value = "noOfSprintToShow", required = true) int noOfsprintsToShow) {

		List<Sprint> sprintsInDB = sprintService.getSprints(projectId);
		Collections.sort(sprintsInDB);

		List<Sprint> sprints = new ArrayList<Sprint>();
		sprintsInDB.stream().limit(noOfsprintsToShow)
				 .forEach(sprint -> sprints.add(sprint));
		
		return sprints;
	}
	
	@RequestMapping(value = "/sprints/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Sprint getSprintDetails(@RequestParam(value = "sid", required = true) Long sprintId, 
			@RequestParam(value = "projectId", required = true) String projectId) {
		return sprintService.getSprintDetails(projectId, sprintId);
	}
	
	@RequestMapping(value = "/sprints", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createSprintMetrics(@RequestBody SprintMetricsRequest re){
		try{
			sprintService.create(re);
			return ResponseEntity.status(HttpStatus.CREATED).body("sprint successfully created");
		}catch (org.springframework.dao.DuplicateKeyException e) {	
			return ResponseEntity.status(HttpStatus.CONFLICT).body("sprint already exists for the project id "+re.getProjectId() + " and sprint id " + re.getSprintId());
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("sprint create failed"); 
		}
	}
	
	@RequestMapping(value = "/sprints", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateSprintMetrics(@RequestBody SprintMetricsRequest re){
		try{
			sprintService.update(re);
			return ResponseEntity.status(HttpStatus.OK).body("sprint successfully updated");
		}catch (org.springframework.dao.DuplicateKeyException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("sprint already exists for the project id "+re.getProjectId() + " and sprint id " + re.getSprintId());
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("sprint update failed"); 
		}
	}
}
