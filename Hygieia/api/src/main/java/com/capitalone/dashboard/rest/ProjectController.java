package com.capitalone.dashboard.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Set;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.Project;
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
	public ResponseEntity<String> createProject(@Valid @RequestBody ProjectRequest request) {
		try{
			Project project=projectService.create(request);
			if(project!=null){
				return ResponseEntity.status(HttpStatus.CREATED).body("project created successfully with ID::"+project.getId());
			}else{
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("project created failed"); 
			}
		}catch (org.springframework.dao.DuplicateKeyException e) {
			return ResponseEntity.status(HttpStatus.OK).body("project already Exists with Project ID::"+request.getProjectId()+" and Project Name::"+request.getProjectName());
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("project created failed"); 

		}
		
	}

	@RequestMapping(value = "/deleteProject/{id}", method = DELETE)
	public ResponseEntity<String> deleteProject(@PathVariable String id) {
		try{
			ObjectId projectUniqueId=new ObjectId(id);
			Project project=projectService.deactivateProject(projectUniqueId);
			if(null!=project && !project.isProjectStatus()){
				return ResponseEntity.status(HttpStatus.OK).body("Project deactivated successfully");
			}else{
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Project details not found");
			}
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid Project ID");
		}
	}
	
	@RequestMapping(value = "/updateProject", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateProject(@Valid @RequestBody ProjectRequest request) {
		try{
			Project project=projectService.updateProject(request);
			if(project!=null){
				return ResponseEntity.status(HttpStatus.OK).body("project updated successfully ");
			}else{
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("project does not Exists");

			}
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("project creation failed"); 

		}
		
	}

	@RequestMapping(value = "/getProjects", method = GET, produces = APPLICATION_JSON_VALUE)
	public Iterable<Project> getProjects() {
		Iterable<Project> projects = projectService.all();
		return projects;

	}
	
	@RequestMapping(value = "/getProjectsByUser", method = GET, produces = APPLICATION_JSON_VALUE)
	public Iterable<Project> getProjectsOwnedByUser(@RequestParam String username) {
		
		return projectService.getProjectsOwnedByUser(username);

	}
	
	@RequestMapping(value = "/projectUsers", method = POST, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<String>> createProjectUsers(@RequestParam String projectname, @RequestParam Set<String> users) {
		
		Project project=projectService.createUsers(projectname, users);
		if(project!=null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(project.getProjectUsersList());
		}else{
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

		}

	}
	
	@RequestMapping(value = "/getProject/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Project> getProject(@PathVariable String id) {
		try{
			ObjectId projectUniqueId=new ObjectId(id);
			Project project=projectService.getProject(projectUniqueId);
			return ResponseEntity.status(HttpStatus.OK).body(project);

		}catch (Exception e) {
			
		}
		
		return null;

	}

}
