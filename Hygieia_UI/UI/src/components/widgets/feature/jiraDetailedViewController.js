(function () {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('jiraDetailedViewController', jiraDetailedViewController);

    jiraDetailedViewController.$inject = ['$scope', 'codeAnalysisData', 'testSuiteData', '$q', '$filter', '$uibModal', '$location', '$routeParams', '$http', '$cookieStore', '$cookies', 'featureData'];

    function jiraDetailedViewController($scope, codeAnalysisData, testSuiteData, $q, $filter, $uibModal, $location, $routeParams, $http, $cookieStore, $cookies, featureData) {
        var ctrl = this;        
        ctrl.componentId = $routeParams.componentId;
        ctrl.openback = openback;
        ctrl.logout = function () {
            $cookieStore.remove("username");
            $cookieStore.remove("authenticated");
        $location.path('/');
        };
         function openback() {
            ctrl.dashId = $cookies.get('dashboardidpa');
            $location.path('/dashboard/' + ctrl.dashId);
        }

        $scope.configs = {
            bindto: "#bloodpressure",
            data: {
                columns: [
                    ['data1', 30, 200, 100, 400, 150, 250]

                ],
                type: 'spline'
            }

            /*size: {
                           height: 350
                       },
                       legend: {
                           position: 'inset'
                       },
                          axis: {
                           x: {
                               type: 'category',
                               tick: {
                                   rotate: -50,
                                   multiline: false
                               },
                               height: 130,
                               label: 'Sprint Name'
                           }
                       }*/


        };

        $scope.configBar = {
            data: {
                columns: [
                    ['data1', 30, 200, 100, 400, 150, 250],
                    ['data2', 130, 100, 140, 200, 150, 50],
                    ['data3', 130, -150, 200, 300, -200, 100]

                ],
                type: 'bar'
            }

        };

        $scope.configBars = {
            bindto: "#storyVol",
            data: {

                columns: [
                    ['data1', 30, 200, 100, 400, 150, 250],
                    ['data2', 130, 100, 140, 200, 150, 50],
                    ['data3', 130, -150, 200, 300, -200, 100],
                    ['data4', 130, -150, 200, 300, -200, 100]

                ],
                type: 'bar'
            }

        };

        $scope.configArea = {
            bindto: "#veloChart",
            data: {
                columns: [
                    ['data1', 30, 200, 100, 400, 150, 250],
                    ['data2', 130, 100, 140, 200, 150, 50],
                    ['data3', 130, -150, 200, 300, -200, 100]

                ],
                type: 'area-spline',
                colors: {
                    data1: '#0099cc',
                    data2: '#669900'
                },
            }

        };
        var somethingWeLoadedFromTheServer = [
            { a: 23, b: 45, c: 12 },
            { a: 34, b: 19, c: 38 }
            
        ];

    

        $scope.configdefClosure = {
            bindto: "#defectClosure",
            data: {
                columns: [
                    ['data1', 30, 200, 100, 400, 150, 250]

                ],
                type: 'spline',
                colors: {
                    data1: '#ff4444'
                },
            }
        };        
        var apiHost = 'http://localhost:3000';
        var qahost = 'http://10.20.1.183:3001';
        ctrl.ppiidss = $cookies.get('projectIdd');
        /*$http.get(apiHost + "/api/getDefectSummery/" + ctrl.ppiidss)
                    .then(jiraDataFetch); 
        
                     function jiraDataFetch(data) {
                        alert("asd");
                ctrl.mediumIssue = data;
             
            }*/
        featureData.jiraData(ctrl.ppiidss).then(jiraDataFetch);
        var high = ['Major'];
        var medium = ['Medium'];
        var opndefhighest = ['Critical'];
        var opndefhigh = ['Major'];
        var opndeflow = ['Minor'];
        var highest = ['Critical'];
        var lowval = ['Minor'];

        var jiraLebels = [];
        var openDefJiraLabel = [];
        //var highest = ['Highest'];
        function jiraDataFetch(data) {
            
            ctrl.criIssue = data.defectsByProirity.Highest;
            ctrl.majorIssue = data.defectsByProirity.High;
            ctrl.mediumIssue = data.defectsByProirity.Medium;
            ctrl.lowIssue = data.defectsByProirity.Low;
            ctrl.graphData = data.defectsByResolutionDetails;
            ctrl.ageOfOpenDefects = data.defectsByAgeDetails;
             ctrl.medlow = parseInt(ctrl.lowIssue) + (ctrl.mediumIssue);
          
            //console.log(ctrl.graphData);
            //angular.forEach(ctrl.graphData, function (value,key) {
            var highArr = [];
            if (ctrl.graphData.hasOwnProperty("High")) {
                alert("asas");
            }
            for (var key in ctrl.graphData) {
                if (ctrl.graphData.hasOwnProperty(key)) {
                    highArr.push(ctrl.graphData[key]);
                }
            }
          
            for (var i = 0; i < highArr.length; i++) {
                if (highArr[i][0].hasOwnProperty("Highest")) {
                
                highest.push(highArr[i][0].Highest); 
            }
            else{
                highest.push(0);
               
            }
                lowval.push(highArr[i][0].Low); 
                high.push(highArr[i][0].High);
                medium.push(highArr[i][0].Medium);
                jiraLebels.push(highArr[i][0]['Resolution Strategy']);
            }
            console.log(high);  
            
            //open defects graph

            var openDef = [];
            if (ctrl.ageOfOpenDefects.hasOwnProperty("High")) {
                alert("asas");
            }

             for (var key in ctrl.ageOfOpenDefects) {
                if (ctrl.ageOfOpenDefects.hasOwnProperty(key)) {
                    openDef.push(ctrl.ageOfOpenDefects[key]);
                }
            }

            for (var i = 0; i < openDef.length; i++) {
                if (openDef[i][0].hasOwnProperty("Highest")) {
                
                 opndefhighest.push(openDef[i][0].Highest);
             }
             else{
               
                 opndefhighest.push(0);
             }
                opndefhigh.push(openDef[i][0].High);
               
                opndeflow.push(openDef[i][0].Low);
                
                openDefJiraLabel.push(openDef[i][0]['Defect Age Strategy']);
                
                //jiraLebels.push(openDef[i][0]['Resolution Strategy']);
            }

            $scope.openDef = c3.generate({
                bindto: '#openDef', 
                             
                data: {
                  columns: [
                    highest,high,lowval
                  ],
                  type: 'bar'
                },
                axis: {
                    x:{
                        type: 'category',
                        categories: jiraLebels
                    }
                },
                color: {
                pattern: ['#ff4d4d', '#ffa31a','#1ac6ff']
            }
  
            });        

            $scope.restime = c3.generate({
            bindto: "#restime",
            
            data: {
                columns: [
                   opndefhighest,opndefhigh,opndeflow

                ],
                type: 'bar'
               
            },
              axis: {
                    x:{
                        type: 'category',
                        categories: openDefJiraLabel
                    }
                    
                },
                color: {
                pattern: ['#ff4d4d', '#ffa31a','#1ac6ff']
            }
        });                
    }        

    featureData.sprintDta(ctrl.ppiidss).then(sprintdataProcess);
    function sprintdataProcess(data){
        console.log(data[0]);
        console.log(data[1]);
        var comittedStoryPoints = ['committed Story Points'];
        var completedStoryPoint = ['completed Story Points'];
        var axisSprintName = [];
        ctrl.defectsfound = data[0].sprintData.defectsFound.total;
        ctrl.defectsResolved = data[0].sprintData.defectsResolved.total;
       ctrl.defectsUnresolved = data[0].sprintData.defectsUnresolved.total;
       ctrl.spname = data[0].name;
       ctrl.sidd = data[0].sid;
       ctrl.stat = data[0].sprintData.state;
       ctrl.spname = data[0].sprintData.sprintName;

       //ctrl.defectsfoundPrvSprint = data[1].sprintData.defectsFound.total;
       //ctrl.defectsResolvedPrvSprint =data[1].sprintData.defectsResolved.total;
       //ctrl.defectsUnresolvedPrvSprint =data[1].sprintData.defectsUnresolved.total;

       //story count

       ctrl.completedIssueCount = data[0].sprintData.completedIssueCount;
       ctrl.committedIssueCount = data[0].sprintData.committedIssueCount;
       ctrl.committedStoryPoints = data[0].sprintData.burndown.issuesAdded.count;
       ctrl.completedStoryPoints = data[0].sprintData.burndown.issuesRemoved.count;
       for(var i=0;i<data.length;i++){
          if(data[i].sprintData != undefined){
              comittedStoryPoints.push(data[i].sprintData.committedStoryPoints)
          }
          
       }

       for(var i=0;i<data.length;i++){
         
              axisSprintName.push(data[i].name)
          
          
       }
       for(var i=0;i<data.length;i++){
          if(data[i].sprintData != undefined){
           
              completedStoryPoint.push(data[i].sprintData.completedStoryPoints)
          }
          
       }

       $scope.sprintdatas = c3.generate({
                bindto: '#sprintdatas', 
                             
                data: {
                  columns: [
                    comittedStoryPoints,completedStoryPoint
                  ],
                  type: 'area-spline'
                },
                axis: {
                    x:{
                        type: 'category',
                        categories: axisSprintName
                    }
                },
                color: {
                pattern: ['#CCEBF5', '#B7DBC4']
            }
  
            });

        }


      
        
    }
})();