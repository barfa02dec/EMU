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

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.capitalone.dashboard.client.JiraClient;
import com.capitalone.dashboard.model.Defect;
import com.capitalone.dashboard.model.DefectAggregation;
import com.capitalone.dashboard.model.Feature;
import com.capitalone.dashboard.model.FeatureStatus;
import com.capitalone.dashboard.model.JiraSprint;
import com.capitalone.dashboard.model.JiraVersion;
import com.capitalone.dashboard.model.QDefect;
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

			// will result in an extra call if number of results == pageSize
			// but I would rather do that then complicate the jira client
			// implementation
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

	public void saveDetailedSprintData(String projectId) {
		List<JiraSprint> sprintsJira = JiraCollectorUtil.getSprintList(projectId, featureSettings.getJiraBaseUrl(),
		featureSettings.getJiraCredentials(), featureSettings.getNoOfSprintsToShow());
		
		List<Sprint> list = new ArrayList<Sprint>();
		for (JiraSprint js : sprintsJira) {
			Sprint sprint = sprintRepository.findOne(QSprint.sprint.sid.eq(js.getId()));
			if (null == sprint) {
				
				sprint = new Sprint();
				sprint.setSid(js.getId());
				sprint.setName(js.getName());
				sprint.setViewBoardsUrl(js.getViewBoardsUrl());
				sprint.setProjectId(projectId);
				sprint.setStart(js.getStart());
				sprint.setClosed(js.getClosed());

			}
			// get the detailed metrics for sprint with status [open] 
			// get the detailed metrics for sprint with status[closed] but detailed metrics/SprintData is null
						
			if(((sprint.getClosed() && null==sprint.getSprintData())||!sprint.getClosed())){
				JiraCollectorUtil.getRecentSprintMetrics(js, projectId,
						featureSettings.getJiraBaseUrl(), featureSettings.getJiraCredentials(),
						featureSettings.getRapidView());
				sprint.setSprintData(js.getSprintData());
			}
			sprint.setEditable(js.getEditable());
			sprint.setEnd(js.getEnd());
			list.add(sprint);
		}
		if (!list.isEmpty()) {
			sprintRepository.save(list);
		}

	}

	public void saveDetailedReleaseData(String projectId) {

		List<JiraVersion> jiraVersions = JiraCollectorUtil.getVersionsFromJira(projectId,
				featureSettings.getJiraBaseUrl(), featureSettings.getJiraCredentials(), featureSettings.getRapidView(),
				featureSettings.getNoOfSprintsToShow());

		List<Release> releaseList = new ArrayList<Release>();

		for (JiraVersion jv : jiraVersions) {
			Release release = releaseRepository.findOne(QRelease.release.releaseId.eq(jv.getId()));
			if (release == null) {
				release = new Release();
				release.setReleaseId(jv.getId());
				release.setProjectId(projectId);
				release.setName(jv.getName());
				release.setStartDate(jv.getStartDate());
				release.setReleased(jv.getReleased());
			}
			// get the detailed metrics for release with status [not release] 
			// get the detailed metrics for release with status[released] but detailed metrics/versionData is null
			
			if ((release.getReleased() && null == release.getVersionData())|| !release.getReleased()) {
				String detailedVersionMetrics = JiraCollectorUtil.getVersionDetailsFromJira(
						featureSettings.getRapidView(), jv.getId(), featureSettings.getJiraBaseUrl(),
						featureSettings.getJiraCredentials());
				jv.setVersionData(JiraCollectorUtil.getReleaseData(detailedVersionMetrics, projectId,
						featureSettings.getJiraBaseUrl(), featureSettings.getJiraCredentials(),
						featureSettings.getRapidView()));
				// setting the detailed metrics to release.
				release.setVersionData(jv.getVersionData());

			}

			release.setOverdue(jv.getOverdue());
			release.setReleaseDate(jv.getReleaseDate());
			releaseList.add(release);
		}
		if (!jiraVersions.isEmpty()) {
			releaseRepository.save(releaseList);
		}

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

		/*
		 * setting the collector ID as multiple of defect ID and project ID to
		 * avoid the creation of duplicate Defects. There are scenarios where
		 * different projects will be same / defect ID will be same w.r.t
		 * different JIRA's
		 */
		defect.setCollectorId(issue.getId() * issue.getProject().getId());
		defect.setDefectId(TOOLS.sanitizeResponse(issue.getId()));
		defect.setProjectId(issue.getProject().getId().toString());
		defect.setProjectName(issue.getProject().getName());
		defect.setDefectStatus(this.toCanonicalFeatureStatus(issue.getStatus().getName()));
		defect.setDefectDescription(issue.getSummary());
		defect.setDefectPriority(null != issue.getPriority() ? issue.getPriority().getName() : null);
		defect.setCreationDate(issue.getCreationDate().toString());
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
		defect.setUpdateDate(null != issue.getUpdateDate() ? issue.getUpdateDate().toString() : null);
		defect.setAssignee(null != issue.getAssignee() ? issue.getAssignee().getName().toString() : null);
		// defect age
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
		return defect;

	}

	private DefectAggregation processDefectsSummary(NewFeatureSettings featureSettings, List<Defect> defects,
			Scope scopeProject, DefectAggregation summery) {

		/*
		 * Logic to bucket the defects based on environment and priority.
		 */
		JiraCollectorUtil.processDefectsByPriorityAndEnvironment(defects, summery, scopeProject);

		/*
		 * Logic for bucketing the defects based on resolution days and priority
		 * in each class of resolution.
		 */
		processDefectsByDefectResolutionPeriod(summery, scopeProject);

		/*
		 * Logic for bucketing the defects based on age of open defects.
		 */
		processDefectsByDefectAge(summery, scopeProject);

		return summery;
	}

	@Override
	public void processDefectAggregation(NewFeatureSettings featureSettings, List<Defect> defectsInDB,
			Scope scopeProject) {
		/*
		 * For a single project, there is always a single aggregater
		 * exists.Hence setting the collector ID as same as scope ID.
		 */
		LOGGER.info("processing Defects aggregation");
		DefectAggregation summery = defectAggregationRepository.findByProjectId(scopeProject.getpId());
		if (null == summery) {
			summery = new DefectAggregation();
		}
		summery = processDefectsSummary(featureSettings, defectsInDB, scopeProject, summery);
		summery.setProjectId(scopeProject.getpId());
		summery.setProjectName(scopeProject.getName());
		summery.setValuesAsOn(new Date().toString());
		defectAggregationRepository.save(summery);
		LOGGER.info("Defects aggregation ends.");
	}

	private void processDefectsByDefectResolutionPeriod(DefectAggregation aggregation, Scope scopeProject) {
		List<Integer> resolutionsList = new ArrayList<Integer>();
		for (int i = 0; i < featureSettings.getResolutionPeriod().length; i++) {
			try {
				if (!resolutionsList.contains(featureSettings.getResolutionPeriod()[i]))
					resolutionsList.add(Integer.parseInt(featureSettings.getResolutionPeriod()[i]));
			} catch (Exception e) {
				LOGGER.debug(e.getMessage());
			}
		}
		Collections.sort(resolutionsList);
		Map<String, List<Map<String, String>>> fixedDefectsByResolution = new LinkedHashMap<String, List<Map<String, String>>>();

		boolean firstIndex = true;
		int resolCount = resolutionsList.size();
		Set<String> defectPriorities = null != aggregation.getDefectsByProirity()
				? aggregation.getDefectsByProirity().keySet() : null;
		// If defect priority set is null, then there are no defects in that
		// particular project. So no need to show the defects by resolution.
		if (null == defectPriorities) {
			return;
		}
		for (int i = 0; i < resolCount; i++) {
			Map<String, String> metric = new HashMap<String, String>();
			if (firstIndex) {
				String key = "<" + resolutionsList.get(i);
				firstIndex = false;
				List<Map<String, String>> defectsByResolution = new ArrayList<Map<String, String>>();
				for (String priorityKey : defectPriorities) {
					metric.put(priorityKey,
							(String.valueOf(defectRepository
									.count(QDefect.defect.defectResolutionInDays.lt(resolutionsList.get(i) + 1)
											.and(QDefect.defect.defectPriority.equalsIgnoreCase(priorityKey))
											.and(QDefect.defect.projectId.equalsIgnoreCase(scopeProject.getpId())
													.and(QDefect.defect.defectStatus.equalsIgnoreCase(DONE)))))));
				}
				metric.put("Resolution Strategy", key);
				defectsByResolution.add(metric);
				fixedDefectsByResolution.put("Range" + (i + 1), defectsByResolution);
			} else {
				String key = ">" + (resolutionsList.get(i - 1) + 1) + " && <" + resolutionsList.get(i);
				List<Map<String, String>> defectsByResolution = new ArrayList<Map<String, String>>();
				for (String priorityKey : defectPriorities) {

					metric.put(priorityKey,
							(String.valueOf(defectRepository.count(QDefect.defect.defectResolutionInDays
									.between(resolutionsList.get(i - 1) + 1, resolutionsList.get(i)).and(
											QDefect.defect.defectPriority.equalsIgnoreCase(priorityKey))
									.and(QDefect.defect.projectId.equalsIgnoreCase(scopeProject.getpId())
											.and(QDefect.defect.defectStatus.equalsIgnoreCase(DONE)))))));

				}
				metric.put("Resolution Strategy", key);
				defectsByResolution.add(metric);

				fixedDefectsByResolution.put("Range" + (i + 1), defectsByResolution);
			}

		}
		String keyAfterUpperLimit = ">" + resolutionsList.get(resolCount - 1);
		List<Map<String, String>> defectsByResolutionMorethanUpperLimit = new ArrayList<Map<String, String>>();
		Map<String, String> metricsAfterUpperLimit = new HashMap<String, String>();
		for (String priorityKey : defectPriorities) {
			metricsAfterUpperLimit.put(priorityKey,
					(String.valueOf(defectRepository
							.count(QDefect.defect.defectResolutionInDays.gt(resolutionsList.get(resolCount - 1))
									.and(QDefect.defect.defectPriority.equalsIgnoreCase(priorityKey))
									.and(QDefect.defect.projectId.equalsIgnoreCase(scopeProject.getpId())
											.and(QDefect.defect.defectStatus.equalsIgnoreCase(DONE)))))));
		}
		metricsAfterUpperLimit.put("Resolution Strategy", keyAfterUpperLimit);
		defectsByResolutionMorethanUpperLimit.add(metricsAfterUpperLimit);
		fixedDefectsByResolution.put("Range" + (resolCount + 1), defectsByResolutionMorethanUpperLimit);
		if (!fixedDefectsByResolution.isEmpty()) {
			aggregation.setDefectsByResolutionDetails(fixedDefectsByResolution);
		}
	}

	private void processDefectsByDefectAge(DefectAggregation aggregation, Scope scopeProject) {
		List<Integer> defectAgeList = new ArrayList<Integer>();
		for (int i = 0; i < featureSettings.getDefectAge().length; i++) {
			try {
				if (!defectAgeList.contains(featureSettings.getDefectAge()[i]))
					defectAgeList.add(Integer.parseInt(featureSettings.getDefectAge()[i]));
			} catch (Exception e) {
				LOGGER.debug(e.getMessage());
			}
		}
		Collections.sort(defectAgeList);
		Map<String, List<Map<String, String>>> openDefectsByAge = new LinkedHashMap<String, List<Map<String, String>>>();

		boolean firstIndex = true;
		int resolCount = defectAgeList.size();
		Set<String> defectPriorities = null != aggregation.getDefectsByProirity()
				? aggregation.getDefectsByProirity().keySet() : null;
		// If defect priority set is null, then there are no defects in that
		// particular project. So no need to show the defects by age.
		if (null == defectPriorities) {
			return;
		}
		for (int i = 0; i < resolCount; i++) {
			Map<String, String> metric = new HashMap<String, String>();
			if (firstIndex) {
				String key = "<" + defectAgeList.get(i);
				firstIndex = false;
				List<Map<String, String>> defectsByAge = new ArrayList<Map<String, String>>();
				for (String priorityKey : defectPriorities) {
					metric.put(priorityKey, (String
							.valueOf(defectRepository.count(QDefect.defect.defectAge.lt(defectAgeList.get(i) + 1)
									.and(QDefect.defect.defectPriority.equalsIgnoreCase(priorityKey)).and(
											QDefect.defect.projectId.equalsIgnoreCase(scopeProject.getpId()).and(
													QDefect.defect.defectStatus.equalsIgnoreCase(IN_PROGRESS)))))));
				}
				metric.put("Defect Age Strategy", key);
				defectsByAge.add(metric);
				openDefectsByAge.put("Range" + (i + 1), defectsByAge);
			} else {
				String key = ">" + (defectAgeList.get(i - 1) + 1) + " && <" + defectAgeList.get(i) ;
				List<Map<String, String>> defectsByAge = new ArrayList<Map<String, String>>();
				for (String priorityKey : defectPriorities) {

					metric.put(priorityKey, (String.valueOf(defectRepository
							.count(QDefect.defect.defectAge.between(defectAgeList.get(i - 1) + 1, defectAgeList.get(i))
									.and(QDefect.defect.defectPriority.equalsIgnoreCase(priorityKey))
									.and(QDefect.defect.projectId.equalsIgnoreCase(scopeProject.getpId())
											.and(QDefect.defect.defectStatus.equalsIgnoreCase(IN_PROGRESS)))))));

				}
				metric.put("Defect Age Strategy", key);
				defectsByAge.add(metric);

				openDefectsByAge.put("Range" + (i + 1), defectsByAge);
			}

		}
		String keyAfterUpperLimit = ">" + defectAgeList.get(resolCount - 1);
		List<Map<String, String>> defectsByAgeMorethanUpperLimit = new ArrayList<Map<String, String>>();
		Map<String, String> metricsAfterUpperLimit = new HashMap<String, String>();
		for (String priorityKey : defectPriorities) {
			metricsAfterUpperLimit.put(priorityKey,
					(String.valueOf(
							defectRepository.count(QDefect.defect.defectAge.gt(defectAgeList.get(resolCount - 1))
									.and(QDefect.defect.defectPriority.equalsIgnoreCase(priorityKey))
									.and(QDefect.defect.projectId.equalsIgnoreCase(scopeProject.getpId())
											.and(QDefect.defect.defectStatus.equalsIgnoreCase(IN_PROGRESS)))))));
		}
		metricsAfterUpperLimit.put("Defect Age Strategy", keyAfterUpperLimit);
		defectsByAgeMorethanUpperLimit.add(metricsAfterUpperLimit);
		openDefectsByAge.put("Range" + (resolCount + 1), defectsByAgeMorethanUpperLimit);
		if (!openDefectsByAge.isEmpty()) {
			aggregation.setDefectsByAgeDetails(openDefectsByAge);
		}
	}

}
