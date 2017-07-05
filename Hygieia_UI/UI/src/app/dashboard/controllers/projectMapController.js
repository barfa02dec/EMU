(function () {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('projectMapController', projectMapController);

    projectMapController.$inject = ['$scope', 'codeAnalysisData', 'testSuiteData', '$q', '$filter', '$uibModal', '$location', '$routeParams', '$http', '$route', '$cookies', '$timeout', '$cookieStore'];

    function projectMapController($scope, codeAnalysisData, testSuiteData, $q, $filter, $uibModal, $location, $routeParams, $http, $route, $cookies, $timeout, $cookieStore) {
        var ctrl = this;
        ctrl.usernamepro = $cookies.get('username');
        
        ctrl.showAddPopUpBox = false;
        ctrl.editorEnabled = false;
        ctrl.title = "THB";
        ctrl.payl = {
            "projectId": ctrl.projectId,
            "projectName": ctrl.projectName,
            "projectOwner": ctrl.projectOwner,
            "client": ctrl.client,
            "businessUnit": ctrl.businessUnit,
            "program": ctrl.program,
            "projectUsersList":[]
        }

        var apiHost = 'http://localhost:3000';
        var qahost = 'http://10.20.1.183:3000';
        
       // $http.get(apiHost + "/api/getProjects/")
         //   .then(processCaResponse);

            $http.get(apiHost + "/api/getProjectsByUser/?username="+ctrl.usernamepro)
            .then(processCaResponse);

        function processCaResponse(response) {
            ctrl.getAllProjects = response.data;
            //response.data.projectName;
}


        ctrl.createDashboard = function () {


            $uibModal.open({
                templateUrl: 'app/dashboard/views/createProject.html',
                controller: postProjetController,
                controllerAs: 'cpr',
                backdrop: 'static'
            });
        }
        ctrl.shareProjectPopUp = function () {


            $uibModal.open({
                templateUrl: 'app/dashboard/views/shareProject.html',
                controller: 'projectMapController',
                controllerAs: 'pm'
            });
        }

        ctrl.enableEditor = function (vall) {
            console.log(ctrl.getAllProjects);

            angular.forEach(ctrl.getAllProjects, function (value, key) {
                if (value.id === vall) {
                    value.editorEnabled = true;
                    //value.editableTitle = value.projectName;

                }
            });

        };

        ctrl.disableEditor = function () {
            ctrl.editorEnabled = false;
        };

        


        ctrl.editproject = function (info) {
            info.editorEnabled = false;
            console.log(info);
            var apiHost = ' http://localhost:3000';
            var qahost = 'http://10.20.1.183:3000';
            if ((info.businessUnit) && (info.projectId) && (info.client) && (info.projectOwner)) {
                if ((info.businessUnit.length >= 3) && (info.projectId.length >= 3) && (info.client.length >= 3) && (info.projectOwner.length >= 3) && (info.program.length >= 3)) {
                    $http.post(apiHost + '/api/updateProject', (info)).then(function (response) {
                        $uibModal.open({
                            templateUrl: 'app/dashboard/views/editConfirm.html',
                            controller: 'projectMapController',
                            controllerAs: 'pm'
                        });

                    })
                }
                else {
                    alert("Fields should have more than 2 characters");
                    info.editorEnabled = true;

                }
            } else {
                alert("Fields cannot be empty");
                info.editorEnabled = true;
            }
        }
        ctrl.logout = function () {
            $cookieStore.remove("username");
            $cookieStore.remove("authenticated");
            $location.path('/');
        };



        ctrl.deleteProjectModel = function (pro) {
            console.log(pro);
            ctrl.proData = pro;


            $uibModal.open({
                templateUrl: 'app/dashboard/views/deleteModel.html',
                controller: delProjetController,
                controllerAs: 'dpc',
                resolve: {
                    pid: function () {
                        return pro.id;
                    }
                }

            });
        }

        function delProjetController($uibModalInstance, pid, $route) {

            var dpmObj = this;

            dpmObj.deleteProjects = function (pro) {


                $http.delete(apiHost + '/api//deleteProject/' + pid).then(function (response) {
                    $uibModalInstance.dismiss("cancel");
                    $route.reload();
                    $uibModal.open({
                        templateUrl: 'app/dashboard/views/deleteconfirm.html',
                        controller: 'projectMapController',
                        controllerAs: 'pm'
                    });
                })
            };
        }


        function postProjetController($uibModalInstance, $http, $route, $timeout, $scope,$cookies) {
           
       
            var dpmObjpos = this;
             dpmObjpos.usernamepro = $cookies.get('username');
            dpmObjpos.payl = {
                "projectId": ctrl.projectId,
                "projectName": ctrl.projectName,

                "projectOwner": ctrl.projectOwner,
                "client": ctrl.client,
                "businessUnit": ctrl.businessUnit,
                "program": ctrl.program,
                "projectUsersList":[dpmObjpos.usernamepro]
            }
            dpmObjpos.postProject = function () {

                var apiHost = 'http://localhost:3000';
                if (dpmObjpos.createProModel.$valid == true) {
                    $http.post(apiHost + '/api/createProject ', (dpmObjpos.payl)).then(function (response) {

                        $route.reload();
                        $uibModalInstance.dismiss("cancel");
                        $uibModal.open({
                            templateUrl: 'app/dashboard/views/postconfirm.html',
                            controller: 'projectMapController',
                            controllerAs: 'pm'
                        });


                    })
                } else {
                    $scope.errormsg = "Please fill the required fields";
                    $timeout(function () {
                        $scope.errormsg = "";
                    }, 3000);
                }

            };
        }

        $scope.showDahboardPage = function (ProId) {
            $http.get(apiHost + '/api/dashboard/projectdashboard?projectId=' + ProId)
                .then(function () {


                });
            $location.path('/site/');
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