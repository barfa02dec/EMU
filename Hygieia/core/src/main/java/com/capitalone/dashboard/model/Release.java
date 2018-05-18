package com.capitalone.dashboard.model;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="release")
@CompoundIndex(def = "{'projectId':1, 'releaseId':1}", name = "index_Release_projectId_releaseId", unique=true)
public class Release extends BaseModel implements java.lang.Comparable<Release>{
	private Long releaseId;
    private String projectId;
    private String jiraProjectId;
	
    private String description;
    private String name;
    private Boolean archived;
    private Boolean released;
    private Date releaseDate;
    private Date startDate;
    private String overdue;
    private VersionData versionData;
    private String originalreleaseData;
    private String projectName;
    private boolean automated;
    	    
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Long getReleaseId() {
		return releaseId;
	}
	public void setReleaseId(Long releaseId) {
		this.releaseId = releaseId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getArchived() {
		return archived;
	}
	public void setArchived(Boolean archived) {
		this.archived = archived;
	}
	public Boolean getReleased() {
		return released;
	}
	public void setReleased(Boolean released) {
		this.released = released;
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getOverdue() {
		return overdue;
	}
	public void setOverdue(String overdue) {
		this.overdue = overdue;
	}
	
	public VersionData getVersionData() {
		return versionData;
	}
	public void setVersionData(VersionData versionData) {
		this.versionData = versionData;
	}
	
	public String getOriginalreleaseData() {
		return originalreleaseData;
	}
	public void setOriginalreleaseData(String originalreleaseData) {
		this.originalreleaseData = originalreleaseData;
	}
	
	public boolean getAutomated() {
		return automated;
	}
	public void setAutomated(boolean automated) {
		this.automated = automated;
	}
	
	public String getJiraProjectId() {
		return jiraProjectId;
	}
	public void setJiraProjectId(String jiraProjectId) {
		this.jiraProjectId = jiraProjectId;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Release that = (Release) o;
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(releaseId, that.releaseId).build();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(releaseId).toHashCode();
	}
	
	@Override
	public int compareTo(Release o) {
		//sort by descending order
		if(o.releaseId > this.releaseId){
			return 1;
		}else if(o.releaseId == this.releaseId){
			return 0;
		}
		return -1;
	}
}
