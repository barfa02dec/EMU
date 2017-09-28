/*************************DA-BOARD-LICENSE-START*********************************
 * Copyright 2014 CapitalOne, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************DA-BOARD-LICENSE-END*********************************/

package com.capitalone.dashboard.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.Defect;

/**
 * Repository for {@link FeatureCollector}.
 */
public interface DefectRepository extends CrudRepository<Defect, ObjectId>,
		QueryDslPredicateExecutor<Defect>, DefectRepositoryCustom {

	@Query(value = "{'collectorId' : ?0}", fields = "{'collectorId' : ?1}")
	List<Defect> findById(ObjectId collectorId);
	/**
	 * This essentially returns the max change date from the collection, based
	 * on the last change date (or default delta change date property) available
	 * 
	 * @param collectorId
	 *            Collector ID of source system collector
	 * @param changeDate
	 *            Last available change date or delta begin date property
	 * @return A single Change Date value that is the maximum value of the
	 *         existing collection
	 */
	

	@Query(value = "{'defectStatus' : ?0}")
	List<Defect> findByStatus(String defectStatus);
	
	@Query(value = "{'defectId' : ?0}")
	Defect findByDefectId(String defectId);
	@Query(value = "{'projectId' : ?0, 'emuProjectId':?1}")
	List<Defect> findByProjectId(String projectId, String emuProjectId);
	
	@Query(value = "{'defectSeverity' : ?0}")
	List<Defect> findBySeverity(String defectSeverity);
	
	@Query(value = "{'defectAge' : ?0}")
	List<Defect> findByAge(Integer defectAge);
	
	@Query(value = "{'defectId' : ?0}", fields = "{'defectId' : 1}")
	List<Defect> getDefectById(String sId);
	
	
	
	
	
}