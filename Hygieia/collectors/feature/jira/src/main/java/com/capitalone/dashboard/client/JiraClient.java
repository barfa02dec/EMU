package com.capitalone.dashboard.client;

import java.util.List;
import java.util.Map;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.capitalone.dashboard.model.Team;
import com.capitalone.dashboard.util.NewFeatureSettings;

public interface JiraClient {
	List<Issue> getIssues(long startTime, int pageStart, NewFeatureSettings featureSettings);
	
	List<BasicProject> getProjects(NewFeatureSettings featureSettings);
	
	List<Team> getTeams(NewFeatureSettings featureSettings);
	
	Issue getEpic(String epicId,NewFeatureSettings featureSettings);
	
	int getPageSize();

	List<Issue> getEpics(List<String> epicKeys,NewFeatureSettings featureSettings);
	
	Map<String, String> getStatusMapping(NewFeatureSettings featureSettings);
}
