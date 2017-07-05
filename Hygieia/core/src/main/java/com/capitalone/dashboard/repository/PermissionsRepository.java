package com.capitalone.dashboard.repository;

import org.bson.types.ObjectId;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.Permission;

public interface PermissionsRepository extends CrudRepository<Permission, ObjectId>,
QueryDslPredicateExecutor<Permission>, PermissionsRepositoryCustom {

}
