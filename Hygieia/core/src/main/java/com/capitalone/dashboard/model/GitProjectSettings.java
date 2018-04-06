package com.capitalone.dashboard.model;

/**
 * Created by benathmane on 23/06/16.
 */

/**
 * Bean to hold settings specific to the Gitlab project 
 */


public class GitProjectSettings {

	private String host;
    private String apiToken;
    private String projectId;
    
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
