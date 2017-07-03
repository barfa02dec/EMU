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

package com.capitalone.dashboard.client.story;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.capitalone.dashboard.client.JiraClient;
import com.capitalone.dashboard.model.Defect;
import com.capitalone.dashboard.model.DefectAggregation;
import com.capitalone.dashboard.model.Feature;
import com.capitalone.dashboard.model.FeatureStatus;
import com.capitalone.dashboard.model.JiraIssue;
import com.capitalone.dashboard.model.JiraSprint;
import com.capitalone.dashboard.model.QDefect;
import com.capitalone.dashboard.model.QSprint;
import com.capitalone.dashboard.model.Scope;
import com.capitalone.dashboard.model.Sprint;
import com.capitalone.dashboard.model.SprintData;
import com.capitalone.dashboard.repository.DefectAggregationRepository;
import com.capitalone.dashboard.repository.DefectRepository;
import com.capitalone.dashboard.repository.FeatureCollectorRepository;
import com.capitalone.dashboard.repository.FeatureRepository;
import com.capitalone.dashboard.repository.SprintRepository;
import com.capitalone.dashboard.repository.TeamRepository;
import com.capitalone.dashboard.util.ClientUtil;
import com.capitalone.dashboard.util.CoreFeatureSettings;
import com.capitalone.dashboard.util.DateUtil;
import com.capitalone.dashboard.util.DefectUtil;
import com.capitalone.dashboard.util.FeatureCollectorConstants;
import com.capitalone.dashboard.util.NewFeatureSettings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;



/**
 * This is the primary implemented/extended data collector for the feature
 * collector. This will get data from the source system, but will grab the
 * majority of needed data and aggregate it in a single, flat MongoDB collection
 * for consumption.
 * 
 * @author kfk884
 * 
 */
public class StoryDataClientImpl implements StoryDataClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(StoryDataClientImpl.class);
	private static final ClientUtil TOOLS = ClientUtil.getInstance();
	private static final String TO_DO = "To Do";
	private static final String IN_PROGRESS = "In Progress";
	private static final String DONE = "Done";
	private static final String BUG="Bug";
	private static final String GET_PROJECT_SPRINTS = "/rest/greenhopper/1.0/integration/teamcalendars/sprint/list?jql=project=%1s";
	private static final String GET_PROJECT_SPRINT_DETAILS = "/rest/greenhopper/1.0/rapid/charts/sprintreport?rapidViewId=%1s&sprintId=%2$d";
	private static final String GET_DEFECTS_CREATED =  "/rest/api/2/search?jql=project=%1s and type in (Bug) and createdDate >\"%2s\" and createdDate <\"%3s\" &maxResults=1000";
	private static final String GET_DEFECTS_RESOLVED = "/rest/api/2/search?jql=project=%1s and type in (Bug) AND resolutiondate>\"%2s\" and resolutiondate  < \"%3s\" &maxResults=1000";
	private static final String GET_DEFECTS_UNRESOLVED = "/rest/api/2/search?jql=project=%1s and type in (Bug) and createddate<\"%2s\" and (resolutiondate > \"%2s\" or resolution in (unresolved)) &maxResults=1000";

	// works with ms too (just ignores them)
	private final DateFormat SETTINGS_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	private final NewFeatureSettings featureSettings;
	private final FeatureRepository featureRepo;
	private final DefectRepository defectRepository;
	private final SprintRepository sprintRepository;
	private final DefectAggregationRepository defectAggregationRepository;
	private final FeatureCollectorRepository featureCollectorRepository;
	private final JiraClient jiraClient;
	
	// epicId : list of epics
	private Map<String, Issue> epicCache;
	private Set<String> todoCache;
	private Set<String> inProgressCache;
	private Set<String> doneCache;

	/**
	 * Extends the constructor from the super class.
	 */
	public StoryDataClientImpl(CoreFeatureSettings coreFeatureSettings, NewFeatureSettings featureSettings,
			FeatureRepository featureRepository,DefectRepository defectRepository,SprintRepository sprintRepository,DefectAggregationRepository defectAggregationRepository, FeatureCollectorRepository featureCollectorRepository, TeamRepository teamRepository,
			JiraClient jiraClient) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing data collection for the feature widget, story-level data...");
		}

		this.featureSettings = featureSettings;
		this.featureRepo = featureRepository;
		this.featureCollectorRepository = featureCollectorRepository;
		this.jiraClient = jiraClient;
		this.defectRepository=defectRepository;
		this.sprintRepository=sprintRepository;
		this.defectAggregationRepository=defectAggregationRepository;
		this.epicCache = new HashMap<>();
		
		todoCache = buildStatusCache(coreFeatureSettings.getTodoStatuses());
		inProgressCache = buildStatusCache(coreFeatureSettings.getDoingStatuses());
		doneCache = buildStatusCache(coreFeatureSettings.getDoneStatuses());
	}

	/**
	 * Explicitly updates queries for the source system, and initiates the
	 * update to MongoDB from those calls.
	 */
	public int updateStoryInformation() {
		int count = 0;
		epicCache.clear(); 
		
		String startDateStr = featureSettings.getDeltaStartDate();
		String maxChangeDate = getMaxChangeDate();
		if (maxChangeDate != null) {
			startDateStr = maxChangeDate;
		}
		
		startDateStr = getChangeDateMinutePrior(startDateStr);
		long startTime;
		try {
			startTime = SETTINGS_DATE_FORMAT.parse(startDateStr).getTime();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
		int pageSize = jiraClient.getPageSize();
			
		updateStatuses();

		boolean hasMore = true;
		for (int i = 0; hasMore; i += pageSize) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Obtaining story information starting at index " + i + "...");
			}
			long queryStart = System.currentTimeMillis();
			List<Issue> issues = jiraClient.getIssues(startTime, i,featureSettings);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Story information query took " + (System.currentTimeMillis() - queryStart) + " ms");
			}
			
			if (issues != null && !issues.isEmpty()) {
				updateMongoInfo(issues);
				count += issues.size();
			}

			LOGGER.info("Loop i " + i + " pageSize " + issues.size());
			
			// will result in an extra call if number of results == pageSize
			// but I would rather do that then complicate the jira client implementation
			if (issues == null || issues.size() < pageSize) {
				hasMore = false;
				break;
			}
		}
		
		return count;
	}

	/**
	 * Updates the MongoDB with a JSONArray received from the source system
	 * back-end with story-based data.
	 * 
	 * @param currentPagedJiraRs
	 *            A list response of Jira issues from the source system
	 */
	
	private void updateMongoInfo(List<Issue> currentPagedJiraRs) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					"Size of paged Jira response: " + (currentPagedJiraRs == null ? 0 : currentPagedJiraRs.size()));
		}

		if (currentPagedJiraRs != null) {

			Set<String> issueTypeNames = new HashSet<>();
			for (String issueTypeName : featureSettings.getJiraIssueTypeNames()) {
				issueTypeNames.add(issueTypeName.toLowerCase(Locale.getDefault()));
			}

			
			List<Defect> defectsToSave = new ArrayList<Defect>();
			for (Issue issue : currentPagedJiraRs) {
				Map<String, IssueField> fields = buildFieldMap(issue.getFields());

				IssueType issueType = issue.getIssueType();

				if (issueTypeNames
						.contains(TOOLS.sanitizeResponse(issueType.getName()).toLowerCase(Locale.getDefault()))) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(String.format("[%-12s] %s", TOOLS.sanitizeResponse(issue.getKey()),
								TOOLS.sanitizeResponse(issue.getSummary())));
					}
					if (TOOLS.sanitizeResponse(issueType.getName()).equals(BUG)) {
						Defect defect=defectRepository.findByDefectId(TOOLS.sanitizeResponse(issue.getId()));
						if(null==defect){
							defect=new Defect();
						}
						defectsToSave.add(processDefects(issue, defect, fields));

					}
					
				}
			}

			if (null != defectsToSave && !defectsToSave.isEmpty()) {

				defectRepository.save(defectsToSave);

			}
			
			defectsToSave = null;

		}
	}	
	
	
	public void saveDetailedSprintData(String projectId){
		featureSettings.getRapidView();
		String query =featureSettings.getJiraBaseUrl()+String.format(GET_PROJECT_SPRINTS, projectId);
		
		HttpEntity<String> entity = new HttpEntity<String>(getHeader());

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
		// get the details of the sprint for recent sprint and for remaining, don't fetch the data.
		//As the sprints grownup, we will get recent all sprints in 4-5 iterations. 
		boolean sprintDataCaptured=false;
		if(!sprintDataCaptured && null!=sprintsJira.get(0))
		{
			sprintsJira.get(0).setSprintData(ClientUtil.parseToSprintData(fectSprintMetrcis(projectId,sprintsJira.get(0).getId())));
			sprintsJira.get(0).getSprintData().setStartDate(DateUtil.convertStringToDate(sprintsJira.get(0).getStart(), "ddMMyyyyHHmmss"));
			sprintsJira.get(0).getSprintData().setEndDate(DateUtil.convertStringToDate(sprintsJira.get(0).getEnd(), "ddMMyyyyHHmmss"));
			
			List<JiraIssue> issues = new ArrayList<JiraIssue>();
			System.out.println(sprintsJira.get(0).getSprintData());
			// Get created defects
			String json = getSprintDefectsFound(projectId,
					DateUtil.format(sprintsJira.get(0).getSprintData().getStartDate(),
							"yyyy/MM/dd HH:mm"), DateUtil.format(
									sprintsJira.get(0).getSprintData().getEndDate(),
							"yyyy/MM/dd HH:mm"));
			issues = DefectUtil.parseDefectsJson(json);	
			sprintsJira.get(0).getSprintData().setDefectsFound(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
			
			// Get resolved defects
			json = getSprintDefectResolved(projectId,
					DateUtil.format(sprintsJira.get(0).getSprintData().getStartDate(),
							"yyyy/MM/dd HH:mm"), DateUtil.format(
									sprintsJira.get(0).getSprintData().getEndDate(),
							"yyyy/MM/dd HH:mm"));
			issues = DefectUtil.parseDefectsJson(json);		
			sprintsJira.get(0).getSprintData().setDefectsResolved(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
			
			// Get unresolved defects
			json = getSprintDefectUnresolved(projectId,
					DateUtil.format(sprintsJira.get(0).getSprintData().getStartDate(),
							"yyyy/MM/dd HH:mm"));
			issues = DefectUtil.parseDefectsJson(json);		
			sprintsJira.get(0).getSprintData().setDefectsUnresolved(DefectUtil.defectCount(DefectUtil.defectCountBySeverity(issues)));
			//setting the sprintDataCaptured flag to true to ensure we get the detailed sprint data only for recent sprint.
			sprintDataCaptured=true;

			
		}
		
		mapJiraSprintToHygieiaModel(sprintsJira,projectId);
		
		
	}
	
	private void mapJiraSprintToHygieiaModel(List<JiraSprint> sprintsJira, String projectId){
		List<Sprint> list= new ArrayList<Sprint>();
		for(JiraSprint js:sprintsJira){
			Sprint sprint=sprintRepository.findOne(QSprint.sprint.sid.eq(js.getId()));
			if(null==sprint){
				sprint= new Sprint();
				sprint.setSid(js.getId());
			}
			sprint.setEditable(js.getEditable());
			sprint.setStart(js.getStart());
			sprint.setEnd(js.getEnd());
			sprint.setName(js.getName());
			sprint.setViewBoardsUrl(js.getViewBoardsUrl());
			sprint.setSprintData(js.getSprintData());
			sprint.setProjectId(projectId);
			list.add(sprint);
		}
		sprintRepository.save(list);
		
	}
	
	private String fectSprintMetrcis(String projectId, Long sprintId){
		String query = String.format(GET_PROJECT_SPRINT_DETAILS, StringUtils.trimWhitespace(featureSettings.getRapidView()), sprintId);
		query=featureSettings.getJiraBaseUrl()+query;
		HttpEntity<String> entity = new HttpEntity<String>(getHeader());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(query
				, HttpMethod.GET, entity, String.class);
		return result.getBody();
		
	}
	
	private HttpHeaders getHeader() {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + featureSettings.getJiraCredentials());

		return headers;
	}
	

	/**
	 * ETL for converting any number of custom Jira statuses to a reduced list
	 * of generally logical statuses used by Hygieia
	 * 
	 * @param nativeStatus
	 *            The status label as native to Jira
	 * @return A Hygieia-canonical status, as defined by a Core enum
	 */
	private String toCanonicalFeatureStatus(String nativeStatus) {
		// default to backlog
		String canonicalStatus = FeatureStatus.BACKLOG.getStatus();
		
		if (nativeStatus != null) {
			String nsLower = nativeStatus.toLowerCase(Locale.getDefault());
			
			if (todoCache.contains(nsLower)) {
				canonicalStatus = FeatureStatus.BACKLOG.getStatus();
			} else if (inProgressCache.contains(nsLower)) {
				canonicalStatus = FeatureStatus.IN_PROGRESS.getStatus();
			} else if (doneCache.contains(nsLower)) {
				canonicalStatus = FeatureStatus.DONE.getStatus();
			}
		}
		
		return canonicalStatus;
	}
	
	/**
	 * Retrieves the maximum change date for a given query.
	 * 
	 * @return A list object of the maximum change date
	 */
	public String getMaxChangeDate() {
		String data = null;

		try {
			List<Feature> response = featureRepo
					.findTopByCollectorIdAndChangeDateGreaterThanOrderByChangeDateDesc(
							featureCollectorRepository.findByName(FeatureCollectorConstants.JIRA).getId(),
							featureSettings.getDeltaStartDate());
			if ((response != null) && !response.isEmpty()) {
				data = response.get(0).getChangeDate();
			}
		} catch (Exception e) {
			LOGGER.error("There was a problem retrieving or parsing data from the local "
					+ "repository while retrieving a max change date\nReturning null", e);
		}

		return data;
	}
	
	
	
	private String getChangeDateMinutePrior(String changeDateISO) {
		int priorMinutes = this.featureSettings.getScheduledPriorMin();
		return DateUtil.toISODateRealTimeFormat(DateUtil.getDatePriorToMinutes(
				DateUtil.fromISODateTimeFormat(changeDateISO), priorMinutes));
	}
	

	
	private Map<String, IssueField> buildFieldMap(Iterable<IssueField> fields) {
		Map<String, IssueField> rt = new HashMap<String, IssueField>();
		
		if (fields != null) {
			for (IssueField issueField : fields) {
				rt.put(issueField.getId(), issueField);
			}
		}
		
		return rt;
	}
	
	private Set<String> buildStatusCache(List<String> statuses) {
		Set<String> rt = new HashSet<>();
		
		if (statuses != null) {
			for (String status : statuses) {
				rt.add(status.toLowerCase(Locale.getDefault()));
			}
		}
		
		return rt;
	}
	
	private void updateStatuses() {
		Map<String, String> statusMap = jiraClient.getStatusMapping(featureSettings);
		for (String status : statusMap.keySet()) {
			String statusCategory = statusMap.get(status);
			if (TO_DO.equals(statusCategory)) {
				todoCache.add(status.toLowerCase(Locale.getDefault()));
			} else if (IN_PROGRESS.equals(statusCategory)) {
				inProgressCache.add(status.toLowerCase(Locale.getDefault()));
			} else if (DONE.equals(statusCategory)) {
				doneCache.add(status.toLowerCase(Locale.getDefault()));
			}
		}
	}
	private Defect processDefects(Issue issue,Defect defect,Map<String, IssueField> fields){
		
		/*setting the collector ID as multiple of defect ID  and project ID to avoid the creation of duplicate Defects. 
		 * There are scenarios where different projects will be same / defect ID will be same w.r.t different JIRA's  
		 */
		defect.setCollectorId(issue.getId() * issue.getProject().getId());
		defect.setDefectId(TOOLS.sanitizeResponse(issue.getId()));
		defect.setProjectId(issue.getProject().getId().toString());
		defect.setProjectName(issue.getProject().getName());
		defect.setDefectStatus(this.toCanonicalFeatureStatus(issue.getStatus().getName()));
		defect.setDefectDescription(issue.getSummary());
		defect.setDefectPriority(null!=issue.getPriority()?issue.getPriority().getName():null);
		defect.setCreationDate(issue.getCreationDate().toString());
		defect.setCreatedBy(null!=issue.getReporter()?issue.getReporter().getName():null);
		int originalEstimate = 0;
		
		if (issue.getTimeTracking() != null && issue.getTimeTracking().getOriginalEstimateMinutes() != null) {
			originalEstimate = issue.getTimeTracking().getOriginalEstimateMinutes();
		} else if (fields.get("aggregatetimeoriginalestimate") != null
				&& fields.get("aggregatetimeoriginalestimate").getValue() != null) {
			// this value is in seconds
			originalEstimate = ((Integer)fields.get("aggregatetimeoriginalestimate").getValue()) / 60;
		}
		defect.setOriginalEstimate(originalEstimate);
		defect.setDefectResolutionStatus(null!=issue.getResolution()?issue.getResolution().getName():null);
		defect.setReporter(null!=issue.getReporter()?issue.getReporter().toString():null);
		defect.setUpdateDate(null!=issue.getUpdateDate()?issue.getUpdateDate().toString():null);
		defect.setAssignee(null!=issue.getAssignee()?issue.getAssignee().getName().toString():null);
		//defect age
		Date createdDate=DateUtil.fromISODateTimeFormat(issue.getCreationDate().toString());
		Date updateDate=DateUtil.fromISODateTimeFormat(issue.getUpdateDate().toString());
		Date now= new Date();
		if(null!=defect.getDefectResolutionStatus() && defect.getDefectResolutionStatus().equals(DONE) )
		{
			defect.setDefectResolutionInDays(DateUtil.differenceInDays(updateDate, createdDate));
			defect.setDefectAge(defect.getDefectResolutionInDays());
		}
		if(null==defect.getDefectResolutionStatus()){
			defect.setDefectAge(DateUtil.differenceInDays(now, createdDate));
		}
		return defect;
	
	}
	
	private DefectAggregation processDefectsSummary(NewFeatureSettings featureSettings,List<Defect> defects,Scope scopeProject,DefectAggregation summery){
		
		/*
		 * Logic to bucket the defects based on environment and priority.
		 */
		processDefectsByPriorityAndEnvironment(defects, summery,scopeProject);
		
		/*
		 * Logic for bucketing the defects based on resolution days and priority in each class of resolution.
		 */
		processDefectsByDefectResolutionPeriod(summery,scopeProject);
				
		/*
		 * 	Logic for bucketing the defects based on age of open defects.
		 */
		processDefectsByDefectAge(summery,scopeProject);
		
		return summery;
	}


	@Override
	public void processDefectAggregation(NewFeatureSettings featureSettings,List<Defect> defectsInDB,Scope scopeProject) {
		/*
		 * For a single project, there is always a single aggregater exists.Hence setting the collector ID as same as scope ID. 
		 */
		DefectAggregation summery=defectAggregationRepository.findByProjectId(scopeProject.getpId());
		if(null==summery){
			summery=new DefectAggregation();
		}
		summery=processDefectsSummary(featureSettings,defectsInDB,scopeProject,summery);
		summery.setProjectId(scopeProject.getpId());
		summery.setProjectName(scopeProject.getName());
		summery.setValuesAsOn(new Date().toString());
		defectAggregationRepository.save(summery);
	}
	
	private void processDefectsByPriorityAndEnvironment(List<Defect> defects,DefectAggregation aggregation,Scope scopeProject){
		Map<String,Integer> defectsByProirity= new LinkedHashMap<String,Integer>();
		Map<String,Integer> defectsByEnvironment= new LinkedHashMap<String,Integer>();
		
			for(Defect defect: defects){
			
			if(defect.getProjectId().equals(scopeProject.getpId())){
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
	
	
	private void processDefectsByDefectResolutionPeriod(DefectAggregation aggregation,Scope scopeProject){
		List<Integer> resolutionsList= new ArrayList<Integer>();
		for (int i=0;i<featureSettings.getResolutionPeriod().length;i++){
			try{
				if(!resolutionsList.contains(featureSettings.getResolutionPeriod()[i]))
					resolutionsList.add(Integer.parseInt(featureSettings.getResolutionPeriod()[i]));
			}catch (Exception e) {
				LOGGER.debug(e.getMessage());
			}
		}
		Collections.sort(resolutionsList);
		Map<String, List<Map<String,String>>> fixedDefectsByResolution=new LinkedHashMap<String, List<Map<String,String>>>();
				
		boolean firstIndex=true;
		int resolCount=resolutionsList.size();
		Set<String> defectPriorities=null!=aggregation.getDefectsByProirity()?aggregation.getDefectsByProirity().keySet():null;
		//If defect priority set is null, then there are no defects in that particular project. So no need to show the defects by resolution.
		if(null==defectPriorities){
			return;
		}
		for(int i=0;i<resolCount;i++){
			Map<String,String> metric= new HashMap<String,String>();
			if(firstIndex){
				String key="lessthan"+resolutionsList.get(i)+"days";
				firstIndex=false;
				List<Map<String,String>> defectsByResolution= new ArrayList<Map<String,String>>();
				for(String priorityKey: defectPriorities){
					metric.put(priorityKey, (String.valueOf(defectRepository.count(QDefect.defect.defectResolutionInDays.lt(resolutionsList.get(i)+1).and(QDefect.defect.defectPriority.equalsIgnoreCase(priorityKey)).and(QDefect.defect.projectId.equalsIgnoreCase(scopeProject.getpId()).and(QDefect.defect.defectStatus.equalsIgnoreCase(DONE)))))));
				}
				metric.put("Resolution Strategy", key);
				defectsByResolution.add(metric);
				fixedDefectsByResolution.put("Range"+(i+1), defectsByResolution);
			}else{
				String key="between"+(resolutionsList.get(i-1)+1)+"to"+resolutionsList.get(i)+"days";
				List<Map<String,String>> defectsByResolution= new ArrayList<Map<String,String>>();
				for(String priorityKey: defectPriorities){
					
					metric.put(priorityKey, (String.valueOf(defectRepository.count(QDefect.defect.defectResolutionInDays.between(resolutionsList.get(i-1)+1 ,resolutionsList.get(i) ).and(QDefect.defect.defectPriority.equalsIgnoreCase(priorityKey)).and(QDefect.defect.projectId.equalsIgnoreCase(scopeProject.getpId()).and(QDefect.defect.defectStatus.equalsIgnoreCase(DONE)))))));
					
				}
				metric.put("Resolution Strategy", key);
				defectsByResolution.add(metric);
				
				fixedDefectsByResolution.put("Range"+(i+1), defectsByResolution);
				}
			
			}
			String keyAfterUpperLimit="morethan"+resolutionsList.get(resolCount-1)+"days";
			List<Map<String,String>> defectsByResolutionMorethanUpperLimit= new ArrayList<Map<String,String>>();
			Map<String,String> metricsAfterUpperLimit= new HashMap<String,String>();
			for(String priorityKey: defectPriorities){
				metricsAfterUpperLimit.put(priorityKey, (String.valueOf(defectRepository.count(QDefect.defect.defectResolutionInDays.gt(resolutionsList.get(resolCount-1)).and(QDefect.defect.defectPriority.equalsIgnoreCase(priorityKey)).and(QDefect.defect.projectId.equalsIgnoreCase(scopeProject.getpId()).and(QDefect.defect.defectStatus.equalsIgnoreCase(DONE)))))));
			}
			metricsAfterUpperLimit.put("Resolution Strategy", keyAfterUpperLimit);
			defectsByResolutionMorethanUpperLimit.add(metricsAfterUpperLimit);
			fixedDefectsByResolution.put("Range"+(resolCount+1), defectsByResolutionMorethanUpperLimit);
			if(!fixedDefectsByResolution.isEmpty()){
				aggregation.setDefectsByResolutionDetails(fixedDefectsByResolution);
			}
	}
	private void processDefectsByDefectAge(DefectAggregation aggregation,Scope scopeProject){
		List<Integer> defectAgeList= new ArrayList<Integer>();
		for (int i=0;i<featureSettings.getDefectAge().length;i++){
			try{
				if(!defectAgeList.contains(featureSettings.getDefectAge()[i]))
					defectAgeList.add(Integer.parseInt(featureSettings.getDefectAge()[i]));
			}catch (Exception e) {
				LOGGER.debug(e.getMessage());
			}
		}
		Collections.sort(defectAgeList);
		Map<String, List<Map<String,String>>> openDefectsByAge=new LinkedHashMap<String, List<Map<String,String>>>();
		
		boolean firstIndex=true;
		int resolCount=defectAgeList.size();
		Set<String> defectPriorities=null!=aggregation.getDefectsByProirity()?aggregation.getDefectsByProirity().keySet():null;
		//If defect priority set is null, then there are no defects in that particular project. So no need to show the defects by age. 
		if(null==defectPriorities){
			return;
		}
		for(int i=0;i<resolCount;i++){
			Map<String,String> metric= new HashMap<String,String>();
			if(firstIndex){
				String key="lessthan"+defectAgeList.get(i)+"days";
				firstIndex=false;
				List<Map<String,String>> defectsByAge= new ArrayList<Map<String,String>>();
				for(String priorityKey: defectPriorities){
					metric.put(priorityKey, (String.valueOf(defectRepository.count(QDefect.defect.defectAge.lt(defectAgeList.get(i)+1).and(QDefect.defect.defectPriority.equalsIgnoreCase(priorityKey)).and(QDefect.defect.projectId.equalsIgnoreCase(scopeProject.getpId()).and(QDefect.defect.defectStatus.equalsIgnoreCase(IN_PROGRESS)))))));
				}
				metric.put("Defect Age Strategy", key);
				defectsByAge.add(metric);
				openDefectsByAge.put("Range"+(i+1), defectsByAge);
			}else{
				String key="between"+(defectAgeList.get(i-1)+1)+"to"+defectAgeList.get(i)+"days";
				List<Map<String,String>> defectsByAge= new ArrayList<Map<String,String>>();
				for(String priorityKey: defectPriorities){
					
					metric.put(priorityKey, (String.valueOf(defectRepository.count(QDefect.defect.defectAge.between(defectAgeList.get(i-1)+1 ,defectAgeList.get(i) ).and(QDefect.defect.defectPriority.equalsIgnoreCase(priorityKey)).and(QDefect.defect.projectId.equalsIgnoreCase(scopeProject.getpId()).and(QDefect.defect.defectStatus.equalsIgnoreCase(IN_PROGRESS)))))));
					
				}
				metric.put("Defect Age Strategy", key);
				defectsByAge.add(metric);
				
				openDefectsByAge.put("Range"+(i+1), defectsByAge);
				}
			
			}
			String keyAfterUpperLimit="morethan"+defectAgeList.get(resolCount-1)+"days";
			List<Map<String,String>> defectsByAgeMorethanUpperLimit= new ArrayList<Map<String,String>>();
			Map<String,String> metricsAfterUpperLimit= new HashMap<String,String>();
			for(String priorityKey: defectPriorities){
				metricsAfterUpperLimit.put(priorityKey, (String.valueOf(defectRepository.count(QDefect.defect.defectAge.gt(defectAgeList.get(resolCount-1)).and(QDefect.defect.defectPriority.equalsIgnoreCase(priorityKey)).and(QDefect.defect.projectId.equalsIgnoreCase(scopeProject.getpId()).and(QDefect.defect.defectStatus.equalsIgnoreCase(IN_PROGRESS)))))));
			}
			metricsAfterUpperLimit.put("Defect Age Strategy", keyAfterUpperLimit);
			defectsByAgeMorethanUpperLimit.add(metricsAfterUpperLimit);
			openDefectsByAge.put("Range"+(resolCount+1), defectsByAgeMorethanUpperLimit);
			if(!openDefectsByAge.isEmpty()){
				aggregation.setDefectsByAgeDetails(openDefectsByAge);
			}
	}
	public String getSprintDefectsFound(String projectId, String startdate, String enddate) {
		String query = String.format(GET_DEFECTS_CREATED, projectId, startdate,enddate);
		query=featureSettings.getJiraBaseUrl()+query;
		HttpEntity<String> entity = new HttpEntity<String>(getHeader());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(query
				, HttpMethod.GET, entity, String.class);
		return result.getBody();
	}

	public String getSprintDefectResolved(String projectId, String startdate,String enddate) {
		String query = String.format(GET_DEFECTS_RESOLVED, projectId, startdate, enddate);
		query=featureSettings.getJiraBaseUrl()+query;
		HttpEntity<String> entity = new HttpEntity<String>(getHeader());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(query
				, HttpMethod.GET, entity, String.class);
		return result.getBody();
	}

	public String getSprintDefectUnresolved(String projectId, String enddate) {
		String query = String.format(GET_DEFECTS_UNRESOLVED, projectId, enddate, enddate);
		query=featureSettings.getJiraBaseUrl()+query;
		HttpEntity<String> entity = new HttpEntity<String>(getHeader());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(query
				, HttpMethod.GET, entity, String.class);
		return result.getBody();
	}
	
	
}

