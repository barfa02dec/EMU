(function() {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('projectMapController', projectMapController);

    projectMapController.$inject = ['$scope', 'codeAnalysisData', 'testSuiteData', '$q', '$filter', '$uibModal', '$location', '$routeParams', '$http','$route','$cookies','$timeout','$uibModalInstance'];

    function projectMapController($scope, codeAnalysisData, testSuiteData, $q, $filter, $uibModal, $location, $routeParams, $http,$route,$cookies,$timeout,$uibModalInstance) {
        var ctrl = this;


        ctrl.showAddPopUpBox = false;
        ctrl.editorEnabled = false;
        ctrl.title = "THB";
        ctrl.payl = {
            "projectId": ctrl.projectId,
            "projectName": ctrl.projectName,

            "projectOwner": ctrl.projectOwner,
            "client": ctrl.client,
            "businessUnit": ctrl.businessUnit,
            "program": ctrl.program
        }

        var apiHost = 'http://localhost:3000';
        var qahost = 'http://10.20.1.183:3000';

        $http.get(apiHost + "/api/getProjects")
            .then(processCaResponse);

        function processCaResponse(response) {
            ctrl.getAllProjects = response.data;
            //response.data.projectName;

        }


        ctrl.createDashboard = function() {


            $uibModal.open({
                templateUrl: 'app/dashboard/views/createProject.html',
                controller: 'projectMapController',
                controllerAs: 'pm'
            });
        }
        ctrl.shareProjectPopUp = function() {


            $uibModal.open({
                templateUrl: 'app/dashboard/views/shareProject.html',
                controller: 'projectMapController',
                controllerAs: 'pm'
            });
        }

        ctrl.enableEditor = function(vall) {
            console.log(ctrl.getAllProjects);

            angular.forEach(ctrl.getAllProjects,  function(value,  key) {          
                if (value.projectId === vall) {
                    value.editorEnabled = true;
                    //value.editableTitle = value.projectName;

                }
            });

        };

        ctrl.disableEditor = function() {
            ctrl.editorEnabled = false;
        };

        ctrl.getProjectDetails = function() {

        };

        ctrl.postProject = function() {
            $uibModalInstance.dismiss();
            var apiHost = 'http://localhost:3000';
            if(ctrl.createProModel.$valid == true){
            $http.post(apiHost + '/api/createProject ', (ctrl.payl)).then(function(response) { 
                alert("Project Created Successfully");
                $route.reload();
        })
    }
        else{
            $scope.errormsg = "Please fill the required fields";
            $timeout(function() {
                        $scope.errormsg = "";
                    }, 3000);
        }

        };
        ctrl.editproject = function(info) {
            info.editorEnabled = false;
            console.log(info);
            var apiHost = ' http://localhost:3000'; 
            var qahost = 'http://10.20.1.183:3000'; 
            $http.post(apiHost + '/api/updateProject', (info)).then(function(response) {
                alert("Project updated Successfully");

            })

        }


         ctrl.deleteProjects = function(valle, id) {
            console.log(ctrl.getAllProjects);

            /*angular.forEach(ctrl.getAllProjects,  function(value,  key) {          
                if (value.projectId === valle) {
                    console.log(value.id);
                    alert("asdas");*/
                    $http.delete(apiHost + '/api//deleteProject/' + valle.id, (ctrl.payl)).then(function(response) {
                        alert("Project deleted Successfully");
                         var index=ctrl.getAllProjects.indexOf(valle);
                         ctrl.getAllProjects.splice(index,1);

                    })
                /*}
            });*/

        };

        ctrl.deleteProjectModel = function(pro) {
            console.log(pro);
            ctrl.proData = pro;
           
           
            $uibModal.open({
                templateUrl: 'app/dashboard/views/deleteModel.html',
                controller: 'projectMapController',
                controllerAs: 'pm'
            });
        }
        $scope.showDahboardPage = function(ProId) {
            $http.get(apiHost + '/api/dashboard/projectdashboard?projectId=' + ProId)
            .then(function(){


            });
            $location.path('/site/' + ProId);
             $cookies.put('ProId', ProId);
            /*$timeout(function(){
                $rootScope.ProId = ProId;
            $rootScope.$broadcast("ProId", ProId);
            })*/
        }

        /*var apiHost = 'http://localhost:3000';
        var qahost = 'http://10.20.1.183:3000';
       
        $http.get(apiHost + "/api//dashboard/projectdashboard?projectId=5941ed365785b2317c927fb4")
            .then(showDahboardPage);

        
        function showDahboardPage(response) {
            ctrl.getAlldashboard = response.data;
            

        }*/

    }
})();