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
        var apiHost = 'http://localhost:3000';
        var qahost = 'http://10.20.1.183:3001';
        ctrl.ppiidss = $cookies.get('projectIdd');

        //Empty array created for Jira Defect Graphs
        var high = ['Major'];
        var medium = ['Medium'];
        var opndefhighest = ['Critical'];
        var opndefhigh = ['Major'];
        var opndeflow = ['Minor'];
        var highest = ['Critical'];
        var lowval = ['Minor'];
        var jiraLebels = [];
        var openDefJiraLabel = [];

        //Logout Functionality
        ctrl.logout = function () {
            $cookieStore.remove("username");
            $cookieStore.remove("authenticated");
            $location.path('/');
        };

        //Retrieving the Dashboard Id and passing it in the Url
         function openback() {
            ctrl.dashId = $cookies.get('dashboardidpa');
            $location.path('/dashboard/' + ctrl.dashId);
        }

        
        //Retrieving Jira-Defect Response and Processing 
        featureData.jiraData(ctrl.ppiidss).then(jiraDataFetch);

        
        //Processing Jira-Defect Response
        function jiraDataFetch(data) {
            ctrl.criIssue = data.defectsByProirity.Highest;
            ctrl.majorIssue = data.defectsByProirity.High;
            ctrl.mediumIssue = data.defectsByProirity.Medium;
            ctrl.lowIssue = data.defectsByProirity.Low;
            ctrl.graphData = data.defectsByResolutionDetails;
            ctrl.ageOfOpenDefects = data.defectsByAgeDetails;
            ctrl.medlow = parseInt(ctrl.lowIssue) + (ctrl.mediumIssue);
            var highArr = [];
            for (var key in ctrl.graphData) {
                if (ctrl.graphData.hasOwnProperty(key)) {
                    highArr.push(ctrl.graphData[key]);
                }
            }
             
                //Adding Highest values into Array
                for (var i = 0; i < highArr.length; i++) {
                        if (highArr[i][0].hasOwnProperty("Highest")) {
                        highest.push(highArr[i][0].Highest); 
                    }
                    else{
                        highest.push(0);
                       }
                     }

               //Adding Low and Medium values into Array
              for (var i = 0; i < highArr.length; i++) {
                if (highArr[i][0].hasOwnProperty("Low") && highArr[i][0].hasOwnProperty("Medium")) {
                  var convertt = Number(highArr[i][0].Low);
                  var converttMed = Number(highArr[i][0].Medium);
                  var valpars =convertt + converttMed;
                  lowval.push(valpars); 
              }
              else{
                lowval.push(0);
              }
            }

              //Adding High values into Array            
               for (var i = 0; i < highArr.length; i++) {
                if (highArr[i][0].hasOwnProperty("High")) {
                high.push(highArr[i][0].High);
              }
               else{
                high.push(0);
              }
            }
             
               //Adding X axis label values into Array
               for (var i = 0; i < highArr.length; i++) {
                if (highArr[i][0].hasOwnProperty("Resolution Strategy")) {
                jiraLebels.push(highArr[i][0]['Resolution Strategy']);
              }
              else{
                jiraLebels.push(0);
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
             }
             else{
               opndefhighest.push(0);
             }
           }
            
           //Adding High values into Array
            for (var i = 0; i < openDef.length; i++) {
                if (openDef[i][0].hasOwnProperty("High")) {
                opndefhigh.push(openDef[i][0].High);
              }
               else{
               opndefhigh.push(0);
             }
            }

            //Adding Low and Medium values into Array
             for (var i = 0; i < openDef.length; i++) {
                if (openDef[i][0].hasOwnProperty("Low") && openDef[i][0].hasOwnProperty("Medium")) {
                  var convertLowval = Number(openDef[i][0].Low);
                  var convertMedVal = Number(openDef[i][0].Medium);
                  var parsinh = convertLowval + convertMedVal;
                  opndeflow.push(parsinh);
               }
              else{
               opndeflow.push(0);
             }
            }

            //Adding x axis label values into Array
              for (var i = 0; i < openDef.length; i++) {
                if (openDef[i][0].hasOwnProperty("Defect Age Strategy")) {
                openDefJiraLabel.push(openDef[i][0]['Defect Age Strategy']);
              }
               else{
               openDefJiraLabel.push(0);
             }
            }
                

            //c3.js graphs

            //Resolution Time Graph Generation
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

            //Age of Open Defects Graph Generation
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


    //Fetching Jira-Sprint Data

      featureData.sprintDta(ctrl.ppiidss).then(sprintdataProcess);

      //Processing Jira-Sprint Data
      function sprintdataProcess(data){
        ctrl.jirametricsdata  = data;
        var progress = ['Defect Closure'];
        var comittedStoryPoints = ['committed Story Points'];
        var completedStoryPoint = ['completed Story Points'];
        var axisSprintName = [];
        var axisSprintNameclos = [];

        //Data procesing and fetching two sprints Data
        ctrl.defectsfound = data[0].sprintData.defectsFound.total;
        ctrl.defectsResolved = data[0].sprintData.defectsResolved.total;
        ctrl.defectsUnresolved = data[0].sprintData.defectsUnresolved.total;
       if(data[1].sprintData.defectsFound.total != undefined){
           ctrl.defectsfoundprev = data[1].sprintData.defectsFound.total;
       }
      else{
          ctrl.defectsfoundprev = 0;
      }

      if(data[1].sprintData.defectsResolved.total != undefined){
        ctrl.defectsResolvedprev = data[1].sprintData.defectsResolved.total;
      }
      else{
        ctrl.defectsResolvedprev = 0;
      }
       if(data[1].sprintData.defectsUnresolved.total != undefined){
         ctrl.defectsUnresolvedprev = data[1].sprintData.defectsUnresolved.total;
       }
      else{
        ctrl.defectsUnresolvedprev = 0;
      }

       //Processing data for sprint list table
       ctrl.spAllDetails = data;
       ctrl.spname = data[0].name;
       ctrl.sidd = data[0].sid;
       ctrl.stat = data[0].sprintData.state;
       ctrl.spname = data[0].sprintData.sprintName;

       ctrl.spnameprev = data[1].name;
       ctrl.siddprev = data[1].sid;
       ctrl.statprev = data[1].sprintData.state;
       ctrl.spnameprev = data[1].sprintData.sprintName;

       

      

       //Data procesing and fetching two sprints Data
       if(data[0].sprintData.completedIssueCount != undefined){
          ctrl.completedIssueCount = data[0].sprintData.completedIssueCount;
       }
       else{
           ctrl.completedIssueCount = 0;
       }  
       ctrl.committedIssueCount = data[0].sprintData.committedIssueCount;
       ctrl.committedStoryPoints = data[0].sprintData.burndown.issuesAdded.count;
       ctrl.completedStoryPoints = data[0].sprintData.burndown.issuesRemoved.count;
      if(data[1].sprintData.completedIssueCount != undefined){
        ctrl.completedIssueCountprev = data[1].sprintData.completedIssueCount;
       }
       else{
          ctrl.completedIssueCountprev = 0;
       }
       
       if(data[1].sprintData.committedIssueCount != undefined){
         ctrl.committedIssueCountprev = data[1].sprintData.committedIssueCount;
       }
       else{
        ctrl.committedIssueCountprev = 0;
       }
      
      if(data[1].sprintData.burndown.issuesAdded.count != undefined){
         ctrl.committedStoryPointsprev = data[1].sprintData.burndown.issuesAdded.count;
      }
      else{
        ctrl.committedStoryPointsprev = 0;
      }
      
      if(data[1].sprintData.burndown.issuesRemoved.count != undefined){
        ctrl.completedStoryPointsprev = data[1].sprintData.burndown.issuesRemoved.count;
      }
       else{
        ctrl.completedStoryPointsprev = 0;
       }


       //Data Process for Velocity Chart Graphs
       for(var i=0;i<data.length;i++){
          if(data[i].sprintData != undefined){
              comittedStoryPoints.push(data[i].sprintData.committedStoryPoints)
          }
          
       }

       for(var i=0;i<data.length;i++){
          if(data[i].sprintData != undefined){
           completedStoryPoint.push(data[i].sprintData.completedStoryPoints)
          }
          
       }
       
       //Data Process for Defect Closure Graph for taking the percentage Value
        for(var i=0;i<data.length;i++){
           if(data[i].sprintData != undefined){
               var percentScore =  Math.round((data[i].sprintData.completedStoryPoints/data[i].sprintData.committedStoryPoints)*100);
               progress.push(percentScore);
                axisSprintNameclos.push(data[i].name)

           }
        }

       //Adding x axis label for both graphs
       for(var i=0;i<data.length;i++){
         axisSprintName.push(data[i].name)
        }

       //C3.js Sprint Graph Generation
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
                    x:{
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


        //Fetching Release  Data
       featureData.ReleaseData(ctrl.ppiidss).then(ReleaseDataProcessing);

      //Processing Release Data
        function ReleaseDataProcessing(data){
          ctrl.releasegraph = data.versionData;
          var noOfStoryCompleted = ["No of Story points completed"];
          var noOfStoryPoints = ["No of Stories Completed"];
          var totaldefectsFound = ["Total Defects Found"];
          var releaseName = [];
          var totalDefectsResolved = ["Total Defects Resolved"];

          //Adding No of Story Points Completed to the Array
          for(var i=0;i<data.length;i++){
          if(data[i].versionData != undefined){
              noOfStoryCompleted.push(data[i].versionData.noofStoryCompleted)
          }
          
       }

       //Adding No of Story Points to the Array
       for(var i=0;i<data.length;i++){
          if(data[i].versionData != undefined){
              noOfStoryPoints.push(data[i].versionData.noofStoryPoints)
          }
      }

       //Adding Version data as x axis label  to the Array
       for(var i=0;i<data.length;i++){
          if(data[i].versionData != undefined){
              releaseName.push(data[i].versionData.releaseName)
          }
      }

      //Adding  Total Defects Found to the Array
      for(var i=0;i<data.length;i++){
        if(data[i].versionData != undefined){
          if(data[i].versionData.defectsFound !=undefined){
          totaldefectsFound.push(data[i].versionData.defectsFound.total)
       }
       else{
           totaldefectsFound.push(0);
         }
     }
    }

        //Adding  Total Defects Resolved to the Array
        for(var i=0;i<data.length;i++){
        if(data[i].versionData != undefined){
          if(data[i].versionData.defectsResolved !=undefined){
         totalDefectsResolved.push(data[i].versionData.defectsResolved.total)
       }
       else{
           totalDefectsResolved.push(0);
       }
     }
   }


       //Release Graph Generation
       $scope.releaseGraph = c3.generate({
                bindto: '#releaseGraph', 
                             
                data: {
                  columns: [
                    noOfStoryCompleted,noOfStoryPoints,totaldefectsFound,totalDefectsResolved
                  ],
                  type: 'spline'
                },
                axis: {
                    x:{
                        type: 'category',
                        categories: releaseName
                    }
                },
                color: {
                pattern: ['#00cc00','#3399ff','#000','#cc6699']
            }
  
            });

          
        }
    }
})();