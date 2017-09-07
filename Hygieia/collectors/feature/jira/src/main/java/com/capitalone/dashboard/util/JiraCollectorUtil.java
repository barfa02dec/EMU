package com.capitalone.dashboard.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.capitalone.dashboard.model.Defect;
import com.capitalone.dashboard.model.DefectAggregation;
import com.capitalone.dashboard.model.JiraIssue;
import com.capitalone.dashboard.model.JiraSprint;
import com.capitalone.dashboard.model.JiraVersion;
import com.capitalone.dashboard.model.Scope;
import com.capitalone.dashboard.model.SprintData;
import com.capitalone.dashboard.model.VersionData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JiraCollectorUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraCollectorUtil.class);

	private static final String GET_PROJECT_SPRINTS = "/rest/greenhopper/1.0/integration/teamcalendars/sprint/list?jql=project=%1s";
	private static final String GET_PROJECT_SPRINT_DETAILS = "/rest/greenhopper/1.0/rapid/charts/sprintreport?rapidViewId=%1s&sprintId=%2$d";
	private static final String GET_DEFECTS_CREATED =  "/rest/api/2/search?jql=project=%1s and type in (Bug) and createdDate >\"%2s\" and createdDate <\"%3s\" &maxResults=1000";
	private static final String GET_DEFECTS_RESOLVED = "/rest/api/2/search?jql=project=%1s and type in (Bug) AND createddate>\"%2s\" and status = Done and Sprint = %3s &maxResults=1000";
	private static final String GET_DEFECTS_UNRESOLVED = "/rest/api/2/search?jql=project=%1s and type in (Bug) and createddate<\"%2s\" and (resolutiondate > \"%2s\" or resolution in (unresolved)) &maxResults=1000";
	private static final String DONE = "Done";
	public static final String GET_PROJECT_VERSIONS = "/rest/api/2/project/%1s/versions";
	public static final String GET_PROJECT_VERSION = "/rest/greenhopper/1.0/rapid/charts/versionreport?rapidViewId=%1s&versionId=%2s";
	public static final String GET_ISSUE = "/rest/api/2/issue/%1s?fields=%2s";
	public static final String GET_SPRINT_VELOCITY_REPORT = "/rest/greenhopper/1.0/rapid/charts/velocity?rapidViewId=%1s";
	public static final String GET_ALL_DEFECTS_RESOLVED = "/rest/api/2/search?jql=project=%1s and type in (Bug) and status = Done and Sprint = %2s &maxResults=1000";
	public static final String GET_VERSION_DEFECTS_CREATED =  "rest/api/2/search?jql=project=%1s and type in (Bug) and affectedVersion in (%2s) &maxResults=1000";
	public static final String GET_VERSION_DEFECTS_RESOLVED = "rest/api/2/search?jql=project=%1s and type in (Bug) AND fixVersion in (%2s) and resolution not in (Unresolved) &maxResults=1000";	

	public static String getDefectsFound(String projectId, String startdate, String enddate,String baseUrl,String base64Credentials) {
		try{
			String query = String.format(GET_DEFECTS_CREATED, projectId, startdate,enddate);
			query=baseUrl+query;
			HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result = restTemplate.exchange(query
					, HttpMethod.GET, entity, String.class);
			return result.getBody();
		}catch (Exception e) {
			return null;
		}
	}
	
	public static String getVersionDefectsFound(String projectId, Long releaseId,String baseUrl,String base64Credentials) {
		try{
			String query = String.format(GET_VERSION_DEFECTS_CREATED, releaseId);
			query=baseUrl+query;
			HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result = restTemplate.exchange(query
					, HttpMethod.GET, entity, String.class);
			return result.getBody();
		}catch (Exception e) {
			return null;
		}
	}
	

	public static String getDefectResolved(String projectId, String startdate,String enddate,String baseUrl,String base64Credentials) {
		try{
			String query = String.format(GET_ALL_DEFECTS_RESOLVED, projectId, startdate, enddate);
			query=baseUrl+query;
			HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result = restTemplate.exchange(query
					, HttpMethod.GET, entity, String.class);
			return result.getBody();
		}catch (Exception e) {
			return null;
		}
	}
	
	public static String getVersionDefectResolved(String projectId, Long versionId,String baseUrl,String base64Credentials) {
		try{
			String query = String.format(GET_VERSION_DEFECTS_RESOLVED, projectId, versionId);
			query=baseUrl+query;
			HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result = restTemplate.exchange(query
					, HttpMethod.GET, entity, String.class);
			return result.getBody();
		}catch (Exception e) {
			return null;
		}
	}
	public static String getDefectResolvedInSprint(String projectId, String startdate,Long sprintId,String baseUrl,String base64Credentials) {
		try{
			String query = String.format(GET_DEFECTS_RESOLVED, projectId, startdate,sprintId);
			query=baseUrl+query;
			HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result = restTemplate.exchange(query
					, HttpMethod.GET, entity, String.class);
			return result.getBody();
		}catch (Exception e) {
			return null;
		}
	}


	public static String getDefectUnresolved(String projectId, String startdate,String enddate,String baseUrl,String base64Credentials) {
		try{
			String query = String.format(GET_DEFECTS_UNRESOLVED, projectId, startdate, enddate);
			query=baseUrl+query;
			HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result = restTemplate.exchange(query
					, HttpMethod.GET, entity, String.class);
			return result.getBody();
		}catch (Exception e) {
			return null;
		}
	}
	/*
	 * This will give the list of recent n-sprints[n is the limit i.e noOfSprintsToShow value]
	 */
	
	public static List<JiraVersion>  getVersionsFromJira(String projectId,String baseUrl,String base64Credentials,String rapidViewId, int limit) {
		try{
			String query = String.format(GET_PROJECT_VERSIONS, projectId);
			query=baseUrl+query;
			HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result = restTemplate.exchange(query
					, HttpMethod.GET, entity, String.class);
			String versionJosn= result.getBody();
			JsonArray sprintArray = new GsonBuilder().create().fromJson(versionJosn, JsonArray.class);
			
			List<JiraVersion> versions = new ArrayList<JiraVersion>();
			if(sprintArray != null && sprintArray.size() > 0){
				for(int count = 0; count <  sprintArray.size() ; count++){
					versions.add(new Gson().fromJson(sprintArray.get(count), JiraVersion.class));
				}
			}
			Collections.sort(versions);
			return versions.stream().limit(limit).collect(Collectors.toList());
		}catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<JiraVersion>();
		}
	}
	
	public static  String getVersionDetailsFromJira(String boardId, Long versionId,String baseUrl,String base64Credentials){
		try{
			String query = String.format(GET_PROJECT_VERSION, boardId, versionId);
			query=baseUrl+query;
			HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result = restTemplate.exchange(query
					, HttpMethod.GET, entity, String.class);
			String versionJosn= result.getBody();
			return versionJosn;
		}catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	private static HttpHeaders getHeader(String base64Credentials) {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Credentials);

		return headers;
	}
	
	public static List<JiraSprint> getSprintList(String projectId,String baseUrl,String base64Credentials, int limit){
		try{
			String query =baseUrl+String.format(GET_PROJECT_SPRINTS, projectId);
			
			HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result = restTemplate.exchange(query
			, HttpMethod.GET, entity, String.class);
			String sprints=result.getBody();
			JsonArray sprintArray = new GsonBuilder().create().fromJson(sprints, JsonObject.class).getAsJsonArray("sprints");
			List<JiraSprint> sprintsJira = new ArrayList<JiraSprint>();
			if (sprintArray != null && sprintArray.size() > 0) {
				for (int count = 0; count < sprintArray.size(); count++) {
					JiraSprint jsprint=new Gson().fromJson(sprintArray.get(count),JiraSprint.class);
					sprintsJira.add(jsprint);
				}
			}
			Collections.sort(sprintsJira);
			
			return sprintsJira.stream().limit(limit).collect(Collectors.toList());
		}catch (Exception e) {
			// TODO: handle exception
			return new ArrayList<JiraSprint>();
		}
	}
	
	public static void getRecentSprintMetrics(JiraSprint jiraSprint, String projectId,NewFeatureSettings featureSettings){
		LOGGER.info("Processing Sprint"+jiraSprint.getName());
		String originalSprintData=fectSprintMetrcis(projectId,jiraSprint.getId(),featureSettings.getJiraBaseUrl(),featureSettings.getJiraCredentials(),featureSettings.getRapidView());
		SprintData sprintdata =ClientUtil.parseToSprintData(jiraSprint,originalSprintData);
		jiraSprint.setSprintData(sprintdata);
		// code changes incorporated from PMD-- BEGINS
		String json = null;
		List<String> issuesAdded = ClientUtil.getIssuesAddedDuringSprint(originalSprintData);
		if(issuesAdded != null && issuesAdded.size() > 0){
		   Double addedstorypoints = 0.0;
		   for( String issueAdded : issuesAdded){
			   json = getIssue(issueAdded,featureSettings.getJiraCredentials(),featureSettings.getJiraBaseUrl(),featureSettings.getJiraSprintDataFieldName());
			   addedstorypoints += getEstimate(json, featureSettings.getJiraSprintDataFieldName());
		   }   
		   sprintdata.getBurndown().getIssuesAdded().setStoryPoints(addedstorypoints);
		   sprintdata.getBurndown().getInitialIssueCount().setStoryPoints(sprintdata.getBurndown().getInitialIssueCount().getStoryPoints() - addedstorypoints);
		   sprintdata.setCommittedStoryPoints(sprintdata.getCommittedStoryPoints() - addedstorypoints);
		}
				
		json = getVelocityChart(featureSettings.getRapidView(),featureSettings.getJiraCredentials(),featureSettings.getJiraBaseUrl());
		Double estimate = ClientUtil.getSprintVelocity(json, jiraSprint.getId(), "estimated");
		if(estimate != null){
			sprintdata.getBurndown().getInitialIssueCount().setStoryPoints(estimate.doubleValue());
			sprintdata.setCommittedStoryPoints(estimate.doubleValue());
		}
		// code changes incorporated from PMD-- ENDS
		List<JiraIssue> issues = new ArrayList<JiraIssue>();
		String startDate=DateUtil.format(jiraSprint.getSprintData().getStartDate(),	ClientUtil.DATE_FORMAT_5);
		String endDate=DateUtil.format(	jiraSprint.getSprintData().getCompleteDate(),ClientUtil.DATE_FORMAT_5);
		// Get created defects
		json = JiraCollectorUtil.getDefectsFound(projectId,startDate,endDate,featureSettings.getJiraBaseUrl(),featureSettings.getJiraCredentials());
		if(null==json) return;
		issues = DefectUtil.parseDefectsJson(json);	
		jiraSprint.getSprintData().setDefectsFound(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
		
		// Get resolved defects
		json = JiraCollectorUtil.getDefectResolvedInSprint(projectId, startDate, jiraSprint.getId(), featureSettings.getJiraBaseUrl(),featureSettings.getJiraCredentials());
		issues = DefectUtil.parseDefectsJson(json);		
		jiraSprint.getSprintData().setDefectsResolved(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
		// get all defects resolved
		json = getSprintAllDefectResolved(projectId, sprintdata.getSprintId(),featureSettings.getJiraBaseUrl(),featureSettings.getJiraCredentials());
		
	//	issues = DefectUtil.parseDefectsJson(projectjirasetting, json);		
		sprintdata.setDefectsResolved(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));

		// Get unresolved defects
		json = JiraCollectorUtil.getDefectUnresolved(projectId,startDate,endDate,featureSettings.getJiraBaseUrl(),featureSettings.getJiraCredentials());
		issues = DefectUtil.parseDefectsJson(json);		
		jiraSprint.getSprintData().setDefectsUnresolved(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
		
	}
	public static VersionData getReleaseData(String versionJson, String projectId ,String baseUrl,String base64Credentials,String rapidViewId){
		
		VersionData versionData = parseToVersionData(versionJson);
		LOGGER.info("processing Release metrics"+versionData.getReleaseName());

		List<JiraIssue> issues = new ArrayList<JiraIssue>();
	
		if(null!=versionData && versionData.getStartDate() != null && versionData.getReleaseDate() != null){
			String startDate=DateUtil.format(versionData.getStartDate(), "yyyy/MM/dd HH:mm");
			String releaseDate=DateUtil.format(versionData.getReleaseDate(), "yyyy/MM/dd HH:mm");
			//Get created defects 
			String json = getVersionDefectsFound(projectId, versionData.getReleaseId(),baseUrl,base64Credentials);
			if(null==json) return versionData;
			issues = DefectUtil.parseDefectsJson(json);
			versionData.setDefectsFound(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
			
			//Get resolved defects 
			json = getVersionDefectResolved(projectId,versionData.getReleaseId(),baseUrl,base64Credentials );
			issues = DefectUtil.parseDefectsJson(json);
			versionData.setDefectsResolved(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
			
			// Get unresolved defects
			//Commenting the API as per PMD code changes.
			/*json = getDefectUnresolved(projectId,startDate,releaseDate,baseUrl,base64Credentials);
			issues = DefectUtil.parseDefectsJson(json);
			versionData.setDefectsUnresolved(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));*/
		}
		
		return versionData;
	}
	
	private static String fectSprintMetrcis(String projectId, Long sprintId,String baseUrl,String base64Credentials, String rapidViewId){
		try{
			String query = String.format(GET_PROJECT_SPRINT_DETAILS, StringUtils.trimWhitespace(rapidViewId), sprintId);
			query=baseUrl+query;
			HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result = restTemplate.exchange(query
					, HttpMethod.GET, entity, String.class);
			return result.getBody();
		}catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}
	
	public static void processDefectsByPriorityAndEnvironment(List<Defect> defects,DefectAggregation aggregation,Scope scopeProject){
		Map<String,Integer> defectsByProirity= new LinkedHashMap<String,Integer>();
		Map<String,Integer> defectsByEnvironment= new LinkedHashMap<String,Integer>();
		
			for(Defect defect: defects){
			
			if(!defect.getDefectStatus().equals(DONE) && defect.getProjectId().equals(scopeProject.getpId())){
				if(defectsByProirity.containsKey(defect.getDefectPriority())){
					defectsByProirity.put(defect.getDefectPriority(), defectsByProirity.get(defect.getDefectPriority())+1);
				}else{
					defectsByProirity.put(defect.getDefectPriority(), 1);
				}
				
				if(defectsByEnvironment.containsKey(defect.getEnvironment())){
					defectsByEnvironment.put(defect.getEnvironment(), defectsByEnvironment.get(defect.getEnvironment())+1);
				}else if(null!=defect.getEnvironment()){
					defectsByEnvironment.put(defect.getEnvironment(), 1);
				}
			}
			
			
			}
		
			if(!defectsByProirity.isEmpty())
			{
				aggregation.setDefectsByProirity(defectsByProirity);
			}
			if(!defectsByEnvironment.isEmpty())
			{
				aggregation.setDefectsByEnvironment(defectsByEnvironment);
			}
		
	}
	
	public static VersionData parseToVersionData(String json){
    	
		VersionData releasedata = new VersionData();
		
		Gson gson = new GsonBuilder().create();
    	releasedata.setReleaseId(gson.fromJson(json, JsonObject.class).getAsJsonObject("version").get("id").getAsLong());
		releasedata.setReleaseName(isJsonObjectNull(gson.fromJson(json, JsonObject.class).getAsJsonObject("version").get("name")));
		releasedata.setDescription(isJsonObjectNull(gson.fromJson(json, JsonObject.class).getAsJsonObject("version").get("description")));
		releasedata.setReleased(new Boolean(gson.fromJson(json, JsonObject.class).getAsJsonObject("version").get("released").toString()));
		
		String startdate = isJsonObjectNull(gson.fromJson(json, JsonObject.class).getAsJsonObject("version").get("startDate"));
		String releasedate = isJsonObjectNull(gson.fromJson(json, JsonObject.class).getAsJsonObject("version").get("releaseDate"));
		
		releasedata.setStartDate(StringUtils.isEmpty(startdate) ? null : new Date(Long.parseLong(startdate)));  			
		releasedata.setReleaseDate(StringUtils.isEmpty(releasedate) ? null : new Date(Long.parseLong(releasedate)));  
		
		
    	releasedata.setNoofStoryCompleted(gson.fromJson(json, JsonObject.class).getAsJsonObject("contents").getAsJsonArray("completedIssues") == null ? 
				0 : gson.fromJson(json, JsonObject.class).getAsJsonObject("contents").getAsJsonArray("completedIssues").size());

    	releasedata.setNoofStoryPoints(gson.fromJson(json, JsonObject.class).getAsJsonObject("contents").get("completedIssuesEstimateSum") == null ? 
				0 : gson.fromJson(json, JsonObject.class).getAsJsonObject("contents").getAsJsonObject("completedIssuesEstimateSum").get("value").getAsFloat());
    	
    	return releasedata;		
	}

	private static String isJsonObjectNull(JsonElement gsonelement){
		return gsonelement == null ? null : gsonelement.toString();
	}
	
	public static String getIssue(String issueId, String base64Credentials,String baseUrl, String storyPointsCustomField) {
		// OPEN: jiraStoryPoints		
		try{
			String query = String.format(GET_ISSUE, issueId, storyPointsCustomField);
			query=baseUrl+query;
			HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result = restTemplate.exchange(query
					, HttpMethod.GET, entity, String.class);
			String issueJson= result.getBody();
			return issueJson;
		}catch (Exception e) {
			// TODO: handle exception
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
	
	public static String getVelocityChart(String boardId, String base64Credentials,String baseUrl) {
		try{
			String query = String.format(GET_SPRINT_VELOCITY_REPORT, boardId);
			query=baseUrl+query;
			HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result = restTemplate.exchange(query
					, HttpMethod.GET, entity, String.class);
			String velocityJson= result.getBody();
			return velocityJson;
		}catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
	
	public static String getSprintAllDefectResolved(String projectId, Long sprintId, String base64Credentials,String baseUrl) {
		try{
			String query = String.format(GET_ALL_DEFECTS_RESOLVED, projectId, sprintId);
			query=baseUrl+query;
			HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result = restTemplate.exchange(query
					, HttpMethod.GET, entity, String.class);
			String sprintAllDefectResolvedJson= result.getBody();
			return sprintAllDefectResolvedJson;
		}catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}	

}
