package com.capitalone.dashboard.service;

import com.capitalone.dashboard.model.UserRole;
import com.capitalone.dashboard.request.UserRoleRequest;

public interface UserRoleService {
	public UserRole createUserRole(UserRoleRequest role);
	public Iterable<UserRole> getAllActiveUserRoles();
	public Iterable<UserRole> getAllInactiveUserRoles();
	public Iterable<UserRole> createBulkUserRole(Iterable<UserRoleRequest> roles);
	public UserRole deactivateRole(String key);
	public UserRole reactivateRole(String key);
	public UserRole editUserRole(UserRoleRequest role);
}
