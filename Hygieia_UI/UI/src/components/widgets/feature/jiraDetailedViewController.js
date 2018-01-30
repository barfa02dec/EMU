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
        //Empty array created for Jira Defect Graphs
        var medium = ['Medium'];
        var opndefhighest = ['Highest'];
        var opndefhigh = ['High'];
        var opndeflow = ['Minor'];
        var lowval = ['Minor'];
        var jiraLebels = [];
        var openDefJiraLabel = [];
        var highest = ['Highest'];
        var high = ['High'];
        var Mediumvalue = ['Medium'];
        var Lowvalue = ['Low'];
        var Lowestvalue = ['Lowest'];
        var OpendefectMedium = ['Medium'];
        var OpendefectLow = ["Low"];
        var OpendefectLowest = ["Lowest"];

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

        //Retrieving Jira-Defect Response and Processing 
        featureData.jiraData(ctrl.projectiddefects, ctrl.projectpath).then(jiraDataFetch);

        //Processing Jira-Defect Response
        function jiraDataFetch(data) {
            ctrl.criIssue = data.defectsByProirity.Highest;
            ctrl.majorIssue = data.defectsByProirity.High;
            ctrl.mediumIssue = data.defectsByProirity.Medium;
            ctrl.lowIssue = data.defectsByProirity.Low;
            ctrl.lowestIssue = data.defectsByProirity.Lowest;
            ctrl.graphData = data.defectsByResolutionDetails;
            ctrl.ageOfOpenDefects = data.defectsByAgeDetails;
            ctrl.medlow = parseInt(ctrl.lowIssue) + (ctrl.mediumIssue);
            var highArr = [];
            ctrl.dataEnv = data.defectsByEnvironment;

            for (var key in ctrl.graphData) {
                if (ctrl.graphData.hasOwnProperty(key)) {
                    highArr.push(ctrl.graphData[key]);
                }
            }

            //Adding Highest values into Array
            for (var i = 0; i < highArr.length; i++) {
                if (highArr[i][0].hasOwnProperty("Highest")) {
                    highest.push(highArr[i][0].Highest);
                } else {
                    highest.push(0);
                }
            }

            //Adding Low values into Array
            for (var i = 0; i < highArr.length; i++) {
                if (highArr[i][0].hasOwnProperty("Low")) {
                    Lowvalue.push(highArr[i][0].Low);
                } else {
                    Lowvalue.push(0);
                }
            }

            //Adding Medium values into Array
            for (var i = 0; i < highArr.length; i++) {
                if (highArr[i][0].hasOwnProperty("Medium")) {
                    Mediumvalue.push(highArr[i][0].Medium);
                } else {
                    Mediumvalue.push(0);
                }
            }

            //Adding High values into Array            
            for (var i = 0; i < highArr.length; i++) {
                if (highArr[i][0].hasOwnProperty("High")) {
                    high.push(highArr[i][0].High);
                } else {
                    high.push(0);
                }
            }

            //Adding High values into Array            
            for (var i = 0; i < highArr.length; i++) {
                if (highArr[i][0].hasOwnProperty("Lowest")) {
                    Lowestvalue.push(highArr[i][0].Lowest);
                } else {
                    Lowestvalue.push(0);
                }
            }

            //Adding X axis label values into Array
            for (var i = 0; i < highArr.length; i++) {
                if (highArr[i][0].hasOwnProperty("Defect Resolution Strategy")) {
                    jiraLebels.push(highArr[i][0]['Defect Resolution Strategy']);
                }
                if (highArr[i][0].hasOwnProperty("Resolution Strategy")) {
                    jiraLebels.push(highArr[i][0]['Resolution Strategy']);
                }
            }

            //open defects graph
            var openDef = [];
            for (var key in ctrl.ageOfOpenDefects) {
                if (ctrl.ageOfOpenDefects.hasOwnProperty(key)) {
                    openDef.push(ctrl.ageOfOpenDefects[key]);
                }
            }

            //Adding Highest values into Array
            for (var i = 0; i < openDef.length; i++) {
                if (openDef[i][0].hasOwnProperty("Highest")) {
                    opndefhighest.push(openDef[i][0].Highest);
                } else {
                    opndefhighest.push(0);
                }
            }

            //Adding High values into Array
            for (var i = 0; i < openDef.length; i++) {
                if (openDef[i][0].hasOwnProperty("High")) {
                    opndefhigh.push(openDef[i][0].High);
                } else {
                    opndefhigh.push(0);
                }
            }

            //Adding Medium values into Array
            for (var i = 0; i < openDef.length; i++) {
                if (openDef[i][0].hasOwnProperty("Medium")) {
                    OpendefectMedium.push(openDef[i][0].Medium);
                } else {
                    OpendefectMedium.push(0);
                }
            }

            //Adding Low values into Array
            for (var i = 0; i < openDef.length; i++) {
                if (openDef[i][0].hasOwnProperty("Low")) {
                    OpendefectLow.push(openDef[i][0].Low);
                } else {
                    OpendefectLow.push(0);
                }
            }

            for (var i = 0; i < openDef.length; i++) {
                if (openDef[i][0].hasOwnProperty("Lowest")) {
                    OpendefectLowest.push(openDef[i][0].Lowest);
                } else {
                    OpendefectLowest.push(0);
                }
            }

            //Adding x axis label values into Array
            for (var i = 0; i < openDef.length; i++) {
                if (openDef[i][0].hasOwnProperty("Defect Age Strategy")) {
                    openDefJiraLabel.push(openDef[i][0]['Defect Age Strategy']);
                } else {
                    openDefJiraLabel.push(0);
                }
            }

            //c3.js graphs
            //Resolution Time Graph Generation
            $scope.openDef = c3.generate({
                bindto: '#openDef',
                data: {
                    columns: [
                        highest, high, Mediumvalue, Lowvalue, Lowestvalue
                    ],
                    type: 'bar'
                },
                axis: {
                    x: {
                        type: 'category',
                        categories: jiraLebels
                    }
                },
                color: {
                    pattern: ['rgb(172,8,4)', 'rgb(222,29,24)', 'rgb(123,123,123)', 'rgb(105,203,143)', 'rgb(5,169,68)']
                }

            });

            //Age of Open Defects Graph Generation
            $scope.restime = c3.generate({
                bindto: "#restime",

                data: {
                    columns: [
                        opndefhighest, opndefhigh, OpendefectMedium, OpendefectLow, OpendefectLowest

                    ],
                    type: 'bar'

                },
                axis: {
                    x: {
                        type: 'category',
                        categories: openDefJiraLabel
                    }

                },
                color: {
                    pattern: ['rgb(172,8,4)', 'rgb(222,29,24)', 'rgb(123,123,123)', 'rgb(105,203,143)', 'rgb(5,169,68)']
                }
            });
        }

        //Fetching Jira-Sprint Data
        
        featureData.sprintDta(ctrl.projectpathId, ctrl.projectpath).then(sprintdataProcess);

        //Processing Jira-Sprint Data
        function sprintdataProcess(data) {
            ctrl.jirametricsdata = data;
            if( ctrl.jirametricsdata[5] != undefined ){
            ctrl.sprintIds = ctrl.jirametricsdata[5].sprintData.sprintId;
            }
            featureData.sprintId = ctrl.sprintIds;
            ctrl.sprintId = featureData.sprintId;
            featureData.getLatestSprint(ctrl.sprintId,ctrl.projectpathId).then(fetchLatestSprint);
      
            // $cookies.put('sprintId', ctrl.sprintIds);
            var progress = ['Defect Closure'];
            var comittedStoryPoints = ['Committed Story Points'];
            var completedStoryPoint = ['Completed Story Points'];
            var axisSprintName = [];
            var axisSprintNameclos = [];
            var progressVelocity = ["Saydoratio"];

            //Processing data for sprint list table
            ctrl.spAllDetails = data;

            //Data Process for Velocity Chart Graphs
            for (var i = 0; i < data.length; i++) {
                if (data[i].sprintData != undefined) {
                    comittedStoryPoints.push(data[i].sprintData.committedStoryPoints);
                    completedStoryPoint.push(data[i].sprintData.completedStoryPoints);
                    var percentScore = Math.round((data[i].sprintData.completedStoryPoints / data[i].sprintData.committedStoryPoints) * 100);
                    progress.push(percentScore);
                    axisSprintNameclos.push(data[i].name);
                    axisSprintName.push(data[i].name)
                }

            }

            //Adding x axis label for both graphs
            for (var i = 0; i < data.length; i++) {
                if (data[i].sprintData != undefined) {
                    var percentScoreVelocity = Math.round((data[i].sprintData.completedStoryPoints / data[i].sprintData.committedStoryPoints) * 100);
                    progressVelocity.push(percentScoreVelocity);
                    var sprint_chart_Data = [completedStoryPoint, comittedStoryPoints, progressVelocity];
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
                    columns: [
                        progress
                    ],
                    type: 'spline'
                },
                axis: {
                    x: {
                        type: 'category',
                        categories: axisSprintNameclos
                    }
                },
                color: {
                    pattern: ['#ff4d4d']
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
        
        function fetchLatestSprint(data){
           var burnData = data.sprintData.burnDownHistory;

             var burnDowndata = ['Burn Down'];
              var xaxisDate = [];
            /* burnData.forEach(function(burndata){
                burnDowndata.push(burndata.allIssuesEstimateSum)
                xaxisDate.push(moment(burndata.date.miliseconds).format('MMM DD'))
             })*/
            
             for (var i = 0; i < burnData.length; i++) {
                if (burnData[i] != undefined) {
                    burnDowndata.push(burnData[i].remainingIssues);
                     xaxisDate.push(burnData[i].date);
                   // xaxisDate.push(moment(burndata[i].date.miliseconds).format('MMM DD'));
                }
            }


             $scope.burnDown = c3.generate({
                bindto: '#burnDown', 
                             
                data: {
                  columns: [
                    burnDowndata
                  ],
                  type: 'line'
                },
                axis: {
                    x:{
                        type: 'category',
                        categories: xaxisDate
                    }
                },
                color: {
                pattern: ['#ff4d4d']
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
            ctrl.releasegraph = data.versionData;
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