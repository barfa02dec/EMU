/**
 * Gets feature related data
 */
(function() {
	'use strict';

	angular.module(HygieiaConfig.module + '.core').factory('featureData', featureData);

	function featureData($http) {
		var param = '?component=';
		var projectParam = '&projectId=';
		
		var agileType = {
			kanban : "&agileType=kanban",
			scrum : "&agileType=scrum",
		};
		var estimateMetricTypeParam = "&estimateMetricType=";
		var agileTypeParam = "&agileType=";
		
		var testAggregateSprintEstimates = 'test-data/feature-aggregate-sprint-estimates.json';
		var buildAggregateSprintEstimates = '/api/feature/estimates/aggregatedsprints/';

		var testFeatureWip = 'test-data/feature-super.json';
		var buildFeatureWip = '/api/feature/estimates/super/';

		var testSprint = 'test-data/feature-iteration.json';
		var buildSprint = '/api/iteration/';
		
		var testProjectsRoute = 'test-data/projects.json';
        var buildProjectsRoute = '/api/scope';

		var testProjectsByCollectorId = 'test-data/teams.json';
		var buildProjectsByCollectorId = '/api/scopecollector/';

		var testTeamsRoute = 'test-data/teams.json';
		var buildTeamsRoute = '/api/team';

		var testTeamsByCollectorId = 'test-data/teams.json';
		var buildTeamsByCollectorId = '/api/teamcollector/';
		var sprintId = undefined;
		//var ProSpId = undefined;
		return {
			sprintMetrics : aggregateSprintEstimates,
			jiraData:jiraDataGer,
			featureWip : featureWip,
			sprint : sprint,
			teams : teams,
			teamsByCollectorId : teamsByCollectorId,
			projects : projects,
			projectsByCollectorId : projectsByCollectorId,
			sprintDta:sprintDataFetch,
			ReleaseData:ReleaseDataFetch,
			updateSprintDta:updateSprintDta,
			getLatestSprint:getLatestSprint,
			updateReleaseDta:updateReleaseDta,
			heatMapData:heatMapData
		};
		
		function aggregateSprintEstimates(componentId, filterTeamId, filterProjectId, estimateMetricType, agileType) {
			return $http.get(HygieiaConfig.local ? testAggregateSprintEstimates : buildAggregateSprintEstimates + filterTeamId + param + componentId + projectParam + filterProjectId
					+ (estimateMetricType != null? estimateMetricTypeParam + estimateMetricType : "")
					+ (agileType != null? agileTypeParam + agileType : ""))
					.then(function(response) {
						return response.data;
					});
		}

		function jiraDataGer(filterProjectId) { 
			 
			return $http.get('/api/defectSummary/' +filterProjectId)
					.then(function(response) {
						return response.data;
					});
		}
		function sprintDataFetch(filterProjectId,projectsname) {  
			var uri_enc = encodeURIComponent(projectsname);
			return $http.get('/api/sprints?projectId=' +filterProjectId + "&noOfSprintToShow=" + "6")
					.then(function(response) { 
						return response.data;
					});
		}

		function updateSprintDta(sprintid,projectid) {  
			 
			return $http.get('/api/sprints/details?sid=' +projectid + "&projectId=" + sprintid)
					.then(function(response) { 
						return response.data;
					});
		}

		function getLatestSprint(sidd,pidd) {  
			 
			return $http.get('/api/details?sid=' +sidd+ "&projectId=" + pidd)
					.then(function(response) { 
						return response.data;
					});
		}
		
		function ReleaseDataFetch(filterProjectId) {  
			 //var uri_enc = encodeURIComponent(projectsname);
			return $http.get('/api/releases?projectId=' + filterProjectId + "&noOfReleaseToShow=" + "6")
					.then(function(response) { 
						return response.data;
					});
		}

		/**
		 * Retrieves current super features and their total in progress
		 * estimates for a given sprint and team
		 *
		 * @param componentId
		 * @param filterTeamId
		 */
		function featureWip(componentId, filterTeamId, filterProjectId, estimateMetricType, agileType) {
			return $http.get(HygieiaConfig.local ? testFeatureWip : buildFeatureWip + filterTeamId + param + componentId + projectParam + filterProjectId
					+ (estimateMetricType != null? estimateMetricTypeParam + estimateMetricType : "")
					+ (agileType != null? agileTypeParam + agileType : ""))
					.then(function(response) {
						return response.data;
					});
		}

		/**
		 * Retrieves current team's sprint detail
		 *
		 * @param componentId
		 * @param filterTeamId
		 */
		function sprint(componentId, filterTeamId, filterProjectId, agileType) {
			return $http.get(HygieiaConfig.local ? testSprint : buildSprint + filterTeamId + param + componentId + projectParam + filterProjectId
					+ (agileType != null? agileTypeParam + agileType : ""))
					.then(function(response) {
						return response.data;
					});
		}

		/**
		 * Retrieves projects by  collector ID
		 *
		 * @param collectorId
		 */
		function projectsByCollectorId(collectorId,projectIDspec) {
			return $http.get(HygieiaConfig.local ? testProjectsByCollectorId : buildProjectsByCollectorId + collectorId +'/'+ projectIDspec)
				.then(function(response) {
					return response.data;
				});
		}

		/**
		 * Retrieves teams by  collector ID
		 *
		 * @param collectorId
		 */
		function teamsByCollectorId(collectorId) {
			return $http.get(HygieiaConfig.local ? testTeamsByCollectorId : buildTeamsByCollectorId + collectorId)
					.then(function(response) {
						return response.data;
					});
		}
		
		/**
         * Retrieves all projects
         */      
        function projects() {
            return $http.get(HygieiaConfig.local ? testProjectsRoute : (buildProjectsRoute))
                .then(function (response) {
                    return response.data;
                });
        }

		/**
		 * Retrieves all teams
		 */
		function teams() {
			return $http.get(HygieiaConfig.local ? testTeamsRoute : (buildTeamsRoute))
				.then(function (response) {
					return response.data;
				});
		}
		
		function updateReleaseDta(releaseId,projectId) {  
			return $http.get('/api/releases/details?releaseId=' +releaseId + "&projectId=" + projectId)
					.then(function(response) { 
						return response.data;
					});
		}

		 function heatMapData(projectid) {
            
             return   $http.get("/api/heatmaps/?projectId=" + projectid + '&noOfHeatMapToShow=' + 3)
                    .then(function(response) {
                       return response.data;
                    });
        }
	}
})();
