package com.capitalone.dashboard.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.request.ProjectRequest;
import com.capitalone.dashboard.service.ProjectService;

@RestController
public class ProjectController {
	
	private final ProjectService projectService;


    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    
	@RequestMapping(value = "/createProject", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@Valid @RequestBody ProjectRequest request) {
        // TODO: should return proper HTTP codes for existing users
        return ResponseEntity.status(HttpStatus.OK).body(projectService.create(request.getProjectName()));
    }

}
