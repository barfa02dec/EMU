package com.capitalone.dashboard.service;

import java.util.List;

import com.capitalone.dashboard.model.Permission;

public interface PermissionService {

	List<Permission> createPermissions(List<Permission> permisssions);
	void removePermissionsList(List<Permission> permisssions);
	void removePermissionWithCollector(String collector);
	
	
}
