package com.capitalone.dashboard.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.Permission;
import com.capitalone.dashboard.request.PermissionRequest;
import com.capitalone.dashboard.service.PermissionService;
@RestController
public class EngineeringDashboardPermissionsController {
	
	private final PermissionService permissionService;
	@Autowired
	public EngineeringDashboardPermissionsController(PermissionService permissionService) {
		this.permissionService = permissionService;
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/engineeringDashboardPermission")
	public ResponseEntity<Permission> createEngineeringDashboardPermission(@RequestBody PermissionRequest permissionRequest){
		Permission response=permissionService.createPermission(permissionRequest);
		if(response!=null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/engineeringDashboardPermissionMultiple")
	public ResponseEntity<Iterable<Permission>> createMultipleEngineeringDashboardPermission(@RequestBody Iterable<PermissionRequest> permissionRequest){
		Iterable<Permission> response=permissionService.createPermissions(permissionRequest);
		if(response!=null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

		}
	}
	

	@RequestMapping(method=RequestMethod.GET,value="/allActiveEngineeringDashboardPermissions")
	public Iterable<Permission> getAllActiveEngineeringDashboardPermission(){
		return permissionService.getAllActivePermissions();
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/getEngineeringDashboardPermissionWithName")
	public Permission getEngineeringDashboardPermissionWithName(@RequestParam(name="name") String name){
		return permissionService.getPermissionWithName(name);
	}

	@RequestMapping(method=RequestMethod.GET,value="/allInActiveEngineeringDashboardPermissions")
	public Iterable<Permission> getAllInActiveEngineeringDashboardPermission(){
		return permissionService.getAllInactivePermissions();
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/engineeringDashboardPermissionUpdate")
	public ResponseEntity<Permission> updateEngineeringDashboardPermission(@RequestBody PermissionRequest permissionRequest){
		Permission response= permissionService.updatePermission(permissionRequest);
		if(response!=null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else{
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/deactivateEngineeringDashboardPermission")
	public ResponseEntity<Permission> deactivateEngineeringDashboardPermission(@RequestParam(name="name") String name){
		Permission response=  permissionService.deactivatePermission(name);
		if(response!=null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else{
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/reactivateEngineeringDashboardPermission")
	public ResponseEntity<Permission> reactivateEngineeringDashboardPermission(@RequestParam(name="name") String name){
		Permission response=  permissionService.reactivatePermission(name);
		if(response!=null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else{
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

		}
	}
	
	

}
