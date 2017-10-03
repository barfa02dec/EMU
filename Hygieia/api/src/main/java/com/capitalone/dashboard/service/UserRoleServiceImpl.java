package com.capitalone.dashboard.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.model.Permission;
import com.capitalone.dashboard.model.Project;
import com.capitalone.dashboard.model.ProjectRoles;
import com.capitalone.dashboard.model.UserGroup;
import com.capitalone.dashboard.model.UserRole;
import com.capitalone.dashboard.repository.PermissionsRepository;
import com.capitalone.dashboard.repository.ProjectRepository;
import com.capitalone.dashboard.repository.UserRoleRepository;
import com.capitalone.dashboard.request.UserRoleRequest;
@Service
public class UserRoleServiceImpl implements UserRoleService {
	
	private final UserRoleRepository roleRepository;
	private final PermissionsRepository permissionsRepository;
	private final ProjectRepository projectRepository;
	@Autowired
	public UserRoleServiceImpl(UserRoleRepository roleRepository,PermissionsRepository permissionsRepository,ProjectRepository projectRepository) {
		this.roleRepository = roleRepository;
		this.permissionsRepository=permissionsRepository;
		this.projectRepository=projectRepository;
	}
	
	@Override
	public UserRole createUserRole(UserRoleRequest role) {
		// TODO Auto-generated method stub
		return roleRepository.save(mapUserRoleRequestToUserRoleModel(role));
	}
	
	private UserRole mapUserRoleRequestToUserRoleModel(UserRoleRequest role){
		UserRole userRole=new UserRole();
		userRole.setRoleKey(role.getRoleKey());
		userRole.setCreatedBy(role.getCreatedBy());
		userRole.setCreatedOn(new Date().toString());
		userRole.setEnabled(role.isEnabled());
		userRole.setDescription(role.getDescription());
		userRole.setExposetoApi(role.isExposetoApi());
		Map<String , Boolean> permissions= new HashMap<String , Boolean>();
		List<String> permissionKeysInDB=getpermissionKeysInDB();
		for(String permit:role.getPermissions()){
			if(permissionKeysInDB.contains(permit))
				{
					permissions.put(permit, Boolean.TRUE);
				}
		}
		userRole.setPermissions(permissions);
		return userRole;
	}
	private Iterable<UserRole> mapMultipleUserRoleRequestToUserRoleModel(Iterable<UserRoleRequest> roles){
		Set <UserRole> roleset = new HashSet<UserRole>();
		for(UserRoleRequest role:roles)
		{
			UserRole userRole=new UserRole();
			userRole.setRoleKey(role.getRoleKey());
			userRole.setCreatedBy(role.getCreatedBy());
			userRole.setCreatedOn(new Date().toString());
			userRole.setEnabled(role.isEnabled());
			userRole.setExposetoApi(role.isExposetoApi());
			Map<String , Boolean> permissions= new HashMap<String , Boolean>();
			List<String> permissionKeysInDB=getpermissionKeysInDB();

			for(String permit:role.getPermissions()){
				if(permissionKeysInDB.contains(permit))
				{
					permissions.put(permit, Boolean.TRUE);
				}
			}
			userRole.setPermissions(permissions);
			roleset.add(userRole);
		}
		return roleset;
	}
	@Override
	public Iterable<UserRole> getAllActiveUserRoles() {
		//param1: active/not param2: exposetoAPI/not
		return roleRepository.findByStatus(true,true);
	}

	@Override
	public Iterable<UserRole> getAllInactiveUserRoles() {
		//param1: active/not param2: exposetoAPI/not
		return roleRepository.findByStatus(false,true);
	}

	@Override
	public Iterable<UserRole> createBulkUserRole(Iterable<UserRoleRequest> roles) {
		return roleRepository.save(mapMultipleUserRoleRequestToUserRoleModel(roles));
	}

	@Override
	public UserRole deactivateRole(String key) {
		UserRole roleInDb=roleRepository.findByRoleKey(key);
		if(null!=roleInDb){
			roleInDb.setEnabled(false);
			
			List<Project> dbActiveProjects=(List<Project>) projectRepository.getAllActiveProjects(true);
			//remove the role from all associated project roles
			for(Project proj: dbActiveProjects){
				
					for(UserGroup group:proj.getUsersGroup()){
						group.getUserRoles().remove(mapUserRoleToProjectRole(roleInDb));
					}
				
			}
			
			projectRepository.save(dbActiveProjects);
		
			return roleRepository.save(roleInDb);
			
			}
		return null;
	}

	@Override
	public UserRole reactivateRole(String key) {
		UserRole roleInDb=roleRepository.findByRoleKey(key);
		if(null!=roleInDb){
			roleInDb.setEnabled(true);
			return roleRepository.save(roleInDb);
		}
		return null;
	}

	@Override
	public UserRole editUserRole(UserRoleRequest role) {
		UserRole roleInDb=roleRepository.findByRoleKey(role.getRoleKey());
		if(null!=roleInDb){
			Map<String , Boolean> permissions= new HashMap<String , Boolean>();
			List<String> permissionKeysInDB=getpermissionKeysInDB();
			
			for(String permit:role.getPermissions()){
				if(permissionKeysInDB.contains(permit)){
					permissions.put(permit, Boolean.TRUE);
				}
			}
			roleInDb.setPermissions(permissions);
			roleInDb.setDescription(role.getDescription());
			roleInDb.setUpdatedBy(role.getUpdatedBy());
			roleInDb.setLastUpdatedOn(new Date().toString());
			
			List<Project> dbActiveProjects=(List<Project>) projectRepository.getAllActiveProjects(true);
			//update the roles for all associated project roles
			for(Project proj: dbActiveProjects){
				
					for(UserGroup group:proj.getUsersGroup()){
						for(ProjectRoles projRole:group.getUserRoles()){
								if(projRole.getRole().equals(roleInDb.getRoleKey())){
									projRole.setPermissions(roleInDb.getPermissions().keySet());;
								}
							}
					}
				
			}
			projectRepository.save(dbActiveProjects);
		}
		
		return roleRepository.save(roleInDb);
	}
	
	private List<String> getpermissionKeysInDB(){
		List<Permission> activePermissionsInDB=permissionsRepository.findByStatus(true);
		List<String> permissionKeysInDB= new ArrayList<String>();
		activePermissionsInDB.forEach(key->permissionKeysInDB.add(key.getName()));
		return permissionKeysInDB;
	}
	
	private ProjectRoles mapUserRoleToProjectRole(UserRole roleInDb){
		ProjectRoles projectRoles= new ProjectRoles();
		projectRoles.setRole(roleInDb.getRoleKey());
		projectRoles.setPermissions(roleInDb.getPermissions().keySet());
		return projectRoles;
	}
}
