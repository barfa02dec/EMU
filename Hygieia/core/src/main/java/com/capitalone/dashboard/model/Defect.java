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
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a feature (story/requirement) of a component.
 * 
 * Possible collectors: VersionOne PivotalTracker Rally Trello Jira
 * 
 * @author kfk884
 * 
 */
@Document(collection = "defects")
public class Defect extends BaseModel {
	
	private ObjectId collectorId;
	
	/*
	 * Defect specific attributes
	 */
	@Indexed
	private String defectId;
	private String defectDescription;
	private String defectPriority;
	private String defectSeverity;
	private String defectStatus;
	private Integer defectAge;
	private String defectResolution;
	private Integer estimatedTime;
	private String createdBy;
	private String creationDate;
	private String updatedBy;
	private String updateDate;
	
	
	
	public ObjectId getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(ObjectId collectorId) {
		this.collectorId = collectorId;
	}

	public String getDefectId() {
		return defectId;
	}

	public void setDefectId(String defectId) {
		this.defectId = defectId;
	}

	public String getDefectDescription() {
		return defectDescription;
	}

	public void setDefectDescription(String defectDescription) {
		this.defectDescription = defectDescription;
	}

	public String getDefectPriority() {
		return defectPriority;
	}

	public void setDefectPriority(String defectPriority) {
		this.defectPriority = defectPriority;
	}

	public String getDefectSeverity() {
		return defectSeverity;
	}

	public void setDefectSeverity(String defectSeverity) {
		this.defectSeverity = defectSeverity;
	}

	public String getDefectStatus() {
		return defectStatus;
	}

	public void setDefectStatus(String defectStatus) {
		this.defectStatus = defectStatus;
	}

	public Integer getDefectAge() {
		return defectAge;
	}

	public void setDefectAge(Integer defectAge) {
		this.defectAge = defectAge;
	}

	public String getDefectResolution() {
		return defectResolution;
	}

	public void setDefectResolution(String defectResolution) {
		this.defectResolution = defectResolution;
	}

	public Integer getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(Integer estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Defect that = (Defect) o;
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(collectorId, that.collectorId).append(defectId, that.defectId).build();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(collectorId).append(defectId).toHashCode();
	}
}
