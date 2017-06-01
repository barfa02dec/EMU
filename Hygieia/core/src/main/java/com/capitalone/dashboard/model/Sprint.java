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

package com.capitalone.dashboard.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 
 * @author munisekhar.k
 * 
 */
@Document(collection = "sprint")
public class Sprint extends BaseModel {
	
	private ObjectId collectorId;
	
	/*
	 * Sprint specific attributes
	 */
	
	private Long sprintId;
	private Long rapidViewId;
	private String state;
	private String name;
	private String startDateStr;
	private String endDateStr;
	private String completeDateStr;
	private int sequence;
	
	

	public ObjectId getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(ObjectId collectorId) {
		this.collectorId = collectorId;
	}

	public Long getSprintId() {
		return sprintId;
	}

	public void setSprintId(Long sprintId) {
		this.sprintId = sprintId;
	}

	public Long getRapidViewId() {
		return rapidViewId;
	}

	public void setRapidViewId(Long rapidViewId) {
		this.rapidViewId = rapidViewId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getCompleteDateStr() {
		return completeDateStr;
	}

	public void setCompleteDateStr(String completeDateStr) {
		this.completeDateStr = completeDateStr;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Sprint that = (Sprint) o;
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(collectorId, that.collectorId).append(sprintId, that.sprintId).build();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(collectorId).append(sprintId).toHashCode();
	}
}
