(function() {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('jiraDetailedViewController', jiraDetailedViewController);

    jiraDetailedViewController.$inject = ['$scope', 'codeAnalysisData', 'testSuiteData', '$q', '$filter', '$uibModal', '$location', '$routeParams', '$http', '$cookieStore', '$cookies', 'featureData'];

    function jiraDetailedViewController($scope, codeAnalysisData, testSuiteData, $q, $filter, $uibModal, $location, $routeParams, $http, $cookieStore, $cookies, featureData) {
        var ctrl = this;
        ctrl.componentId = $routeParams.componentId;
        ctrl.openback = openback;
        var apiHost = 'http://localhost:3000';
        var qahost = 'http://10.20.1.183:3001';
        ctrl.ppiidss = $cookies.get('projectIdd');
        ctrl.projectpath = $cookies.get('projectNameJira');
        ctrl.projectpathId = $cookies.get('projectIdJira');
        ctrl.projectiddefects = $cookies.get('ProSpId');
        ctrl.usernamepro = $cookies.get('username');
       
        //Logout Functionality
        ctrl.logout = function() {
            $cookieStore.remove("username");
            $cookieStore.remove("authenticated");
            $location.path('/');
        };

        $http.get("/api/getProjectsByUser/?username=" + ctrl.usernamepro)
            .then(function(response) {
                ctrl.getAllProjects = response.data;
                for (var i = 0; i < ctrl.getAllProjects.length; i++) {
                    for (var j = 0; j < ctrl.getAllProjects[i].usersGroup.length; j++) {
                        for (var k = 0; k < ctrl.getAllProjects[i].usersGroup[j].userRoles.length; k++) {
                            ctrl.vvv = ctrl.getAllProjects[i].usersGroup[j].user;
                            ctrl.projectIDS = ctrl.getAllProjects[i].id;
                            if ((ctrl.getAllProjects[i].usersGroup[j].userRoles[k].permissions.indexOf("USER_MANAGEMENT_VIEW") > -1) && (ctrl.vvv == ctrl.usernamepro)) {
                                ctrl.usermanagementviews = true;
                            }

                        }
                    }
                }
            });

        //Retrieving the Dashboard Id and passing it in the Url
        function openback() {
            ctrl.dashId = $cookies.get('dashboardidpa');
            $location.path('/dashboard/' + ctrl.dashId);
        }

        featureData.jiraData(ctrl.projectiddefects, ctrl.projectpath).then(jiraDataFetch);
        function jiraDataFetch(data) {
            ctrl.defectsByProirity = data.defectsByProirity;
            var object = data.defectsByAgeDetails;
            var trackObject = {};
            var axisObject = {};
            var objectResoloutionGraph = data.defectsByResolutionDetails;
            var trackObjectResoloutionGraph = {};
            var axisObjectResoloutionGraph = {};
            angular.forEach(object, function(value, key) {
                var localObj = value[0];
                angular.forEach(localObj, function(value1, key1) {
                    if (key1 in trackObject) {
                        if (key1 == 'Defect Age Strategy') {
                            if (key1 in axisObject) {
                                axisObject[key1].push(value1);
                            }
                            else{
                                axisObject[key1] = [];
                                axisObject[key1].push(value1);
                            }
                        } else {
                            trackObject[key1].push(parseInt(value1));
                        }
                    } else {
                        if (key1 == 'Defect Age Strategy') {
                             if (key1 in axisObject) {
                                axisObject[key1].push(value1);
                            }
                            else{
                                axisObject[key1] = [];
                                axisObject[key1].push(value1);
                            }
                        } else {
                            trackObject[key1] = [];
                            trackObject[key1].push(parseInt(value1));
                        }

                    }
                });
            });

            bendDataFormatforChart(trackObject,axisObject);
            angular.forEach(objectResoloutionGraph, function(value, key) {
                var localObjResolution = value[0];
                angular.forEach(localObjResolution, function(value1, key1) {
                    if (key1 in trackObjectResoloutionGraph) {
                        if (key1 == 'Resolution Strategy') {
                            if (key1 in axisObjectResoloutionGraph) {
                                axisObjectResoloutionGraph[key1].push(value1);
                            }
                            else{
                                axisObjectResoloutionGraph[key1] = [];
                                axisObjectResoloutionGraph[key1].push(value1);
                            }
                        } else {
                            trackObjectResoloutionGraph[key1].push(parseInt(value1));
                        }
                    } else {
                        if (key1 == 'Resolution Strategy') {
                             if (key1 in axisObjectResoloutionGraph) {
                                axisObjectResoloutionGraph[key1].push(value1);
                            }
                            else{
                                axisObjectResoloutionGraph[key1] = [];
                                axisObjectResoloutionGraph[key1].push(value1);
                            }
                        } else {
                            trackObjectResoloutionGraph[key1] = [];
                            trackObjectResoloutionGraph[key1].push(parseInt(value1));
                        }

                    }
                });
            });
            ResolutionChartBindData(trackObjectResoloutionGraph,axisObjectResoloutionGraph);
        }

        function bendDataFormatforChart(trackObject,axisObject) {
            var finalObjArray = [];
            angular.forEach(trackObject, function(value, key) {
                var collection = [];
                collection.push(key);
                collection = collection.concat(value);
                finalObjArray.push(collection);              
            });           
            $scope.openDef = c3.generate({
                bindto: '#openDef',
                data: {
                    columns: finalObjArray,
                    
                    type: 'bar'
                },
                axis: {
                    x: {
                        type: 'category',
                        categories: axisObject["Defect Age Strategy"]
                    }
                },
                color: {
                    pattern: ['rgb(172,8,4)', 'rgb(222,29,24)', 'rgb(123,123,123)', 'rgb(105,203,143)', 'rgb(5,169,68)']
                }

            });

        }

        function ResolutionChartBindData(trackObjectResoloutionGraph,axisObjectResoloutionGraph) {
            var finalObjArray = [];
            angular.forEach(trackObjectResoloutionGraph, function(value, key) {
                var collection = [];
                collection.push(key);
                collection = collection.concat(value);
                finalObjArray.push(collection);              
            });           
           $scope.restime = c3.generate({
                bindto: "#restime",

                data: {
                    columns: 
                        finalObjArray,
                    type: 'bar'

                },
                axis: {
                    x: {
                        type: 'category',
                        categories: axisObjectResoloutionGraph["Resolution Strategy"]
                    }

                },
                color: {
                    pattern: ['rgb(172,8,4)', 'rgb(222,29,24)', 'rgb(123,123,123)', 'rgb(105,203,143)', 'rgb(5,169,68)']
                }
            });

        }

        featureData.sprintDta(ctrl.projectpathId, ctrl.projectpath).then(sprintdataProcess);
        function sprintdataProcess(data) {
            ctrl.jirametricsdatanormal = data;
            ctrl.jirametricsdata = ctrl.jirametricsdatanormal.reverse();
            if (ctrl.jirametricsdata[5] != undefined) {
                ctrl.sprintIds = ctrl.jirametricsdata[5].sprintData.sprintId;
            }
            featureData.sprintId = ctrl.sprintIds;
            ctrl.sprintId = featureData.sprintId;
            //featureData.getLatestSprint(ctrl.sprintId,ctrl.projectpathId).then(fetchLatestSprint);
            // $cookies.put('sprintId', ctrl.sprintIds);
            var progress = ['DefectClosure'];
            var comittedStoryPoints = ['CommittedStoryPoints'];
            var completedStoryPoint = ['CompletedStoryPoints'];
            var axisSprintName = [];
            var axisSprintNameclos = [];
            var progressVelocity = ["Saydoratio"];
            var defectsFoundClosure = ["DefectsFound"];
            var defectsResolvedClosure = ["DefectsResolved"];
            var defectsUnResolvedClosure = ["DefectsUnResolved"];
            var sprintDefectsResolvedClosure = ["sprintDefectsResolved"];
            var issuesAdded = ["issuesAdded"];
            var issuesRemoved = ["issuesRemoved"];
            var comittedStoryPointsVolatility = ['Committed Story Points'];
            var completedStoryPointVolatility = ['Completed Story Points'];
            //Processing data for sprint list table
            ctrl.spAllDetails = data;
            //Data Process for Velocity Chart Graphs
            for (var i = 0; i < data.length; i++) {
                if (data[i].sprintData != undefined) {
                    comittedStoryPoints.push(data[i].sprintData.committedStoryPoints);
                    completedStoryPoint.push(data[i].sprintData.completedStoryPoints);
                    defectsFoundClosure.push(data[i].sprintData.defectsFound.total);
                    defectsResolvedClosure.push(data[i].sprintData.defectsResolved.total);
                    defectsUnResolvedClosure.push(data[i].sprintData.defectsUnresolved.total);
                    sprintDefectsResolvedClosure.push(data[i].sprintData.sprintDefectsResolved.total);
                    var percentScore = Math.round((data[i].sprintData.defectsResolved.total / data[i].sprintData.defectsFound.total) * 100);
                    if (isFinite(percentScore)) {
                        progress.push(percentScore);
                        var defects_found = [sprintDefectsResolvedClosure, defectsUnResolvedClosure, defectsResolvedClosure, defectsFoundClosure, progress];
                        axisSprintNameclos.push(data[i].name);
                        axisSprintName.push(data[i].name);
                    } else {
                        var percentScore = 0;
                        progress.push(percentScore);
                        var defects_found = [sprintDefectsResolvedClosure, defectsUnResolvedClosure, defectsResolvedClosure, defectsFoundClosure, progress];
                        axisSprintNameclos.push(data[i].name);
                        axisSprintName.push(data[i].name)
                    }
                }
            }
            //Adding x axis label for both graphs
            for (var i = 0; i < data.length; i++) {
                if (data[i].sprintData != undefined) {
                    var percentScoreVelocity = Math.round((data[i].sprintData.completedStoryPoints / data[i].sprintData.committedStoryPoints) * 100);
                    if (isFinite(percentScoreVelocity)) {
                        progressVelocity.push(percentScoreVelocity);
                        var sprint_chart_Data = [completedStoryPoint, comittedStoryPoints, progressVelocity];
                    } else {
                        var percentScoreVelocity = 0;
                        progressVelocity.push(percentScoreVelocity);
                        var sprint_chart_Data = [completedStoryPoint, comittedStoryPoints, progressVelocity];
                    }
                }
            }
            //Data Process for Velocity Chart Graphs
            for (var i = 0; i < data.length; i++) {
                if (data[i].sprintData != undefined) {
                    comittedStoryPointsVolatility.push(data[i].sprintData.committedStoryPoints);
                    completedStoryPointVolatility.push(data[i].sprintData.completedStoryPoints);
                    issuesAdded.push(data[i].sprintData.burndown.issuesAdded.storyPoints);
                    issuesRemoved.push(data[i].sprintData.burndown.issuesRemoved.storyPoints);
                    var story_volatility = [issuesAdded, issuesRemoved, comittedStoryPointsVolatility, completedStoryPointVolatility];
                }
            }

            //C3.js Sprint Graph Generation
            $scope.sprintdatas = c3.generate({
                bindto: '#sprintdatas',
                data: {
                    columns: sprint_chart_Data,
                    names: {
                        Saydoratio: 'Say Do Ratio',
                        committedStoryPoints: 'Committed Story Points',
                        completedStoryPoints: 'Completed Story Points',

                    },

                    type: 'bar',
                    types: {
                        Saydoratio: 'line',
                    },
                },
                axis: {
                    x: {
                        type: 'category',
                        categories: axisSprintName
                    }
                },
                color: {
                    pattern: ['#CCEBF5', '#00A744', '#000']
                },
                legend: {
                    position: 'inset',
                    inset: {
                        anchor: 'top-left',
                        x: 20,
                        y: -40,
                        step: 1
                    }
                },
                padding: {
                    top: 40
                }

            });

            $scope.defectClosure = c3.generate({
                bindto: '#defectClosure',
                data: {
                    columns: defects_found,
                    names: {
                        DefectClosure: 'Defect Closure',
                        defectsFoundClosure: 'Defects Found',
                        defectsResolvedClosure: 'Defects Resolved',
                        defectsUnResolvedClosure: 'Defects UnResolved',
                        sprintDefectsResolvedClosure: 'Sprint Defects Resolved'

                    },
                    type: 'bar',
                    types: {
                        DefectClosure: 'line',
                    },
                },
                axis: {
                    x: {
                        type: 'category',
                        categories: axisSprintNameclos
                    }
                },
                color: {
                    pattern: ['#75FD63', '#FF4444', '#669900', '#0099CC', '#000']
                },

                legend: {
                    position: 'inset',
                    inset: {
                        anchor: 'top-left',
                        x: 20,
                        y: -40,
                        step: 1
                    }
                },
                padding: {
                    top: 40
                }

            });

            //story volatility graph
            $scope.storyVolatility = c3.generate({
                bindto: '#storyVolatility',

                data: {
                    columns: story_volatility,
                    names: {
                        issuesAdded: 'Issues Added',
                        issuesRemoved: 'Issues Removed',
                        comittedStoryPointsVolatility: 'Committed Story Points',
                        completedStoryPointVolatility: 'Completed Story Points',
                    },

                    type: 'bar'
                },
                axis: {
                    x: {
                        type: 'category',
                        categories: axisSprintName
                    }
                },
                color: {
                    pattern: ['#FF4444', '#FF7F0E', '#40B3D9', '#669900']
                },

                legend: {
                    position: 'inset',
                    inset: {
                        anchor: 'top-left',
                        x: 20,
                        y: -40,
                        step: 1
                    }
                },
                padding: {
                    top: 40
                }

            });

        }


        //Fetching Release  Data
        featureData.ReleaseData(ctrl.projectpathId, ctrl.projectpath).then(ReleaseDataProcessing);

        //Processing Release Data
        function ReleaseDataProcessing(data) {
            ctrl.releasegraphData = data;
            ctrl.releasegraph = ctrl.releasegraphData.reverse();
            var noOfStoryCompleted = ["No of Stories Completed"];
            var noOfStoryPoints = ["Completed Story Points"];
            var totaldefectsFound = ["Defects Found"];
            var releaseName = [];
            var totalDefectsResolved = ["Defects Resolved"];
            //Adding No of Story Points Completed to the Array
            for (var i = 0; i < data.length; i++) {
                if (data[i].versionData != undefined) {
                    noOfStoryCompleted.push(data[i].versionData.noofStoryCompleted)
                }
            }

            //Adding No of Story Points to the Array
            for (var i = 0; i < data.length; i++) {
                if (data[i].versionData != undefined) {
                    noOfStoryPoints.push(data[i].versionData.noofStoryPoints)
                }
            }

            //Adding Version data as x axis label  to the Array
            for (var i = 0; i < data.length; i++) {
                if (data[i].versionData != undefined) {
                    releaseName.push(data[i].versionData.releaseName)
                }
            }

            //Adding  Total Defects Found to the Array
            for (var i = 0; i < data.length; i++) {
                if (data[i].versionData != undefined) {
                    if (data[i].versionData.defectsFound != undefined) {
                        totaldefectsFound.push(data[i].versionData.defectsFound.total)
                    } else {
                        totaldefectsFound.push(0);
                    }
                }
            }

            //Adding  Total Defects Resolved to the Array
            for (var i = 0; i < data.length; i++) {
                if (data[i].versionData != undefined) {
                    if (data[i].versionData.defectsResolved != undefined) {
                        totalDefectsResolved.push(data[i].versionData.defectsResolved.total)
                    } else {
                        totalDefectsResolved.push(0);
                    }
                }
            }

            //Release Graph Generation
            $scope.releaseGraph = c3.generate({
                bindto: '#releaseGraph',

                data: {
                    columns: [
                        noOfStoryCompleted, noOfStoryPoints, totaldefectsFound, totalDefectsResolved
                    ],
                    type: 'spline'
                },
                axis: {
                    x: {
                        type: 'category',
                        categories: releaseName
                    }
                },
                color: {
                    pattern: ['#00cc00', '#3399ff', '#000', '#cc6699']
                }

            });
        }

        ctrl.mySplit = function(string, nb) {
            var array = string.split('"');
            return array[1].split('"')[0];
        }
    }
})();