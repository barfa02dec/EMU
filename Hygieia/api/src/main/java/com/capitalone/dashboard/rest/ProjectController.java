package com.capitalone.dashboard.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.Authentication;
import com.capitalone.dashboard.model.Customer;
import com.capitalone.dashboard.model.Project;
import com.capitalone.dashboard.model.UserGroup;
import com.capitalone.dashboard.model.UserRole;
import com.capitalone.dashboard.request.CustomerRequest;
import com.capitalone.dashboard.request.ProjectRequest;
import com.capitalone.dashboard.request.ProjectUserRoleRequest;
import com.capitalone.dashboard.service.AuthenticationService;
import com.capitalone.dashboard.service.ProjectService;

@RestController
public class ProjectController {

	private final ProjectService projectService;
	private final AuthenticationService authService;
	
	@Autowired
	public ProjectController(ProjectService projectService,AuthenticationService authService) {
		this.projectService = projectService;
		this.authService=authService;
	}

	@RequestMapping(value = "/projects", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	public Iterable<Project> getProjects() {
		Iterable<Project> projects = projectService.getActiveProjects();
		return projects;
	}

	@RequestMapping(value = "/projects", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createProject(@Valid @RequestBody ProjectRequest request) {
		try{
			Project project = projectService.create(request);
			
			if(project != null){
				return ResponseEntity.status(HttpStatus.CREATED).body("project created successfully with ID::"+project.getId());
			}else{
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("project creation failed"); 
			}
		}catch (org.springframework.dao.DuplicateKeyException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("project already Exists with Project ID::"+request.getProjectId());
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("project creation failed"); 
		}
	}

	@RequestMapping(value = "/projects", method = RequestMethod.PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateProject(@Valid @RequestBody ProjectRequest request) {
		try{
			Project project = projectService.updateProject(request);
			
			if(project != null){
				return ResponseEntity.status(HttpStatus.OK).body("project updated successfully");
			}else{
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("project does not Exists/Invalid Project ID");
			}
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("project creation failed"); 
		}
	}

	@RequestMapping(value = "/projects/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteProject(@PathVariable String id) {
		try{
			ObjectId projectUniqueId = new ObjectId(id);
			Project project = projectService.deactivateProject(projectUniqueId);
			
			if(null != project && !project.isProjectStatus()){
				return ResponseEntity.status(HttpStatus.OK).body("project with id::"+id+" is deactivated successfully");
			}else{
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("project not found");
			}
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("invalid project id");
		}
	}

	@RequestMapping(value = "/disassociatedUserFromProject/{user}/{projectId}", method = DELETE)
	public ResponseEntity<String> disassociatedUserFromProject(@PathVariable String user, @PathVariable String projectId) {
		try{

			return ResponseEntity.status(HttpStatus.OK).body(projectService.disassociatedUserFromProject(user, projectId));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Request for disassociating user from project is failed");
		}
	}

	@RequestMapping(value = "/getApplicationUsers/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	public List<String> getAllUsers(@PathVariable String id){
		List<String> dbAppUsers=projectService.getProject(new ObjectId(id)).getUsersGroup().stream().map(UserGroup::getUser).collect(Collectors.toList());
		List<Authentication> authUsers=(List<Authentication>) authService.all();
		List<String> appUsers=authUsers.stream().filter(auth->!dbAppUsers.contains(auth.getUsername())).map(Authentication::getUsername).collect(Collectors.toList());
		return appUsers;
	}


	@RequestMapping(value = "/getProjectsByUser", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	public Iterable<Project> getProjectsOwnedByUser(@RequestParam String username) {
		return projectService.getProjectsOwnedByUser(username);
	}

	@RequestMapping(value = "/projectUsersMapping", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Project> createProjectUserMappingWithRoles(@Valid @RequestBody ProjectUserRoleRequest projectUserRoleRequest) {
		try{
			Project response = projectService.createProjectUserRoleMapping(projectUserRoleRequest);
			if(null!=response){
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}else{
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
			}

		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); 
		}
	}

	@RequestMapping(value = "/getProject/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Project> getProject(@PathVariable String id) {
		try{
			ObjectId projectUniqueId=new ObjectId(id);
			Project project=projectService.getProject(projectUniqueId);
			return ResponseEntity.status(HttpStatus.OK).body(project);

		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); 
		}
	}

	@RequestMapping(value = "/getProjectRoles/{projectId}/{user}", method = GET, produces = APPLICATION_JSON_VALUE)
	public Iterable<UserRole> getAllActiveProjectRolesOfUser(@PathVariable String projectId , @PathVariable String user){
		try{
			return projectService.getActiveprojectRolesOfUser(projectId, user);
		}catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/createGlobalDeliveryUser", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createGlobalDeliveryUser(@RequestParam String username) {
		return ResponseEntity.status(HttpStatus.OK).body(projectService.createGlobalDeliveryUser(username));
	}

	
	@RequestMapping(value = "/createGlobalDeliverySysAdmin", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createGlobalDeliverySysAdmin(@RequestParam String username) {
		return ResponseEntity.status(HttpStatus.OK).body(projectService.createAdditionalSysAdmins(username));
	}

	@RequestMapping(value = "/purgeAppUser", method = DELETE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<String> purgeUser(@RequestParam String username) {
		//purge the user from auth
		authService.delete(username);
		//purge the user from projects && purge the user from dashboards
		return ResponseEntity.status(HttpStatus.OK).body(projectService.purgeUser(username));
	}

	@RequestMapping(value = "/RevokeAppUserAccess", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<String> revokeAppUserAccess(@RequestParam String username) {
		//purge the user from projects && purge the user from dashboards
		return ResponseEntity.status(HttpStatus.OK).body(projectService.purgeUser(username));
	}

	/**
	 * create customer by passing payload
	 * @param customerRequest
	 * @param response
	 */
	@RequestMapping(value = "/customers", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createCustomer(@RequestBody CustomerRequest customerRequest, HttpServletResponse response) {
		projectService.createCustomers(customerRequest);
	}
	
	/**
	 * get All Customers
	 * @param customerName
	 * @return
	 */
	/*@RequestMapping(value = "/getCustomer", method = RequestMethod.GET, consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE)
	public @ResponseBody List<Customer> getCustomers(
			@RequestParam(value = "customerName", required = true) String customerName) {

		List<Customer> customerList = projectService.getCustomer(customerName);
		return customerList;
	}*/
	
	@RequestMapping(value = "/customers", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	public @ResponseBody List<Customer> getCustomers()  {
		List<Customer> customerList = projectService.getCustomer();
		return customerList;
	}
}
