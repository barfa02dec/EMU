package com.capitalone.dashboard.service;

import com.capitalone.dashboard.model.Permission;
import com.capitalone.dashboard.request.PermissionRequest;

public interface PermissionService {
	public Permission createPermission(PermissionRequest permisssions);
	public Iterable<Permission> createPermissions(Iterable<PermissionRequest> permisssions);
	public Permission deactivatePermission(String name);
	public Permission reactivatePermission(String name);
	public Iterable<Permission> getAllActivePermissions();
	public Iterable<Permission> getAllInactivePermissions();
	public Permission getPermissionWithName(String name);
	public Permission updatePermission(PermissionRequest permisssions);
}
