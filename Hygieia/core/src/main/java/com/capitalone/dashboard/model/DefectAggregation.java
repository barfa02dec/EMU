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

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a feature (story/requirement) of a component.
 * 
 * Possible collectors: VersionOne PivotalTracker Rally Trello Jira
 * 
 * @author munisekhar
 * 
 */
@Document(collection = "defects_summary")
public class DefectAggregation extends BaseModel{
	
	private Map<String,Integer> defectsByPriority;
	private Map<String,Integer> defectsByEnvironment;
	private Map<String, List<Map<String,String>>> fixeddefectsByResolutions;
	private Map<String, List<Map<String,String>>> openDefectsByAge;
	private String valuesAsOn;
	private String projectName;
	private String projectId;
	private String metricsProjectId;
	private boolean automated;
	
	public Map<String, List<Map<String, String>>> getFixeddefectsByResolutions() {
		return fixeddefectsByResolutions;
	}
	public void setFixeddefectsByResolutions(Map<String, List<Map<String, String>>> fixeddefectsByResolutions) {
		this.fixeddefectsByResolutions = fixeddefectsByResolutions;
	}
	public Map<String, List<Map<String, String>>> getOpenDefectsByAge() {
		return openDefectsByAge;
	}
	public void setOpenDefectsByAge(Map<String, List<Map<String, String>>> openDefectsByAge) {
		this.openDefectsByAge = openDefectsByAge;
	}
	public String getMetricsProjectId() {
		return metricsProjectId;
	}
	public void setMetricsProjectId(String metricsProjectId) {
		this.metricsProjectId = metricsProjectId;
	}
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
	
	public Map<String, Integer> getDefectsByProirity() {
		return defectsByPriority;
	}
	public void setDefectsByProirity(Map<String, Integer> defectsByPriority) {
		this.defectsByPriority = defectsByPriority;
	}
	public Map<String, Integer> getDefectsByEnvironment() {
		return defectsByEnvironment;
	}
	public void setDefectsByEnvironment(Map<String, Integer> defectsByEnvironment) {
		this.defectsByEnvironment = defectsByEnvironment;
	}
	public Map<String, List<Map<String, String>>> getDefectsByResolutionDetails() {
		return fixeddefectsByResolutions;
	}
	public void setDefectsByResolutionDetails(Map<String, List<Map<String, String>>> defectsByResolutionDetails) {
		this.fixeddefectsByResolutions = defectsByResolutionDetails;
	}
	public Map<String, List<Map<String, String>>> getDefectsByAgeDetails() {
		return openDefectsByAge;
	}
	public void setDefectsByAgeDetails(Map<String, List<Map<String, String>>> defectsByAgeDetails) {
		this.openDefectsByAge = defectsByAgeDetails;
	}
	public String getValuesAsOn() {
		return valuesAsOn;
	}
	public void setValuesAsOn(String valuesAsOn) {
		this.valuesAsOn = valuesAsOn;
	}
	public boolean isAutomated() {
		return automated;
	}
	public void setAutomated(boolean automated) {
		this.automated = automated;
	}
}