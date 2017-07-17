package com.capitalone.dashboard.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.Permission;

public interface PermissionsRepository extends CrudRepository<Permission, ObjectId>,
QueryDslPredicateExecutor<Permission>, PermissionsRepositoryCustom {
	
	@Query(value = "{'name' : ?0}")
	Permission findByName(String name);
	
	@Query(value = "{'status' : ?0}")
	List<Permission> findByStatus(boolean status);
	
	

}
