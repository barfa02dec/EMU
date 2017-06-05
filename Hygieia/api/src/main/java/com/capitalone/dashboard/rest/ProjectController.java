package com.capitalone.dashboard.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
				return ResponseEntity.status(HttpStatus.OK).body("project created successfully with ID::"+project.getId());
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
	public ResponseEntity<String> deleteProject(@PathVariable ObjectId id) {
		try{
			Project project=projectService.deactivateProject(id);
			if(null!=project && !project.isProjectActiveStatus()){
				return ResponseEntity.status(HttpStatus.OK).body("Project deactivated successfully");
			}else{
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Project details not found");
			}
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Project deactivation Failed");
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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("project created failed"); 

		}
		
	}

	@RequestMapping(value = "/getProjects", method = GET, produces = APPLICATION_JSON_VALUE)
	public Iterable<Project> getProjects() {
		Iterable<Project> projects = projectService.all();
		return projects;

	}

}
