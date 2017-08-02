package com.capitalone.dashboard.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.UserRole;
import com.capitalone.dashboard.request.UserRoleRequest;
import com.capitalone.dashboard.service.UserRoleService;

@RestController
public class EngineeringDashboardRolesController {
	
	private final UserRoleService userRoleService;
	
	@Autowired
	public EngineeringDashboardRolesController(UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}

	@RequestMapping(method=RequestMethod.POST,value="/engineeringDashboardUserRole")
	public ResponseEntity<UserRole> createEngineeringDashboardRole(@RequestBody UserRoleRequest roleRequest){
		UserRole response=userRoleService.createUserRole(roleRequest);
		if(response!=null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/engineeringDashboardUserRoleEdit")
	public ResponseEntity<UserRole> editEngineeringDashboardRole(@RequestBody UserRoleRequest roleRequest){
		UserRole response=userRoleService.editUserRole(roleRequest);
		if(response!=null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/engineeringDashboardBulkUserRole")
	public ResponseEntity<Iterable<UserRole>> createMultipleEngineeringDashboardRoles(@RequestBody Iterable<UserRoleRequest> roleRequest){
		Iterable<UserRole> response=userRoleService.createBulkUserRole(roleRequest);
		if(response!=null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

		}
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/allActiveEngineeringDashboardUserRolesRoles")
	public Iterable<UserRole> getAllActiveEngineeringDashboardRole(){
		return userRoleService.getAllActiveUserRoles();
	}
	

	@RequestMapping(method=RequestMethod.GET,value="/allInActiveEngineeringDashboardUserRolesRoles")
	public Iterable<UserRole> getAllInActiveEngineeringDashboardRole(){
		return userRoleService.getAllInactiveUserRoles();
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/deactivateEngineeringDashboardUserRoles")
	public ResponseEntity<UserRole> deactivateEngineeringDashboardRole(@RequestParam(name="key") String key){
		UserRole response=  userRoleService.deactivateRole(key);
		if(response!=null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else{
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/reactivateEngineeringDashboardUserRoles")
	public ResponseEntity<UserRole> reactivateEngineeringDashboardRole(@RequestParam(name="key") String key){
		UserRole response=  userRoleService.reactivateRole(key);
		if(response!=null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else{
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

		}
	}
	

}
