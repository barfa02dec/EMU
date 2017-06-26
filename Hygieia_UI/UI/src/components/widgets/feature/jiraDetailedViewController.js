(function() {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('jiraDetailedViewController', jiraDetailedViewController);

    jiraDetailedViewController.$inject = ['$scope', 'codeAnalysisData', 'testSuiteData', '$q', '$filter', '$uibModal', '$location', '$routeParams', '$http','$cookieStore','$cookies'];

    function jiraDetailedViewController($scope, codeAnalysisData, testSuiteData, $q, $filter, $uibModal, $location, $routeParams, $http,$cookieStore,$cookies) {
        var ctrl = this;
       ctrl.componentId = $routeParams.componentId;

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

   $scope.configopenDef = {
     	bindto: "#openDef",
   data: {
        columns: [
            ['data1', 30, 200, 100, 400, 150, 250],
             ['data2', 130, 100, 140, 200, 150, 50],
            ['data3', 130, -150, 200, 300, -200, 100]
           
        ],
        type: 'bar',
        
    }

};

  $scope.configresTime = {
     	bindto: "#restime",
   data: {
        columns: [
            ['data1', 30, 200, 100, 400, 150, 250],
             ['data2', 130, 100, 140, 200, 150, 50],
            ['data3', 130, -150, 200, 300, -200, 100]
           
        ],
        type: 'bar',
        
    }

};

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
        
       }
})();