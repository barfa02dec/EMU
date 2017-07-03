package com.capitalone.dashboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.model.Permission;
import com.capitalone.dashboard.repository.PermissionsRepository;
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
	public List<Permission> createPermissions(List<Permission> permisssions) {
		// TODO Auto-generated method stub
		return (List<Permission>) permissionsRepository.save(permisssions);
	}

	@Override
	public void removePermissionsList(List<Permission> permisssions) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePermissionWithCollector(String collector) {
		// TODO Auto-generated method stub

	}

}
