package com.capitalone.dashboard.repository;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.UserRole;

public interface UserRoleRepository extends CrudRepository<UserRole, ObjectId>,
QueryDslPredicateExecutor<UserRole>, UserRoleRepositoryCustom {
	
	@Query(value = "{'roleKey' : ?0}")
	UserRole findByRoleKey(String key);
	
	@Query(value = "{'enabled' : ?0, 'exposetoApi' : ?1 }")
	List<UserRole> findByStatus(boolean status, boolean exposetoApi);
	
	@Query(value = "{'permissions' : ?0}")
	List<UserRole> findByPermissionKey(Map<String,Boolean> permissionKey);

}
