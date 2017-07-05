package com.capitalone.dashboard.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="release")
public class Release extends BaseModel {
		@Indexed(unique=true)
		private Long releaseId;
	    private String description;
	    private String name;
	    private Boolean archived;
	    private Boolean released;
	    private String releaseDate;
	    private String startDate;
	    private String overdue;
	    private VersionData versionData;
	    private String originalreleaseData;
	    private String projectId;
	    
	    	    
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
		public String getReleaseDate() {
			return releaseDate;
		}
		public void setReleaseDate(String releaseDate) {
			this.releaseDate = releaseDate;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
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
		
		
	    
	    
}
