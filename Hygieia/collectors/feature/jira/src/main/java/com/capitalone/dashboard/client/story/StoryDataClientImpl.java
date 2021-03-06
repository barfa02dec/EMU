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

import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
import com.capitalone.dashboard.model.JiraVersion;
import com.capitalone.dashboard.model.QRelease;
import com.capitalone.dashboard.model.QSprint;
import com.capitalone.dashboard.model.Release;
import com.capitalone.dashboard.model.Scope;
import com.capitalone.dashboard.model.Sprint;
import com.capitalone.dashboard.repository.DefectAggregationRepository;
import com.capitalone.dashboard.repository.DefectRepository;
import com.capitalone.dashboard.repository.FeatureCollectorRepository;
import com.capitalone.dashboard.repository.FeatureRepository;
import com.capitalone.dashboard.repository.ReleaseRepository;
import com.capitalone.dashboard.repository.SprintRepository;
import com.capitalone.dashboard.repository.TeamRepository;
import com.capitalone.dashboard.util.ClientUtil;
import com.capitalone.dashboard.util.CoreFeatureSettings;
import com.capitalone.dashboard.util.DateUtil;
import com.capitalone.dashboard.util.FeatureCollectorConstants;
import com.capitalone.dashboard.util.JiraCollectorUtil;
import com.capitalone.dashboard.util.NewFeatureSettings;

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
	private static final String BUG = "Bug";

	// works with ms too (just ignores them)
	private final DateFormat SETTINGS_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	private final NewFeatureSettings featureSettings;
	private final FeatureRepository featureRepo;
	private final DefectRepository defectRepository;
	private final SprintRepository sprintRepository;
	private final DefectAggregationRepository defectAggregationRepository;
	private final FeatureCollectorRepository featureCollectorRepository;
	private final JiraClient jiraClient;
	private final ReleaseRepository releaseRepository;

	// epicId : list of epics
	private Map<String, Issue> epicCache;
	private Set<String> todoCache;
	private Set<String> inProgressCache;
	private Set<String> doneCache;

	/**
	 * Extends the constructor from the super class.
	 */
	public StoryDataClientImpl(CoreFeatureSettings coreFeatureSettings, NewFeatureSettings featureSettings,
			FeatureRepository featureRepository, DefectRepository defectRepository, SprintRepository sprintRepository,
			DefectAggregationRepository defectAggregationRepository, ReleaseRepository releaseRepository,
			FeatureCollectorRepository featureCollectorRepository, TeamRepository teamRepository,
			JiraClient jiraClient) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing data collection for the feature widget, story-level data...");
		}

		this.featureSettings = featureSettings;
		this.featureRepo = featureRepository;
		this.featureCollectorRepository = featureCollectorRepository;
		this.jiraClient = jiraClient;
		this.defectRepository = defectRepository;
		this.sprintRepository = sprintRepository;
		this.defectAggregationRepository = defectAggregationRepository;
		this.epicCache = new HashMap<>();
		this.releaseRepository = releaseRepository;
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
			List<Issue> issues = jiraClient.getIssues(startTime, i, featureSettings);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Story information query took " + (System.currentTimeMillis() - queryStart) + " ms");
			}

			if (issues != null && !issues.isEmpty()) {
				updateMongoInfo(issues);
				count += issues.size();
			}

			LOGGER.info("Loop i " + i + " pageSize " + issues.size());

			if (issues == null || issues.size() < pageSize) {
				hasMore = false;
				break;
			}
		}

		return count;
	}
	
	public int updateJiraDefectInfo() {

		epicCache.clear();
		int pageSize = jiraClient.getPageSize();

		boolean hasMore = true;
		int issuecount = 0;
		for (int i = 0; hasMore; i += pageSize) {
			
			if (LOGGER.isDebugEnabled()) 
				LOGGER.debug("Obtaining story information starting at index " + i + "...");
			
			long queryStart = System.currentTimeMillis();
			List<Issue> issues = jiraClient.getIssuesPMD(i, featureSettings);

			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Story information query took " + (System.currentTimeMillis() - queryStart) + " ms");

			if (!CollectionUtils.isEmpty(issues)) {
				updateMongoInfo(issues);
				issuecount += issues.size();
			}

			LOGGER.info("Loop i " + i + " pageSize " + issues.size());

			if (issues == null || issues.size() < pageSize) {
				hasMore = false;
				break;
			}
		}
		return issuecount;
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
						Defect defect = defectRepository.findByDefectId(TOOLS.sanitizeResponse(issue.getId()));
						if (null == defect) {
							defect = new Defect();
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
	
	public void saveDetailedSprintData(String projectId, String projectName) throws JSONException {
		LOGGER.info("Collection of Sprint data started for Project ID : " + projectId + " and Project Name: " + projectName);
		
		List<JiraSprint> jiraSprints = JiraCollectorUtil.getSprintList(projectId, featureSettings);

		Collections.reverse(jiraSprints); 
		if(jiraSprints.size() > 30)
			jiraSprints.stream().limit(30);
		
		int count = 0;		
		for (JiraSprint jiraSprint : jiraSprints) {
			try{
				//Sprint sprint = sprintRepository.findOne(QSprint.sprint.sprintId.eq(jiraSprint.getId()).and(QSprint.sprint.name.eq(jiraSprint.getName())));
				Sprint sprint = sprintRepository.findOne(QSprint.sprint.projectId.eq(featureSettings.getProjectId()).and(QSprint.sprint.sprintId.eq(jiraSprint.getId())));
				
				if (null == sprint) {
					sprint = new Sprint();
					sprint.setSid(jiraSprint.getId());
					sprint.setName(jiraSprint.getName());
					sprint.setViewBoardsUrl(jiraSprint.getViewBoardsUrl());
					sprint.setJiraProjectId(Long.parseLong(projectId));
					sprint.setProjectId(featureSettings.getProjectId());
					sprint.setStartDate(StringUtils.isEmpty(jiraSprint.getStart()) ? null : new Date(Long.parseLong(jiraSprint.getStart())));
					sprint.setEndDate(StringUtils.isEmpty(jiraSprint.getEnd()) ? null : new Date(Long.parseLong(jiraSprint.getEnd())));
					sprint.setClosed(jiraSprint.getClosed());
					sprint.setProjectName(projectName);
					sprint.setCreatedOn(new Date());
					sprint.setAutomated(true);
				}else{
					sprint.setEditable(jiraSprint.getEditable());
					sprint.setEndDate(StringUtils.isEmpty(jiraSprint.getEnd()) ? null : new Date(Long.parseLong(jiraSprint.getEnd())));
					sprint.setUpdatedOn(new Date());
				}
				
				// Get the detailed metrics for sprint with status [open] 
				// Get the detailed metrics for sprint with status[closed] but detailed metrics/SprintData is null
				if(((sprint.getClosed() && null == sprint.getSprintData()) ||!sprint.getClosed())){
					JiraCollectorUtil.getSprintMetrics(jiraSprint, projectId, featureSettings);
					sprint.setSprintData(jiraSprint.getSprintData());
					sprint.setClosed(jiraSprint.getClosed());
				}
				
				if(sprint.getSprintData() != null){
					sprint.setStartDate(sprint.getSprintData().getStartDate());
					
					if(sprint.getSprintData().getCompleteDate() != null)
						sprint.setEndDate(sprint.getSprintData().getCompleteDate());
					else
						sprint.setEndDate(sprint.getSprintData().getEndDate());
				}
				
				sprintRepository.save(sprint);
				
				if(++count > 30) break;
				
			}catch(Exception ex){
				ex.printStackTrace();
				LOGGER.error("Collection of Sprint data failed for Project ID : " + projectId + " and Project Name: " + projectName + " and sprint " + jiraSprint.getName() , ex);
			}
		}
		
		LOGGER.info("Collection of Sprint data completed for Project ID : " + projectId + " and Project Name: " + projectName);
	}
	
	public void saveDetailedReleaseData(String projectId, String projectName) {
		LOGGER.info("Collection of release data started for Project ID : " + projectId + " and Project Name: " + projectName);
		
		List<JiraVersion> jiraVersions = JiraCollectorUtil.getVersionsFromJira(projectId, featureSettings);

		int count = 0;
		for (JiraVersion jiraVersion : jiraVersions) {
			try{
				//Release release = releaseRepository.findOne(QRelease.release.releaseId.eq(jiraVersion.getId()).and(QRelease.release.name.eq(jiraVersion.getName())));
				Release release = releaseRepository.findOne(QRelease.release.projectId.eq(featureSettings.getProjectId()).and(QRelease.release.releaseId.eq(jiraVersion.getId())));
				
				if (release == null) {
					release = new Release();
					release.setReleaseId(jiraVersion.getId());
					release.setProjectId(featureSettings.getProjectId());
					release.setJiraProjectId(projectId);
					release.setName(jiraVersion.getName());
					release.setStartDate(StringUtils.isEmpty(jiraVersion.getStartDate()) ? null : new SimpleDateFormat("yyyy-MM-dd").parse(jiraVersion.getStartDate()));
					release.setReleaseDate(StringUtils.isEmpty(jiraVersion.getReleaseDate()) ? null : new SimpleDateFormat("yyyy-MM-dd").parse(jiraVersion.getReleaseDate()));
					release.setReleased(jiraVersion.getReleased());
					release.setProjectName(projectName);
					release.setCreatedOn(new Date());
					release.setAutomated(true);
					release.setArchived(jiraVersion.getArchived());
				}else{
					release.setOverdue(jiraVersion.getOverdue());
					release.setReleaseDate(StringUtils.isEmpty(jiraVersion.getReleaseDate()) ? null : new SimpleDateFormat("yyyy-MM-dd").parse(jiraVersion.getReleaseDate()));
					release.setUpdatedOn(new Date());
				}
				
				// Get the detailed version metrics for release with status [not release] 
				// Get the detailed version metrics for release with status[released] but detailed metrics/versionData is null
				if (((release.getReleased() && null == release.getVersionData())|| !release.getReleased())  
						&& (release.getArchived() == null || (release.getArchived() != null && !release.getArchived())) ) {  //&&(release.getArchived() == null || (release.getArchived() != null && !release.getArchived()))
					String versionDetails = JiraCollectorUtil.getVersionDetailsFromJira(jiraVersion.getId(), featureSettings);
					if (versionDetails == null) continue;
					
					// setting the detailed metrics to release.
					jiraVersion.setVersionData(JiraCollectorUtil.getReleaseData(versionDetails, projectId, featureSettings));
					release.setVersionData(jiraVersion.getVersionData());
					release.setReleased(jiraVersion.getReleased());
				}
				releaseRepository.save(release);
				if(++count > 30) break;
			}catch(Exception ex){
				ex.printStackTrace();
				LOGGER.error("Collection of release data failed for Project ID : " + projectId + " and Project Name: " + projectName + jiraVersion.getName() , ex);
			}
		}
		LOGGER.info("Collection of release data completed for Project ID : " + projectId + " and Project Name: " + projectName);
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
			List<Feature> response = featureRepo.findTopByCollectorIdAndChangeDateGreaterThanOrderByChangeDateDesc(
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
		return DateUtil.toISODateRealTimeFormat(
				DateUtil.getDatePriorToMinutes(DateUtil.fromISODateTimeFormat(changeDateISO), priorMinutes));
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

	private Defect processDefects(Issue issue, Defect defect, Map<String, IssueField> fields) {

		defect.setDefectId(TOOLS.sanitizeResponse(issue.getId()));
		defect.setProjectId(issue.getProject().getId().toString());
		defect.setProjectName(issue.getProject().getName());
		defect.setDefectStatus(this.toCanonicalFeatureStatus(issue.getStatus().getName()));
		defect.setDefectDescription(issue.getSummary());
		defect.setDefectPriority(null != issue.getPriority() ? issue.getPriority().getName() : null);
		//defect.setCreationDate(issue.getCreationDate().toString());
		defect.setCreatedBy(null != issue.getReporter() ? issue.getReporter().getName() : null);
		
		int originalEstimate = 0;
		if (issue.getTimeTracking() != null && issue.getTimeTracking().getOriginalEstimateMinutes() != null) {
			originalEstimate = issue.getTimeTracking().getOriginalEstimateMinutes();
		} else if (fields.get("aggregatetimeoriginalestimate") != null
				&& fields.get("aggregatetimeoriginalestimate").getValue() != null) {
			// this value is in seconds
			originalEstimate = ((Integer) fields.get("aggregatetimeoriginalestimate").getValue()) / 60;
		}
		
		defect.setOriginalEstimate(originalEstimate);
		defect.setDefectResolutionStatus(null != issue.getResolution() ? issue.getResolution().getName() : null);
		defect.setReporter(null != issue.getReporter() ? issue.getReporter().toString() : null);
		//defect.setUpdateDate(null != issue.getUpdateDate() ? issue.getUpdateDate().toString() : null);
		defect.setAssignee(null != issue.getAssignee() ? issue.getAssignee().getName().toString() : null);
		
		// calculate the defect age in days
		Date createdDate = DateUtil.fromISODateTimeFormat(issue.getCreationDate().toString());
		Date updateDate = DateUtil.fromISODateTimeFormat(issue.getUpdateDate().toString());
		Date now = new Date();
		if (null != defect.getDefectResolutionStatus() && defect.getDefectResolutionStatus().equals(DONE)) {
			defect.setDefectResolutionInDays(DateUtil.differenceInDays(updateDate, createdDate));
			defect.setDefectAge(defect.getDefectResolutionInDays());
		}
		if (null == defect.getDefectResolutionStatus()) {
			defect.setDefectAge(DateUtil.differenceInDays(now, createdDate));
		}
		
		defect.setEmuProjectId(featureSettings.getProjectId());
		return defect;
	}

	@Override
	public void defectMetricsAggregation(Scope project) {
		
		 //For a single project, there is always a single aggregator exists.
		 //hence setting the collector ID as same as scope ID.
		 
		LOGGER.info("Collection of defect data started for Project ID : " + project.getProjectId() + " and Project Name: " + project.getName());
		
		//DefectAggregation summary = defectAggregationRepository.findByProjectIdAndName(project.getProjectId(), project.getName());
		DefectAggregation summary = defectAggregationRepository.findByProjectIdAndJiraId(project.getProjectId(), project.getpId());
		
		if (null == summary) {
			summary = new DefectAggregation();
		}

		summary.setProjectId(project.getpId());
		summary.setProjectName(project.getName());
		summary.setValuesAsOn(new Date().toString());
		summary.setCreatedOn(new Date());
		summary.setAutomated(true);
		summary.setMetricsProjectId(project.getProjectId());
		
		/*
		 * Logic to priorities used in Jira
		 */
		getJiraPriority(featureSettings);
		
		/*
		 * Logic to bucket the defects based on priority.
		 */
		//processDefectsByPriority(opendefects, summary, project);
		processDefectsByPriority(summary, featureSettings);
		
		/*
		 * Logic to bucket the defects based on environment
		 */
		processDefectsByEnvironment(summary, featureSettings);
		
		/*
		 * Logic for bucketing the defects based on resolution days and priority
		 * in each class of resolution.
		 */
		resolutionTimeForClosedDefects(summary);

		/*
		 * Logic for bucketing the defects based on age of open defects.
		 */
		ageOfOpenDefects(summary);

		defectAggregationRepository.save(summary);
		
		LOGGER.info("Collection of defect data completed for Project ID : " + project.getProjectId() + " and Project Name: " + project.getName());
	}

	public static void getJiraPriority (NewFeatureSettings featureSettings){
		featureSettings.setIssuePriorities(JiraCollectorUtil.getJiraPriority(featureSettings));
	}

	public static void processDefectsByEnvironment(DefectAggregation aggregation, NewFeatureSettings featureSettings){
		try{
			List<JiraIssue> issues = JiraCollectorUtil.getOpenDefectsByProject(featureSettings);
			Map<String,Integer> defectsByEnvironment= new LinkedHashMap<String,Integer>();
				
				for(JiraIssue defect: issues){
					String environment = StringUtils.isEmpty(defect.getEnvironment()) ? null :  defect.getEnvironment().replace(".", "_");
					
					if(null != environment){
						if(defectsByEnvironment.containsKey(environment))
						{
							defectsByEnvironment.put(environment, defectsByEnvironment.get(environment)+1);
						}else{
							defectsByEnvironment.put(environment, 1);
						}
					}
				}
				if(!defectsByEnvironment.isEmpty()){
					aggregation.setDefectsByEnvironment(defectsByEnvironment);
				}
		}catch (Exception e) {
			LOGGER.debug(e.getMessage());
		}
	}

	public static void processDefectsByPriority(DefectAggregation aggregation, NewFeatureSettings featureSettings){
		try{
			List<JiraIssue> issues = JiraCollectorUtil.getOpenDefectsByProject(featureSettings);
			Map<String,Integer> defectsByPriority= new LinkedHashMap<String,Integer>();
				
				for(JiraIssue defect: issues){
					if(null != defect.getSeverity()){
						if(defectsByPriority.containsKey(defect.getSeverity()))
						{
							defectsByPriority.put(defect.getSeverity(), defectsByPriority.get(defect.getSeverity())+1);
						}else{
							defectsByPriority.put(defect.getSeverity(), 1);
						}
					}
				}
				if(!defectsByPriority.isEmpty()){
					aggregation.setDefectsByProirity(defectsByPriority);
				}
		}catch (Exception e) {
			LOGGER.debug(e.getMessage());
		}
	}

	private void resolutionTimeForClosedDefects(DefectAggregation aggregation) {
		List<JiraIssue> issues = JiraCollectorUtil.getClosedDefectsByProject(featureSettings);
		
		if(CollectionUtils.isEmpty(issues)) 
			return;

		List<Integer> resolutionsPeriodRange = new ArrayList<Integer>();
		for (int i = 0; i < featureSettings.getResolutionPeriod().length; i++) {
			try {
				if (!resolutionsPeriodRange.contains(featureSettings.getResolutionPeriod()[i]))
					resolutionsPeriodRange.add(Integer.parseInt(featureSettings.getResolutionPeriod()[i]));
			} catch (Exception e) {
				LOGGER.debug(e.getMessage());
			}
		}
		
		Collections.sort(resolutionsPeriodRange);
		Map<String, List<Map<String, String>>> fixedDefectsByResolution = new LinkedHashMap<String, List<Map<String, String>>>();
		
		boolean firstIndex = true;
		int rangeCount = resolutionsPeriodRange.size();
		
		List<String> defectPriorities = featureSettings.getIssuePriorities();
	
		if (null == defectPriorities) {
			return;
		 }
		
		for (int i = 0; i < rangeCount; i++) {
			Map<String, String> metric = new HashMap<String, String>();
			int upperBound = resolutionsPeriodRange.get(i);
			
			if (firstIndex) {
				firstIndex = false;
				String key = "days <=" + resolutionsPeriodRange.get(i);
				List<Map<String, String>> defectCountByPriority = new ArrayList<Map<String, String>>();

				for (String priorityKey : defectPriorities) {
						metric.put(priorityKey, ""+issues.stream().filter(issue-> DateUtil.differenceInDays(DateUtil.fromISODateTimeFormat(issue.getResolutionDate()), DateUtil.fromISODateTimeFormat(issue.getCreateDate()))<= upperBound).filter(issue-> issue.getSeverity().equals(priorityKey)).count());
				}
				metric.put("Resolution Strategy", key);
				
				defectCountByPriority.add(metric);
				fixedDefectsByResolution.put("Range" + (i + 1), defectCountByPriority);
				
			} else {
				int lowerBound = resolutionsPeriodRange.get(i-1);
				String key = (resolutionsPeriodRange.get(i - 1)) + "< days <=" + resolutionsPeriodRange.get(i);
				
				List<Map<String, String>> defectCountByPriority = new ArrayList<Map<String, String>>();
				for (String priorityKey : defectPriorities) {
					metric.put(priorityKey, "" + issues.stream().filter(issue-> DateUtil.differenceInDays( DateUtil.fromISODateTimeFormat(issue.getResolutionDate()), DateUtil.fromISODateTimeFormat(issue.getCreateDate()))> lowerBound).filter(issue-> DateUtil.differenceInDays(DateUtil.fromISODateTimeFormat(issue.getResolutionDate()), DateUtil.fromISODateTimeFormat(issue.getCreateDate()))<= upperBound).filter(issue-> issue.getSeverity().equals(priorityKey)).count());
				}
				metric.put("Resolution Strategy", key);
				defectCountByPriority.add(metric);

				fixedDefectsByResolution.put("Range" + (i + 1), defectCountByPriority);
			}
		}
		
		String keyAfterUpperLimit = "days >" + resolutionsPeriodRange.get(rangeCount - 1);
		int beyondUpperBound = resolutionsPeriodRange.get(rangeCount - 1);
		
		List<Map<String, String>> defectCountByPriority = new ArrayList<Map<String, String>>();
		
		Map<String, String> metricsAfterUpperLimit = new HashMap<String, String>();
		for (String priorityKey : defectPriorities) {
			metricsAfterUpperLimit.put(priorityKey,"" + issues.stream().filter(issue-> DateUtil.differenceInDays(DateUtil.fromISODateTimeFormat(issue.getResolutionDate()), DateUtil.fromISODateTimeFormat(issue.getCreateDate()))> beyondUpperBound).filter(issue-> issue.getSeverity().equals(priorityKey)).count());
		}
		metricsAfterUpperLimit.put("Resolution Strategy", keyAfterUpperLimit);
		defectCountByPriority.add(metricsAfterUpperLimit);
		fixedDefectsByResolution.put("Range" + (rangeCount + 1), defectCountByPriority);
		
		if (!fixedDefectsByResolution.isEmpty()) {
			aggregation.setDefectsByResolutionDetails(fixedDefectsByResolution);
		}
	}
	
	
	private void ageOfOpenDefects(DefectAggregation aggregation) {
		
		List<Integer> defectAgePeriodRanges = new ArrayList<Integer>();
		for (int i = 0; i < featureSettings.getDefectAge().length; i++) {
			try {
				if (!defectAgePeriodRanges.contains(featureSettings.getDefectAge()[i]))
					defectAgePeriodRanges.add(Integer.parseInt(featureSettings.getDefectAge()[i]));
			} catch (Exception e) {
				LOGGER.debug(e.getMessage());
			}
		}
		
		Collections.sort(defectAgePeriodRanges);
		
		Map<String, List<Map<String, String>>> openDefectsByAge = new LinkedHashMap<String, List<Map<String, String>>>();
		int rangeCount = defectAgePeriodRanges.size();
		List<String> defectPriorities = featureSettings.getIssuePriorities();
		List<JiraIssue> issues = JiraCollectorUtil.getOpenDefectsByProject(featureSettings);

		// If defect priority set is null, then there are no defects in that
		// particular project. So no need to show the defects by age.
		if (CollectionUtils.isEmpty(defectPriorities) || CollectionUtils.isEmpty(issues)) {
			return;
		}
		
		boolean firstIndex = true;
		for (int i = 0; i < rangeCount; i++) {
			Map<String, String> metric = new HashMap<String, String>();
			int upperBound = defectAgePeriodRanges.get(i);
			
			if (firstIndex) {
				String key = "days <" + defectAgePeriodRanges.get(i);
				firstIndex = false;
				List<Map<String, String>> defectsByAge = new ArrayList<Map<String, String>>();
				for (String priorityKey : defectPriorities) {
					metric.put(priorityKey, "" + issues.stream().filter(issue-> DateUtil.differenceInDays(new Date(), DateUtil.fromISODateTimeFormat(issue.getCreateDate()))<= upperBound).filter(issue-> issue.getSeverity().equals(priorityKey)).count());
				}
				metric.put("Defect Age Strategy", key);
				defectsByAge.add(metric);
				openDefectsByAge.put("Range" + (i + 1), defectsByAge);
			} else {
				int lowerBound = defectAgePeriodRanges.get(i-1);
				String key =  (defectAgePeriodRanges.get(i - 1)) + "< days <" + defectAgePeriodRanges.get(i) ;
				List<Map<String, String>> defectsByAge = new ArrayList<Map<String, String>>();
				for (String priorityKey : defectPriorities) {
					metric.put(priorityKey, "" + issues.stream().filter(issue-> DateUtil.differenceInDays( new Date(), DateUtil.fromISODateTimeFormat(issue.getCreateDate()))> lowerBound).filter(issue-> DateUtil.differenceInDays(new Date(), DateUtil.fromISODateTimeFormat(issue.getCreateDate()))<= upperBound).filter(issue-> issue.getSeverity().equals(priorityKey)).count());
				}
				metric.put("Defect Age Strategy", key);
				defectsByAge.add(metric);

				openDefectsByAge.put("Range" + (i + 1), defectsByAge);
			}
		}
		
		String keyAfterUpperLimit = "days >" + defectAgePeriodRanges.get(rangeCount - 1);
		int beyondUpperBound = defectAgePeriodRanges.get(rangeCount - 1);
		
		List<Map<String, String>> defectsByAgeMorethanUpperLimit = new ArrayList<Map<String, String>>();
		Map<String, String> metricsAfterUpperLimit = new HashMap<String, String>();
		for (String priorityKey : defectPriorities) {
			metricsAfterUpperLimit.put(priorityKey,"" + issues.stream().filter(issue-> DateUtil.differenceInDays(new Date(), DateUtil.fromISODateTimeFormat(issue.getCreateDate()))> beyondUpperBound).filter(issue-> issue.getSeverity().equals(priorityKey)).count());
		}
		metricsAfterUpperLimit.put("Defect Age Strategy", keyAfterUpperLimit);
		defectsByAgeMorethanUpperLimit.add(metricsAfterUpperLimit);
		openDefectsByAge.put("Range" + (rangeCount + 1), defectsByAgeMorethanUpperLimit);
		if (!openDefectsByAge.isEmpty()) {
			aggregation.setDefectsByAgeDetails(openDefectsByAge);
		}
	}
}
