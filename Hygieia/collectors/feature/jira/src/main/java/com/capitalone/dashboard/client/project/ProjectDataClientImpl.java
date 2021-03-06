package com.capitalone.dashboard.client.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.capitalone.dashboard.client.JiraClient;
import com.capitalone.dashboard.model.Scope;
import com.capitalone.dashboard.repository.FeatureCollectorRepository;
import com.capitalone.dashboard.repository.ScopeRepository;
import com.capitalone.dashboard.util.ClientUtil;
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
public class ProjectDataClientImpl implements ProjectDataClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDataClientImpl.class);
	private static final ClientUtil TOOLS = ClientUtil.getInstance();
	
	private final NewFeatureSettings featureSettings;
	private final ScopeRepository projectRepo;
	private final FeatureCollectorRepository featureCollectorRepository;
	private final JiraClient jiraClient;

	/**
	 * Extends the constructor from the super class.
	 *
	 */
	public ProjectDataClientImpl(NewFeatureSettings featureSettings, ScopeRepository projectRepository, 
			FeatureCollectorRepository featureCollectorRepository, JiraClient jiraClient) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing data collection for the feature widget, project-level data...");
		}

		this.featureSettings = featureSettings;
		this.projectRepo = projectRepository;
		this.featureCollectorRepository = featureCollectorRepository;
		this.jiraClient = jiraClient;
	}

	/**
	 * Explicitly updates queries for the source system, and initiates the
	 * update to MongoDB from those calls.
	 */
	public void updateJiraProjectInfo() {
		LOGGER.info("Collection of project data started for Project ID : " + featureSettings.getProjectId());
		List<BasicProject> projects = JiraCollectorUtil.getJiraProjects(featureSettings);
		
		if (projects != null && !projects.isEmpty()) {
			updateMongoInfo(projects);
		}
		LOGGER.info("Collection of project data completed for Project ID : " + featureSettings.getProjectId());
	}
	
	/**
	 * Updates the MongoDB with a JSONArray received from the source system
	 * back-end with story-based data.
	 * 
	 * @param currentPagedJiraRs
	 *            A list response of Jira issues from the source system
	 */
	private void updateMongoInfo(List<BasicProject> currentPagedJiraRs) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Size of paged Jira response: " + (currentPagedJiraRs == null? 0 : currentPagedJiraRs.size()));
		}
		
		if (currentPagedJiraRs != null) {
			ObjectId jiraCollectorId = featureCollectorRepository.findByName(FeatureCollectorConstants.JIRA).getId();
			
			List<String> jiraProjectsToShowInDashboard = new ArrayList<String>();
			jiraProjectsToShowInDashboard = Arrays.asList(featureSettings.getJiraProjectIdList());
			
			for (BasicProject jiraScope : currentPagedJiraRs) {
				String scopeId = TOOLS.sanitizeResponse(jiraScope.getId());
				Scope scope = projectRepo.getScopeByIdAndProjectId(featureSettings.getProjectId(), scopeId);
				
				if (scope == null && jiraProjectsToShowInDashboard.contains(scopeId)) {
					scope = new Scope();
				}else if (!jiraProjectsToShowInDashboard.contains(scopeId)){
					continue;
				}

				scope.setCollectorId(jiraCollectorId);

				scope.setpId(TOOLS.sanitizeResponse(scopeId));
				scope.setProjectId(featureSettings.getProjectId());
				scope.setName(TOOLS.sanitizeResponse(jiraScope.getName()));
				scope.setIsDeleted("False");
				scope.setProjectPath(TOOLS.sanitizeResponse(jiraScope.getName()));
				scope.setToShowInEMUDashboard(jiraProjectsToShowInDashboard.contains(scope.getpId()));

				projectRepo.save(scope);
			}
		}
	}
	
	/**
	 * Retrieves the maximum change date for a given query.
	 * 
	 * @return A list object of the maximum change date
	 */
	public String getMaxChangeDate() {
		String data = null;
		try {
			List<Scope> response = projectRepo
					.findTopByCollectorIdAndChangeDateGreaterThanOrderByChangeDateDesc(
							featureCollectorRepository.findByName(FeatureCollectorConstants.JIRA).getId(),
							featureSettings.getDeltaStartDate());
			if ((response != null) && !response.isEmpty()) {
				data = response.get(0).getChangeDate();
			}
		} catch (Exception e) {
			LOGGER.error("There was a problem retrieving or parsing data from the local repository while retrieving a max change date\nReturning null");
		}

		return data;
	}
	
	/**
	 * Find the current collector item for the jira team id
	 * 
	 * @param teamId	the team id
	 * @return			the collector item if it exists or null
	 */
	private Scope findOneScope(String scopeId) {
		List<Scope> scopes = projectRepo.getScopeIdById(scopeId);
		
		// Not sure of the state of the data
		if (scopes.size() > 1) {
			LOGGER.warn("More than one collector item found for scopeId " + scopeId);
		}
		
		if (!scopes.isEmpty()) {
			return scopes.get(0);
		}
		
		return null;
	}
}
