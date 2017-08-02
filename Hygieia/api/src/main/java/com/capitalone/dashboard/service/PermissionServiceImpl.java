package com.capitalone.dashboard.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.model.Permission;
import com.capitalone.dashboard.repository.PermissionsRepository;
import com.capitalone.dashboard.request.PermissionRequest;
@Component
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	PermissionsRepository permissionsRepository;

	public PermissionsRepository getPermissionsRepository() {
		return permissionsRepository;
	}

	public void setPermissionsRepository(PermissionsRepository permissionsRepository) {
		this.permissionsRepository = permissionsRepository;
	}

	@Override
	public Permission createPermission(PermissionRequest permisssion) {
		return permissionsRepository.save(mapPermissionRequestToPermissionModelForCreatePermission(permisssion));
	
	}

	@Override
	public Iterable<Permission> createPermissions(Iterable<PermissionRequest> permisssions) {
		return permissionsRepository.save(mapPermissionRequestToPermissionModelForMultipleCreatePermission(permisssions));
	}
	
	@Override
	public Permission deactivatePermission(String name) {
		if(null!=name){
			Permission permissionInDB=permissionsRepository.findByName(name);
			if(null!=permissionInDB){
				permissionInDB.setStatus(false);
				return permissionsRepository.save(permissionInDB);
			}
			
		}
		return null;
		
	}
	
	@Override
	public Permission reactivatePermission(String name) {
		if(null!=name){
			Permission permissionInDB=permissionsRepository.findByName(name);
			if(null!=permissionInDB){
				permissionInDB.setStatus(true);
				return permissionsRepository.save(permissionInDB);
			}
			
		}
		return null;
		
	}

	@Override
	public Iterable<Permission> getAllActivePermissions() {
		return permissionsRepository.findByStatus(true);
	}

	@Override
	public Iterable<Permission> getAllInactivePermissions() {
		return permissionsRepository.findByStatus(false);
	}

	@Override
	public Permission getPermissionWithName(String name) {
		
		return permissionsRepository.findByName(name);
	}
	
	@Override
	public Permission updatePermission(PermissionRequest permisssionReq) {
		if(null!=permisssionReq.getName()){
			Permission permissionInDB=permissionsRepository.findByName(permisssionReq.getName());
			if(null!=permissionInDB){
				return permissionsRepository.save(mapPermissionRequestToPermissionModelForUpdatePermission(permisssionReq, permissionInDB));
			}
			
		}
		return null;
	}
	
	private Permission mapPermissionRequestToPermissionModelForCreatePermission(PermissionRequest request){
		Permission permission=new Permission();
		permission.setName(request.getName());
		permission.setCreatedBy(request.getCreatedBy());
		permission.setDescription(request.getDescription());
		permission.setCreatedOn(new Date().toString());
		permission.setStatus(request.isStatus());
		return permission;
	}
	
	private Set<Permission> mapPermissionRequestToPermissionModelForMultipleCreatePermission(Iterable<PermissionRequest> requestSet){
		Set<Permission> permissionSet= new HashSet<Permission>();
		for(PermissionRequest request: requestSet)
		{
			Permission permission=new Permission();
			permission.setName(request.getName());
			permission.setCreatedBy(request.getCreatedBy());
			permission.setDescription(request.getDescription());
			permission.setStatus(request.isStatus());
			permissionSet.add(permission);
		}
		return permissionSet;
	}
	
	private Permission mapPermissionRequestToPermissionModelForUpdatePermission(PermissionRequest request, Permission permission){
		permission.setUpdatedBy(request.getUpdatedBy());
		permission.setLastUpdatedOn(new Date().toString());
		permission.setDescription(request.getDescription());
		return permission;
	}

	

	

	
}
