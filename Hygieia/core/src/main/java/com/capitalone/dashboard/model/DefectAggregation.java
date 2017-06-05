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

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a feature (story/requirement) of a component.
 * 
 * Possible collectors: VersionOne PivotalTracker Rally Trello Jira
 * 
 * @author kfk884
 * 
 */
@Document(collection = "defects_summary")
public class DefectAggregation extends BaseModel {
	
	private ObjectId collectorId;
	private Map<String,Integer> defectsByProirity;
	private Map<String,Integer> defectsByEnvironment;
	Map<String, List<Map<String,String>>> defectsByResolutionDetails;
	Map<String, List<Map<String,String>>> defectsByAgeDetails;
	private String valuesAsOn;
	private String projectName;
	private String projectId;
	
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public ObjectId getCollectorId() {
		return collectorId;
	}
	public void setCollectorId(ObjectId collectorId) {
		this.collectorId = collectorId;
	}
	public Map<String, Integer> getDefectsByProirity() {
		return defectsByProirity;
	}
	public void setDefectsByProirity(Map<String, Integer> defectsByProirity) {
		this.defectsByProirity = defectsByProirity;
	}
	public Map<String, Integer> getDefectsByEnvironment() {
		return defectsByEnvironment;
	}
	public void setDefectsByEnvironment(Map<String, Integer> defectsByEnvironment) {
		this.defectsByEnvironment = defectsByEnvironment;
	}
	public Map<String, List<Map<String, String>>> getDefectsByResolutionDetails() {
		return defectsByResolutionDetails;
	}
	public void setDefectsByResolutionDetails(Map<String, List<Map<String, String>>> defectsByResolutionDetails) {
		this.defectsByResolutionDetails = defectsByResolutionDetails;
	}
	public Map<String, List<Map<String, String>>> getDefectsByAgeDetails() {
		return defectsByAgeDetails;
	}
	public void setDefectsByAgeDetails(Map<String, List<Map<String, String>>> defectsByAgeDetails) {
		this.defectsByAgeDetails = defectsByAgeDetails;
	}
	public String getValuesAsOn() {
		return valuesAsOn;
	}
	public void setValuesAsOn(String valuesAsOn) {
		this.valuesAsOn = valuesAsOn;
	}
	
	
}