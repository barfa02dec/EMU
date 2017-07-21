package com.capitalone.dashboard.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.model.UserRole;
import com.capitalone.dashboard.repository.UserRoleRepository;
import com.capitalone.dashboard.request.UserRoleRequest;
@Service
public class UserRoleServiceImpl implements UserRoleService {
	
	private final UserRoleRepository roleRepository;
	
	@Autowired
	public UserRoleServiceImpl(UserRoleRepository roleRepository) {
		this.roleRepository = roleRepository;
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
		userRole.setPermissions(role.getPermissions());
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
			userRole.setPermissions(role.getPermissions());
			roleset.add(userRole);
		}
		return roleset;
	}
	@Override
	public Iterable<UserRole> getAllActiveUserRoles() {
		return roleRepository.findByStatus(true);
	}

	@Override
	public Iterable<UserRole> getAllInactiveUserRoles() {
		return roleRepository.findByStatus(false);
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
			roleRepository.save(roleInDb);
			return roleInDb;
			}
		return null;
	}

	@Override
	public UserRole reactivateRole(String key) {
		UserRole roleInDb=roleRepository.findByRoleKey(key);
		if(null!=roleInDb){
			roleInDb.setEnabled(true);
			roleRepository.save(roleInDb);
			return roleInDb;
		}
		return null;
	}

}