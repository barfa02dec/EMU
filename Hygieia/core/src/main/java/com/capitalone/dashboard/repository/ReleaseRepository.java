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

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.Release;

/**
 * Repository for {@link FeatureCollector}.
 */
public interface ReleaseRepository extends CrudRepository<Release, ObjectId>,
		QueryDslPredicateExecutor<Release>, ReleaseRepositoryCustom {

	 	@Query(value = "{ 'projectId' : ?0, 'releaseId' : ?1}")
	 	Release findByReleaseId(String projectId, Long releaseId);
	 	
	 	@Query(value = "{ 'projectId' : ?0}")
	 	Iterable<Release> findByProjectId(String projectId);
	 	
	 	@Query(value = "{ 'projectId' : ?0, 'released' : ?1}")
	 	Iterable<Release> findByProjectId(String projectId, Boolean released);

}