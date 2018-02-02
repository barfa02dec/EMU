package com.capitalone.dashboard.repository;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.HeatMap;

public interface HeatMapRepository extends CrudRepository<HeatMap, ObjectId>,

    QueryDslPredicateExecutor<HeatMap>, HeatMapRepositoryCustom {
	
	@Query(value = "{ 'projectId' : ?0}")
	Iterable<HeatMap> findByProjectId(String projectId);
	
 	@Query(value = "{'heatmapId' : ?0}")
	HeatMap findByHeatMapId(Long heatmapId);
 	
 	@Query(value = "{'projectId' : ?0}")
	HeatMap findByOneProjectId(String projectId);

	

 	

}