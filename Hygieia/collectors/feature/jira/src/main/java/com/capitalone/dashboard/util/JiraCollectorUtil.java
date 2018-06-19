package com.capitalone.dashboard.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.capitalone.dashboard.model.JiraIssue;
import com.capitalone.dashboard.model.JiraSprint;
import com.capitalone.dashboard.model.JiraVersion;
import com.capitalone.dashboard.model.SprintData;
import com.capitalone.dashboard.model.VersionData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JiraCollectorUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraCollectorUtil.class);


	public static final String GET_ISSUE = "issue/%1s?fields=%2s";
	public static final String GET_JIRA_PRIORITY =  "priority";
	public static final String GET_JIRA_PROJECTS =  "project";
	
	//Defect related jira queries
	//public static final String GET_OPEN_DEFECTS_SEVERITY =  "/rest/api/2/search?jql=project=%1s and type in (Bug) and  resolution in (Unresolved) &maxResults=100";
	//public static final String GET_ALL_CLOSED_DEFECTS = "/rest/api/2/search?jql=project=%1s AND type in (Bug) and  resolution not in (Unresolved) &maxResults=100";
	//private static final String GET_DEFECTS_CREATED =  "/rest/api/2/search?jql=project=%1s and type in (Bug) and createdDate >\"%2s\" and createdDate <\"%3s\" &maxResults=100";

	//Sprint related jira queries
	//private static final String GET_SPRINT_DEFECTS_RESOLVED = "/rest/api/2/search?jql=project=%1s and type in (Bug) AND createddate>\"%2s\" and status = Done and Sprint = %3s &maxResults=100";
	//public static final String GET_SPRINT_ALL_DEFECTS_RESOLVED = "/rest/api/2/search?jql=project=%1s and type in (Bug) and status = Done and Sprint = %2s &maxResults=100";
	//private static final String GET_SPRINT_DEFECTS_UNRESOLVED = "/rest/api/2/search?jql=project=%1s and type in (Bug) and createddate<\"%2s\" and (resolutiondate > \"%2s\" or resolution in (unresolved)) &maxResults=100";

	private static final String GET_PROJECT_SPRINTS = "rest/greenhopper/1.0/integration/teamcalendars/sprint/list?jql=project in (%1s)";
	private static final String GET_PROJECT_SPRINT_DETAILS = "rest/greenhopper/1.0/rapid/charts/sprintreport?rapidViewId=%1s&sprintId=%2$d";
	public static final String GET_SPRINT_VELOCITY_REPORT = "rest/greenhopper/1.0/rapid/charts/velocity?rapidViewId=%1s";

	//Version related jira queries
	public static final String GET_PROJECT_VERSIONS = "rest/api/2/project/%1s/versions";  
	public static final String GET_PROJECT_VERSION = "rest/greenhopper/1.0/rapid/charts/versionreport?rapidViewId=%1s&versionId=%2s";

	//public static final String GET_VERSION_DEFECTS_CREATED =  "/rest/api/2/search?jql=project=%1s and type in (Bug) and affectedVersion in (%2s) &maxResults=100";
	//public static final String GET_VERSION_DEFECTS_RESOLVED = "/rest/api/2/search?jql=project=%1s and type in (Bug) AND fixVersion in (%2s) and resolution not in (Unresolved) &maxResults=100";
	
	public static final String GET_SPRINTS = "rest/greenhopper/1.0/sprintquery/%1s?includeFutureSprints=false";
	public static final String	GET_SPRINT_BURNDOWN = "rest/greenhopper/1.0/rapid/charts/scopechangeburndownchart.json?rapidViewId=%1$d&sprintId=%2$d";

	
	/*
	 * This will give the list of recent n-sprints[n is the limit i.e noOfSprintsToShow value]
	 */
	
	public static List<JiraSprint> getSprintList(String projectId, NewFeatureSettings featureSettings){
		try{
			StringBuffer query = new StringBuffer().append(featureSettings.getJiraBaseUrl())
					.append(String.format(GET_PROJECT_SPRINTS, projectId));

			String sprints = executeJiraQuery(featureSettings, query.toString());	
			JsonArray sprintArray = new GsonBuilder().create().fromJson(sprints, JsonObject.class).getAsJsonArray("sprints");
			
			List<JiraSprint> jiraSprints = new ArrayList<JiraSprint>();
			if (sprintArray != null && sprintArray.size() > 0) {
				for (int count = 0; count < sprintArray.size(); count++) {
					JiraSprint jsprint = new Gson().fromJson(sprintArray.get(count), JiraSprint.class);
					jiraSprints.add(jsprint);
				}
			}
			return jiraSprints;
		}catch (Exception e) {
			return new ArrayList<JiraSprint>();
		}
	}
	
	public static void getSprintMetrics(JiraSprint jiraSprint, String projectId, NewFeatureSettings featureSettings) {
		
		String originalSprintData = getSprintDetails(projectId,jiraSprint.getId(), featureSettings);

		SprintData sprintdata = ClientUtil.parseToSprintData(jiraSprint,originalSprintData);
		
		jiraSprint.setSprintData(sprintdata);
		
		String json = null;
		List<String> issuesAdded = ClientUtil.getIssuesAddedDuringSprint(originalSprintData);
		if(!StringUtils.isEmpty(issuesAdded)){
		   Double addedstorypoints = 0.0;
		   for( String issueAdded : issuesAdded){
			   json = getIssue(issueAdded,featureSettings,featureSettings.getJiraStoryPointsFieldName());
			   addedstorypoints += getEstimate(json, featureSettings.getJiraStoryPointsFieldName());
		   }   
		   sprintdata.getBurndown().getIssuesAdded().setStoryPoints(addedstorypoints);
		   sprintdata.getBurndown().getInitialIssueCount().setStoryPoints(sprintdata.getBurndown().getInitialIssueCount().getStoryPoints() - addedstorypoints);
		   sprintdata.setCommittedStoryPoints(sprintdata.getCommittedStoryPoints() - addedstorypoints);
		}

		StringBuffer restQuery = new StringBuffer().append(featureSettings.getJiraBaseUrl())
				.append(String.format(GET_SPRINT_VELOCITY_REPORT, featureSettings.getRapidView()));

		json = executeJiraQuery(featureSettings, restQuery.toString());
		
		Double estimate = ClientUtil.getSprintVelocity(json, jiraSprint.getId(), "estimated");
		if(estimate != null){
			sprintdata.getBurndown().getInitialIssueCount().setStoryPoints(estimate.doubleValue());
			sprintdata.setCommittedStoryPoints(estimate.doubleValue());
		}
		
		// Get new defects found in the sprint
		List<JiraIssue> issues = new ArrayList<JiraIssue>();
		String startDate=DateUtil.format(jiraSprint.getSprintData().getStartDate(),	ClientUtil.DATE_FORMAT_5);
		String endDate=DateUtil.format(	jiraSprint.getSprintData().getCompleteDate(),ClientUtil.DATE_FORMAT_5);

		String query = String.format(featureSettings.getDefectsCreatedQuery(), projectId, startDate,endDate);
		issues = getIssues(query, featureSettings);
		jiraSprint.getSprintData().setDefectsFound(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));

		// Get Defects Resolved found in same sprint
		query = String.format(featureSettings.getSprintDefectsResolvedQuery(), projectId, startDate, jiraSprint.getId());
		issues = getIssues(query, featureSettings);
		jiraSprint.getSprintData().setSprintDefectsResolved(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));

		// Get all defects resolved
		query = String.format(featureSettings.getSprintAllDefectsResolvedQuery(), projectId, sprintdata.getSprintId());
		issues = getIssues(query, featureSettings);
		jiraSprint.getSprintData().setDefectsResolved(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));

		// Get unresolved defects
		query = String.format(featureSettings.getSprintDefectsUnresolvedQuery(), projectId, endDate, endDate);
		issues = getIssues(query, featureSettings);
		jiraSprint.getSprintData().setDefectsUnresolved(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
	}
	
	private static String getSprintDetails(String projectId, Long sprintId, NewFeatureSettings featureSettings){
		try{
			StringBuffer restQuery = new StringBuffer().append(featureSettings.getJiraBaseUrl())
					.append(String.format(GET_PROJECT_SPRINT_DETAILS, StringUtils.trimWhitespace(featureSettings.getRapidView()), sprintId));

			return executeJiraQuery(featureSettings, restQuery.toString());
		}catch (Exception e) {
			return null;
		}
	}


	public static VersionData getReleaseData(String versionDetailJson, String projectId , NewFeatureSettings featureSettings){

		VersionData versionData = parseToVersionData(versionDetailJson);
		LOGGER.info("Processing Release Metrics "+ versionData.getReleaseName());

		List<JiraIssue> issues = new ArrayList<JiraIssue>();
	
		if(null != versionData){
			
			//Get created defects
			String query = String.format(featureSettings.getVersionDefectsCreatedQuery(), projectId, versionData.getReleaseId());
			issues = getIssues(query, featureSettings);
			 
			/*if(CollectionUtils.isEmpty(issues) && versionData.getStartDate() != null && versionData.getReleaseDate() != null) {
				query = String.format(featureSettings.getDefectsCreatedQuery(), projectId, DateUtil.format(versionData.getStartDate(), "yyyy/MM/dd HH:mm"),DateUtil.format(versionData.getReleaseDate(), "yyyy/MM/dd HH:mm"));
				issues = getIssues(query, featureSettings);
			}*/
			
			if(!CollectionUtils.isEmpty(issues)) {
		    	versionData.setDefectsFound(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
			}
			
			//Get resolved defects
			query = String.format(featureSettings.getVersionDefectsResolvedQuery(), projectId, versionData.getReleaseId());
			issues = getIssues(query, featureSettings);
			versionData.setDefectsResolved(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
		}
		
		return versionData;
	}
	
	public static List<JiraVersion>  getVersionsFromJira(String projectId, NewFeatureSettings featureSettings) {
		try{
			
			StringBuffer restQuery = new StringBuffer().append(featureSettings.getJiraBaseUrl())
					.append(String.format(GET_PROJECT_VERSIONS, projectId));

			String versionJosn = executeJiraQuery(featureSettings, restQuery.toString());
			JsonArray sprintArray = new GsonBuilder().create().fromJson(versionJosn, JsonArray.class);
			
			List<JiraVersion> versions = new ArrayList<JiraVersion>();
			if(sprintArray != null && sprintArray.size() > 0){
				for(int count = 0; count <  sprintArray.size() ; count++){
					versions.add(new Gson().fromJson(sprintArray.get(count), JiraVersion.class));
				}
			}
			return versions;
		}catch (Exception e) {
			return new ArrayList<JiraVersion>();
		}
	}
	
	public static  String getVersionDetailsFromJira(Long versionId, NewFeatureSettings featureSettings){
		try{
			StringBuffer restQuery = new StringBuffer().append(featureSettings.getJiraBaseUrl())
					.append(String.format(GET_PROJECT_VERSION, featureSettings.getRapidView(), versionId));

			return executeJiraQuery(featureSettings, restQuery.toString());
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static VersionData parseToVersionData(String json){
    	
		VersionData versiondata = new VersionData();
		
		Gson gson = new GsonBuilder().create();
    	versiondata.setReleaseId(gson.fromJson(json, JsonObject.class).getAsJsonObject("version").get("id").getAsLong());
		versiondata.setReleaseName(isJsonObjectNull(gson.fromJson(json, JsonObject.class).getAsJsonObject("version").get("name")));
		versiondata.setDescription(isJsonObjectNull(gson.fromJson(json, JsonObject.class).getAsJsonObject("version").get("description")));
		versiondata.setReleased( Boolean.valueOf((gson.fromJson(json, JsonObject.class).getAsJsonObject("version").get("released").toString())));
		
		String startdate = isJsonObjectNull(gson.fromJson(json, JsonObject.class).getAsJsonObject("version").get("startDate"));
		String releasedate = isJsonObjectNull(gson.fromJson(json, JsonObject.class).getAsJsonObject("version").get("releaseDate"));
		
		versiondata.setStartDate(StringUtils.isEmpty(startdate) ? null : new Date(Long.parseLong(startdate)));  			
		versiondata.setReleaseDate(StringUtils.isEmpty(releasedate) ? null : new Date(Long.parseLong(releasedate)));  
		
    	versiondata.setNoofStoryCompleted(gson.fromJson(json, JsonObject.class).getAsJsonObject("contents").getAsJsonArray("completedIssues") == null ? 
				0 : gson.fromJson(json, JsonObject.class).getAsJsonObject("contents").getAsJsonArray("completedIssues").size());

    	versiondata.setNoofStoryPoints(gson.fromJson(json, JsonObject.class).getAsJsonObject("contents").get("completedIssuesEstimateSum") == null ? 
				0 : gson.fromJson(json, JsonObject.class).getAsJsonObject("contents").getAsJsonObject("completedIssuesEstimateSum").get("value").getAsFloat());
    	
    	return versiondata;		
	}

	
	private static String isJsonObjectNull(JsonElement gsonelement){
		return gsonelement == null ? null : gsonelement.toString();
	}
	
	public static String getIssue(String issueId, NewFeatureSettings featureSettings , String storyPointsCustomField) {
		try{
			StringBuffer query = new StringBuffer().append(featureSettings.getJiraBaseUrl())
					.append(featureSettings.getJiraQueryEndpoint())
					.append( String.format(GET_ISSUE, issueId, storyPointsCustomField));
			return executeJiraQuery(featureSettings, query.toString());
		}catch (Exception e) {
			return null;
		}
	}
	
	public static Integer getEstimate(String json, String field){
		try{
			Gson gson = new GsonBuilder().create();
	    	return (gson.fromJson(json, JsonObject.class).has("fields") && gson.fromJson(json, JsonObject.class).getAsJsonObject("fields") != null && 
	    			gson.fromJson(json, JsonObject.class).getAsJsonObject("fields").get(field) != null) &&
	    			!gson.fromJson(json, JsonObject.class).getAsJsonObject("fields").get(field).isJsonNull() ? gson.fromJson(json, JsonObject.class).getAsJsonObject("fields").get(field).getAsInt() : 0;
		}catch(Exception ex){
				return 0;
		}
	}
	
	public static String getVelocityChart(String boardId, NewFeatureSettings featureSettings) {
		try{
			StringBuffer restQuery = new StringBuffer().append(featureSettings.getJiraBaseUrl())
					.append(String.format(GET_SPRINT_VELOCITY_REPORT, boardId));

			return executeJiraQuery(featureSettings, restQuery.toString());
		}catch (Exception e) {
			return null;
		}
	}
	
	public static List<JiraIssue> getClosedDefectsByProject(NewFeatureSettings featureSettings){
			return getIssues(String.format(featureSettings.getClosedDefectsQuery(), String.join(",", featureSettings.getJiraProjectIdList())) , featureSettings);
	}
	
	public static List<JiraIssue> getOpenDefectsByProject(NewFeatureSettings featureSettings){
		return getIssues(String.format(featureSettings.getOpenDefectsQuery(), String.join(",", featureSettings.getJiraProjectIdList())) , featureSettings);
	}

	public static List<String> getJiraPriority(NewFeatureSettings featureSettings){
		StringBuffer restQuery = new StringBuffer().append(featureSettings.getJiraBaseUrl())
				.append(featureSettings.getJiraQueryEndpoint()).append(GET_JIRA_PRIORITY);

		String json = executeJiraQuery(featureSettings, restQuery.toString());
		JsonArray jsonArr = new GsonBuilder().create().fromJson(json, JsonArray.class);
		
		ArrayList<String> priorties = new ArrayList<String>();
		for (int i = 0; i < jsonArr.size(); i++){
			priorties.add(jsonArr.get(i).getAsJsonObject().get("name").getAsString());
		}
		return priorties;
	}

	public static List<BasicProject> getJiraProjects(NewFeatureSettings featureSettings){
		StringBuffer restQuery = new StringBuffer().append(featureSettings.getJiraBaseUrl())
				.append(featureSettings.getJiraQueryEndpoint()).append(GET_JIRA_PROJECTS);

		String json = executeJiraQuery(featureSettings, restQuery.toString());
		JsonArray jsonArr = new GsonBuilder().create().fromJson(json, JsonArray.class);
		
		ArrayList<BasicProject> projects = new ArrayList<BasicProject>();
		for (int i = 0; i < jsonArr.size(); i++){
			projects.add(new GsonBuilder().create().fromJson(jsonArr.get(i), BasicProject.class));
		}
		return projects;
	}

	private static List<JiraIssue> getIssues(String query,  NewFeatureSettings featureSettings){
		int startAt = 0, total = 0;
		List<JiraIssue> issuelist = new ArrayList<JiraIssue>();
		
		StringBuffer restQuery = new StringBuffer().append(featureSettings.getJiraBaseUrl()).append(featureSettings.getJiraQueryEndpoint())
				.append("search?jql=").append(query).append(" &maxResults=100");
		
		do{
			String json = executeJiraQuery(featureSettings, String.format((restQuery + "&startAt=%1$d"), startAt));
			List<JiraIssue> issues = DefectUtil.parseDefectsJson(json, featureSettings);
			
			if(issues != null)
				issuelist.addAll(issues);
			
			Gson gson = new GsonBuilder().create();
			total = gson.fromJson(json, JsonObject.class).get("total").getAsInt();

			startAt = startAt + 100;
		}while(startAt < total);
		
		return issuelist;
	}

	private static String executeJiraQuery(NewFeatureSettings featureSettings, String query){
		/*HttpEntity<String> entity;
		if(featureSettings.isCredentialReq())
			entity = new HttpEntity<String>(getHeader(featureSettings.getJiraCredentials()));
		else 
			entity = null;*/
		
		HttpEntity<String> entity = new HttpEntity<String>(getHeader(featureSettings.getJiraCredentials()));
		//RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(query, HttpMethod.GET, entity, String.class);

		return result.getBody();
	}
	
	private static HttpHeaders getHeader(String base64Credentials) {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Credentials);
		return headers;
	}

	public static List<JiraSprint> getSprints(NewFeatureSettings featureSettings){
		String query = String.format(GET_SPRINTS, featureSettings.getProjectId());
		String json = executeJiraQuery(featureSettings, query);
		
		JsonArray sprintArray = new GsonBuilder().create().fromJson(json, JsonObject.class).getAsJsonArray("sprints");
		
		List<JiraSprint> jiraSprints = new ArrayList<JiraSprint>();
		if (sprintArray != null && sprintArray.size() > 0) {
			for (int count = 0; count < sprintArray.size(); count++) {
				JiraSprint jsprint = new Gson().fromJson(sprintArray.get(count), JiraSprint.class);
				jiraSprints.add(jsprint);
			}
		}
		return jiraSprints;
	}
}
