(function() {
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
        $scope.selected = '';

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

        // $http.get(apiHost + "/api/getProjects/")
        //   .then(processCaResponse);

        //Get All Projects
        $http.get("/api/getProjectsByUser/?username=" + ctrl.usernamepro)
            .then(processCaResponse);

        function processCaResponse(response) {
            ctrl.getAllProjects = response.data;
        }

        //Add User AutoComplete API Fetch
        $http.get("/api/getApplicationUsers")
            .then(function(response) {
                ctrl.getUserMaps = response.data;
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
                    }
                }
            });
        }

        //Add User functionality
        function shareProjectController($uibModalInstance, proid, $route, $scope) {
            var sp = this;
            sp.selected = '';
              sp.projectId = proid;
            //Fetch all users
            $http.get("/api/getApplicationUsers")
                .then(function(response) {
                    sp.getUserMaps = response.data;
                });

            //Fetch all roles that is displayed in dual list
            $http.get("/api/allActiveEngineeringDashboardUserRoles")
                .then(function(response) {
                    response.data.selectedItems = [];
                    /*sp.selItem = response.data.selectedItems;
                    sp.newArr = [];
                    for (var i = 0; i < sp.selItem.length; i++) {
                        if (sp.selItem[i].name != undefined) {
                            sp.newArr.push(sp.selItem[i].name);
                        }
                    }*/
                    sp.getRolesKey = response.data;
                });
                
            sp.shareProjects = function(prob) {
                var aa = sp.selected;
                sp.projectId = proid;

                 sp.projectUserPayl = {
                "user": sp.selected,
                "projectId": sp.projectId,
                "userRoles":sp.getRolesKey.selectedItems
               }
                console.log(sp.projectUserPayl);
                $http.post("/api/projectUsersMapping", (sp.projectUserPayl)).then(function(response) {
                    alert('Project Mapped Successfully');
                    console.log(sp.projectUserPayl);
                   /* $uibModal.open({
                        templateUrl: 'app/dashboard/views/userPermissionMap.html',
                        controller: 'projectMapController',
                        controllerAs: 'pm'
                    });*/
                })
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
            info.editorEnabled = false;
            console.log(info);
            var apiHost = ' http://localhost:3000';
            var qahost = 'http://10.20.1.183:3000';
            if ((info.businessUnit) && (info.projectId) && (info.client) && (info.projectOwner)) {
                if ((info.businessUnit.length >= 3) && (info.projectId.length >= 3) && (info.client.length >= 3) && (info.projectOwner.length >= 3) && (info.program.length >= 3)) {
                    $http.post('/api/updateProject', (info)).then(function(response) {
                        $uibModal.open({
                            templateUrl: 'app/dashboard/views/editConfirm.html',
                            controller: 'projectMapController',
                            controllerAs: 'pm'
                        });

                    })
                } else {
                    $uibModal.open({
                        templateUrl: 'app/dashboard/views/validationminlength.html',
                        controller: 'projectMapController',
                        controllerAs: 'pm'
                    });
                    info.editorEnabled = true;

                }
            } else {
                $uibModal.open({
                    templateUrl: 'app/dashboard/views/validationrequiredmessage.html',
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
                        templateUrl: 'app/dashboard/views/deleteconfirm.html',
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
                            templateUrl: 'app/dashboard/views/postconfirm.html',
                            controller: 'projectMapController',
                            controllerAs: 'pm'
                        });
                    })
                } else {}

            };

        }

        //Show List of Dashboards Page for that particular project
        $scope.showDahboardPage = function(ProId, ProName) {
            $http.get('/api/dashboard/projectdashboard?projectId=' + ProId)
                .then(function() {});
            $location.path('/site/');
            $cookies.put('ProId', ProId);
            $cookies.put('ProName', ProName);
        }

        //Add user Popup which matches the user to the project
        ctrl.UserMap = function() {
            $uibModal.open({
                templateUrl: 'app/dashboard/views/userPermissionMap.html',
                controller: 'projectMapController',
                controllerAs: 'pm'
            });
        }


        /*ctrl.mapUserToProject = function(pname){
            ctrl.getProName = $cookies.get('ProName');
            ctrl.paylmapping = {
                "userName": ctrl.userNames
               }
            $http.post('/api/projectUsers?projectname=' + ctrl.getProName).then(function (response) {
            })
        }*/
    }
})();