package com.capitalone.dashboard.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.HeatMap;

public interface HeatMapRepository extends CrudRepository<HeatMap, ObjectId>,
    QueryDslPredicateExecutor<HeatMap>, HeatMapRepositoryCustom {
	
	@Query(value = "{'projectId' : ?0, 'submissionDate':?1}")
	List<HeatMap> findByProjectId(String projectId, String submissionDate);

	@Query(value = "{'projectId' : ?0, 'submissionDate':?1 }")
	HeatMap findByOneProjectId(String projectId, String submissionDate);
	
	
	@Query(value = " {'projectId' : ?0 }")
	List<HeatMap> getByProjectId(String projectId);

}