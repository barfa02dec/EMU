(function() {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('projectMapController', projectMapController);

    projectMapController.$inject = ['$scope', 'codeAnalysisData', 'testSuiteData', '$q', '$filter', '$uibModal', '$location', '$routeParams', '$http', '$route', '$cookies', '$timeout', '$cookieStore', '$rootScope','dashboardData'];

    function projectMapController($scope, codeAnalysisData, testSuiteData, $q, $filter, $uibModal, $location, $routeParams, $http, $route, $cookies, $timeout, $cookieStore, $rootScope,dashboardData) {
        var ctrl = this;
        ctrl.usernamepro = $cookies.get('username');
        ctrl.showAddPopUpBox = false;
        ctrl.editorEnabled = false;
        ctrl.title = "THB";
        $scope.selected = '';

        ctrl.payl = {
            "projectId": ctrl.projectId,
            "projectName": ctrl.projectName,
            "projectOwner": ctrl.projectOwner,
            "client": ctrl.client,
            "businessUnit": ctrl.businessUnit,
            "program": ctrl.program
        }

  
       
        $scope.curPage = 0;
        ctrl.pageSize = 5;

        //Get All Projects
        $http.get("/api/getProjectsByUser/?username=" + ctrl.usernamepro)
            .then(function(response) {
                ctrl.getAllProjects = response.data;
                for (var i = 0; i < ctrl.getAllProjects.length; i++) {
                    for (var j = 0; j < ctrl.getAllProjects[i].usersGroup.length; j++) {
                        for (var k = 0; k < ctrl.getAllProjects[i].usersGroup[j].userRoles.length; k++) {
                            ctrl.vvv = ctrl.getAllProjects[i].usersGroup[j].user;
                            ctrl.projectIDS = ctrl.getAllProjects[i].id;
                            if((ctrl.getAllProjects[i].usersGroup[j].userRoles[k].permissions.indexOf("ADD_PROJECT") > -1) && (ctrl.vvv == ctrl.usernamepro)){
                               ctrl.CreateProjects = true;
                            }
                        }
                    }
                }
            });

        ctrl.numberOfPages = function() {
            return Math.ceil(ctrl.getAllProjects.length / ctrl.pageSize);
        };

        angular.module(HygieiaConfig.module).filter('pagination', function() {
            return function(input, start) {
                start = +start;
                return input.slice(start);
            };
        });

      //open create Project Modal
        ctrl.createDashboard = function() {
            $uibModal.open({
                templateUrl: 'app/dashboard/views/createProject.html',
                controller: postProjetController,
                controllerAs: 'cpr',
                backdrop: 'static'
            });
        }

        //open add user Modal
        ctrl.shareProjectPopUp = function(prob) {
            ctrl.ProObjData = prob;
            $uibModal.open({
                templateUrl: 'app/dashboard/views/shareProject.html',
                controller: shareProjectController,
                controllerAs: 'spc',
                resolve: {
                    proid: function() {
                        return prob.projectId;
                    },
                    id: function() {
                        return prob.id;
                    }

                }
            });
        }

        //Add User functionality
        function shareProjectController($uibModalInstance, proid, $route, $scope,id,$cookies) {
            var sp = this;
            sp.selected = '';
            sp.projectId = proid;
            sp.id = id;
            sp.usernameproject = $cookies.get('username');
            
            //Fetch all users
            $http.get("/api/getApplicationUsers/" + sp.id)
                .then(function(response) {
                    sp.getUserMaps = response.data;
                });

            //Fetch all dashboards
             var mydashboardRouteProMap = "/api/dashboard/mydashboard"; 
             $http.get(mydashboardRouteProMap + "?username=" + sp.usernameproject + "&projectId=" + sp.id)
                .then(function(response) {
                    response.data.selectedItemsDashboard = [];
                    sp.getdashboards = response.data;
                });

            //Fetch all roles that is displayed in dual list
            $http.get("/api/allActiveEngineeringDashboardUserRoles")
                .then(function(response) {
                    response.data.selectedItems = [];
                    sp.getRolesKey = response.data;
                });

            sp.shareProjects = function(prob) {
                var aa = sp.selected;
                sp.projectId = proid;
                sp.projectUserPayl = {
                    "user": sp.selected,
                    "projectId": sp.projectId,
                    "userRoles": sp.getRolesKey.selectedItems,
                    "dashboardsToAssign": sp.getdashboards.selectedItemsDashboard
                }
                if(sp.getRolesKey.selectedItems !=0){
                $http.post("/api/projectUsersMapping", (sp.projectUserPayl)).then(function(response) {
                    $uibModalInstance.dismiss("cancel");
                    $route.reload();
                    $uibModal.open({
                        templateUrl: 'app/dashboard/views/ConfirmationModals/adduserConfirm.html',
                        controller: 'projectMapController',
                        controllerAs: 'pm'
                    });
                })
            }
            else{
                $uibModal.open({
                        templateUrl: 'app/dashboard/views/ConfirmationModals/adduserrequired.html',
                        controller: 'projectMapController',
                        controllerAs: 'pm'
                    });
            }
            };

            //Adding Role to User Dual List Functionality
            sp.transfer = function(from, to, index) {
                if (index >= 0) {
                    to.push(from[index]);
                    from.splice(index, 1);
                } else {
                    for (var i = 0; i < from.length; i++) {
                        to.push(from[i]);
                    }
                    from.length = 0;
                }
            };
        }

        //Enable Edit
        ctrl.enableEditor = function(vall) {
            angular.forEach(ctrl.getAllProjects, function(value, key) {
                if (value.id === vall) {
                    value.editorEnabled = true;
                }
            });

        };

        //Disable Edit
        ctrl.disableEditor = function() {
            ctrl.editorEnabled = false;
        };

        //Edit Project 
        ctrl.editproject = function(info) {
            console.log(info);
            ctrl.usernamepro = $cookies.get('username');
            ctrl.editPayload = {
                "projectId": info.projectId,
                "projectName": info.projectName,
                "projectOwner": info.projectOwner,
                "client": info.client,
                "businessUnit": info.businessUnit,
                "program": info.program,
                "user": ctrl.usernamepro,
                "editorEnabled": info.editorEnabled,
                "id": info.id
            }
            info.editorEnabled = false;
            console.log(info);
            var apiHost = ' http://localhost:3000';
            var qahost = 'http://10.20.1.183:3000';
            if ((info.businessUnit) && (info.projectId) && (info.client) && (info.projectOwner)) {
                if ((info.businessUnit.length >= 3) && (info.projectId.length >= 3) && (info.client.length >= 3) && (info.projectOwner.length >= 3) && (info.program.length >= 3)) {
                    $http.post('/api/updateProject', (ctrl.editPayload)).then(function(response) {
                        $uibModal.open({
                            templateUrl: 'app/dashboard/views/ConfirmationModals/editConfirm.html',
                            controller: 'projectMapController',
                            controllerAs: 'pm'
                        });
                    }, function(response) {
                        if (response.status > 204 && response.status <= 500) {
                            info.editorEnabled = true;
                            alert("Server Error");
                        }
                    })
                } else {
                    $uibModal.open({
                        templateUrl: 'app/dashboard/views/ConfirmationModals/validationminlength.html',
                        controller: 'projectMapController',
                        controllerAs: 'pm'
                    });
                    info.editorEnabled = true;
                }
            } else {
                $uibModal.open({
                    templateUrl: 'app/dashboard/views/ConfirmationModals/validationrequiredmessage.html',
                    controller: 'projectMapController',
                    controllerAs: 'pm'
                });
                info.editorEnabled = true;
            }
        }

        //logout functionality
        ctrl.logout = function() {
            $cookieStore.remove("username");
            $cookieStore.remove("authenticated");
            $location.path('/');
        };


        //Open Delete Project Modal
        ctrl.deleteProjectModel = function(pro) {
            console.log(pro);
            ctrl.proData = pro;
            $uibModal.open({
                templateUrl: 'app/dashboard/views/deleteModel.html',
                controller: delProjetController,
                controllerAs: 'dpc',
                resolve: {
                    pid: function() {
                        return pro.id;
                    }
                }
            });
        }

        //Delete Project Functionality
        function delProjetController($uibModalInstance, pid, $route) {
            var dpmObj = this;
            dpmObj.deleteProjects = function(pro) {
                $http.delete('/api//deleteProject/' + pid).then(function(response) {
                    $uibModalInstance.dismiss("cancel");
                    $route.reload();
                    $uibModal.open({
                        templateUrl: 'app/dashboard/views/ConfirmationModals/deleteconfirm.html',
                        controller: 'projectMapController',
                        controllerAs: 'pm'
                    });
                })
            };
        }

        //Create Project Functionality
        function postProjetController($uibModalInstance, $http, $route, $timeout, $scope, $cookies) {
            var dpmObjpos = this;
            dpmObjpos.usernamepro = $cookies.get('username');
            dpmObjpos.payl = {
                "projectId": ctrl.projectId,
                "projectName": ctrl.projectName,
                "projectOwner": ctrl.projectOwner,
                "client": ctrl.client,
                "businessUnit": ctrl.businessUnit,
                "program": ctrl.program,
                "user": dpmObjpos.usernamepro
            }
            dpmObjpos.postProject = function() {
                var apiHost = 'http://localhost:3000';
                if (dpmObjpos.createProModel.$valid == true) {
                    $http.post('/api/createProject ', (dpmObjpos.payl)).then(function(response) {
                        $route.reload();
                        $uibModalInstance.dismiss("cancel");
                        $uibModal.open({
                            templateUrl: 'app/dashboard/views/ConfirmationModals/postconfirm.html',
                            controller: 'projectMapController',
                            controllerAs: 'pm'
                        });
                    }, function(response) {
                        if (response.status == 409) {
                            $uibModal.open({
                                templateUrl: 'app/dashboard/views/ConfirmationModals/createErrorModal.html',
                                controller: 'projectMapController',
                                controllerAs: 'pm'
                            });
                        }
                    })
                } else {}
            };
        }

        //Show List of Dashboards Page for that particular project
        $scope.showDahboardPage = function(ProId, ProName,projectspcID) {
            dashboardData.mydashboard(ProId,ctrl.usernamepro).then(function(){
        });
            $location.path('/site/');
            $cookies.put('ProId', ProId);
            $cookies.put('ProName', ProName);
            $cookies.put('ProSpId', projectspcID);
        }

        //Add user Popup which matches the user to the project
        ctrl.UserMap = function() {
            $uibModal.open({
                templateUrl: 'app/dashboard/views/userPermissionMap.html',
                controller: 'projectMapController',
                controllerAs: 'pm'
            });
        }
    }
})();