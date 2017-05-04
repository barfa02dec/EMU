package com.capitalone.dashboard.model;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "project")
public class Project extends BaseModel {
	
	@Indexed(unique = true)
	private String projectName;
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
