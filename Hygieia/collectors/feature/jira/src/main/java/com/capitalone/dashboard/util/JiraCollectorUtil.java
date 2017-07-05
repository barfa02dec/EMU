package com.capitalone.dashboard.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.capitalone.dashboard.model.VersionData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JiraCollectorUtil {
	
	private static final String GET_PROJECT_SPRINTS = "/rest/greenhopper/1.0/integration/teamcalendars/sprint/list?jql=project=%1s";
	private static final String GET_PROJECT_SPRINT_DETAILS = "/rest/greenhopper/1.0/rapid/charts/sprintreport?rapidViewId=%1s&sprintId=%2$d";
	private static final String GET_DEFECTS_CREATED =  "/rest/api/2/search?jql=project=%1s and type in (Bug) and createdDate >\"%2s\" and createdDate <\"%3s\" &maxResults=1000";
	private static final String GET_DEFECTS_RESOLVED = "/rest/api/2/search?jql=project=%1s and type in (Bug) AND resolutiondate>\"%2s\" and resolutiondate  < \"%3s\" &maxResults=1000";
	private static final String GET_DEFECTS_UNRESOLVED = "/rest/api/2/search?jql=project=%1s and type in (Bug) and createddate<\"%2s\" and (resolutiondate > \"%2s\" or resolution in (unresolved)) &maxResults=1000";
	private static final String DONE = "Done";
	public static final String GET_PROJECT_VERSIONS = "/rest/api/2/project/%1s/versions";
	public static final String GET_PROJECT_VERSION = "/rest/greenhopper/1.0/rapid/charts/versionreport?rapidViewId=%1s&versionId=%2s";
	
	
	public static String getDefectsFound(String projectId, String startdate, String enddate,String baseUrl,String base64Credentials) {
		String query = String.format(GET_DEFECTS_CREATED, projectId, startdate,enddate);
		query=baseUrl+query;
		HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(query
				, HttpMethod.GET, entity, String.class);
		return result.getBody();
	}

	public static String getDefectResolved(String projectId, String startdate,String enddate,String baseUrl,String base64Credentials) {
		String query = String.format(GET_DEFECTS_RESOLVED, projectId, startdate, enddate);
		query=baseUrl+query;
		HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(query
				, HttpMethod.GET, entity, String.class);
		return result.getBody();
	}

	public static String getDefectUnresolved(String projectId, String startdate,String enddate,String baseUrl,String base64Credentials) {
		String query = String.format(GET_DEFECTS_UNRESOLVED, projectId, startdate, enddate);
		query=baseUrl+query;
		HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(query
				, HttpMethod.GET, entity, String.class);
		return result.getBody();
	}
	
	public static List<JiraVersion>  getVersionsFromJira(String projectId,String baseUrl,String base64Credentials,String rapidViewId) {
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
		if(!versions.isEmpty() && null!=versions.get(0)){
				String detailedVersionMetrics=getVersionDetailsFromJira(rapidViewId,versions.get(0).getId(), baseUrl, base64Credentials);
				versions.get(0).setVersionData(getReleaseData(detailedVersionMetrics, projectId, baseUrl, base64Credentials, rapidViewId));
		}
		return versions;
	}
	
	public static  String getVersionDetailsFromJira(String boardId, Long versionId,String baseUrl,String base64Credentials){
		String query = String.format(GET_PROJECT_VERSION, boardId, versionId);
		query=baseUrl+query;
		HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(query
				, HttpMethod.GET, entity, String.class);
		String versionJosn= result.getBody();
		return versionJosn;
	}
	
	private static HttpHeaders getHeader(String base64Credentials) {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Credentials);

		return headers;
	}
	
	public static List<JiraSprint> getSprintList(String projectId,String baseUrl,String base64Credentials){
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
		
		return sprintsJira;
	}
	
	public static void getRecentSprintMetrics(JiraSprint jiraSprint, String projectId,String baseUrl,String base64Credentials,String rapidViewId){
		jiraSprint.setSprintData(ClientUtil.parseToSprintData(fectSprintMetrcis(projectId,jiraSprint.getId(),baseUrl,base64Credentials,rapidViewId)));
		jiraSprint.getSprintData().setStartDate(DateUtil.convertStringToDate(jiraSprint.getStart(), "ddMMyyyyHHmmss"));
		jiraSprint.getSprintData().setEndDate(DateUtil.convertStringToDate(jiraSprint.getEnd(), "ddMMyyyyHHmmss"));
		
		List<JiraIssue> issues = new ArrayList<JiraIssue>();
		String startDate=DateUtil.format(jiraSprint.getSprintData().getStartDate(),	"yyyy/MM/dd HH:mm");
		String endDate=DateUtil.format(	jiraSprint.getSprintData().getEndDate(),"yyyy/MM/dd HH:mm");
		// Get created defects
		String json = JiraCollectorUtil.getDefectsFound(projectId,startDate,endDate,baseUrl,base64Credentials);
		issues = DefectUtil.parseDefectsJson(json);	
		jiraSprint.getSprintData().setDefectsFound(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
		
		// Get resolved defects
		json = JiraCollectorUtil.getDefectResolved(projectId,startDate,endDate,baseUrl,base64Credentials);
		issues = DefectUtil.parseDefectsJson(json);		
		jiraSprint.getSprintData().setDefectsResolved(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
		
		// Get unresolved defects
		json = JiraCollectorUtil.getDefectUnresolved(projectId,startDate,endDate,baseUrl,base64Credentials);
		issues = DefectUtil.parseDefectsJson(json);		
		jiraSprint.getSprintData().setDefectsUnresolved(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
		
	}
	private static VersionData getReleaseData(String versionJson, String projectId ,String baseUrl,String base64Credentials,String rapidViewId){
		
		VersionData versionData = parseToVersionData(versionJson);
		

		List<JiraIssue> issues = new ArrayList<JiraIssue>();
	
		if(null!=versionData && versionData.getStartDate() != null && versionData.getReleaseDate() != null){
			String startDate=DateUtil.format(versionData.getStartDate(), "yyyy/MM/dd HH:mm");
			String endDate=DateUtil.format(versionData.getReleaseDate(), "yyyy/MM/dd HH:mm");
			//Get created defects 
			String json = getDefectsFound(projectId, startDate,endDate,baseUrl,base64Credentials);
			issues = DefectUtil.parseDefectsJson(json);
			versionData.setDefectsFound(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
			
			//Get resolved defects 
			json = getDefectResolved(projectId,startDate,endDate,baseUrl,base64Credentials );
			issues = DefectUtil.parseDefectsJson(json);
			versionData.setDefectsResolved(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
			
			// Get unresolved defects
			json = getDefectUnresolved(projectId,startDate,endDate,baseUrl,base64Credentials);
			issues = DefectUtil.parseDefectsJson(json);
			versionData.setDefectsUnresolved(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
		}
		
		return versionData;
	}
	
	private static String fectSprintMetrcis(String projectId, Long sprintId,String baseUrl,String base64Credentials, String rapidViewId){
		String query = String.format(GET_PROJECT_SPRINT_DETAILS, StringUtils.trimWhitespace(rapidViewId), sprintId);
		query=baseUrl+query;
		HttpEntity<String> entity = new HttpEntity<String>(getHeader(base64Credentials));
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(query
				, HttpMethod.GET, entity, String.class);
		return result.getBody();
		
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
	
	

}
