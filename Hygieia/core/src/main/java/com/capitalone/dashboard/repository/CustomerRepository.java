package com.capitalone.dashboard.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.model.Customer;
import com.capitalone.dashboard.model.HeatMap;


public interface CustomerRepository extends CrudRepository<Customer, ObjectId>,
QueryDslPredicateExecutor<Customer>, CustomerRepositoryCustom {
	
	
	@Query(value = " {'customerName' : ?0 }")
	List<Customer> findByCustomerName(String customerName);
	
	@Query(value = " {'customerCode' : ?0 }")
	Customer findByCustomerCode(String customerCode);
	

}
