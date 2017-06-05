package com.capitalone.dashboard.model;

import java.util.List;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "project")
@CompoundIndex(name="projectIdName", def="{'projectId': 1, 'projectName': 1}")
public class Project extends BaseModel {
	private String projectName;
	private String projectId;
	private boolean projectActiveStatus;
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public boolean isProjectActiveStatus() {
		return projectActiveStatus;
	}

	public void setProjectActiveStatus(boolean projectActiveStatus) {
		this.projectActiveStatus = projectActiveStatus;
	}

	@DBRef
    private List<Authentication> userIds;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List<Authentication> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Authentication> userIds) {
		this.userIds = userIds;
	}

	
	
	
	
	

}
