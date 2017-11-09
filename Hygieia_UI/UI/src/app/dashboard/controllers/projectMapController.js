(function() {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('projectMapController', projectMapController);

    projectMapController.$inject = ['$scope', 'codeAnalysisData', 'testSuiteData', '$q', '$filter', '$uibModal', '$location', '$routeParams', '$http', '$route', '$cookies', '$timeout', '$cookieStore', '$rootScope', 'dashboardData', 'projectData'];

    function projectMapController($scope, codeAnalysisData, testSuiteData, $q, $filter, $uibModal, $location, $routeParams, $http, $route, $cookies, $timeout, $cookieStore, $rootScope, dashboardData, projectData) {
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
        projectData.fetchallprojects(ctrl.usernamepro).then(function(response) {
            ctrl.getAllProjects = response;
            ctrl.sysadmincheck = $cookies.get('sysAdmin');
            if (ctrl.sysadmincheck == "true") {
                ctrl.CreateProjects = true;
                ctrl.viewProjects = true;
                 ctrl.usermanagementviews = true;
            } else {
                for (var i = 0; i < ctrl.getAllProjects.length; i++) {
                    for (var j = 0; j < ctrl.getAllProjects[i].usersGroup.length; j++) {
                        for (var k = 0; k < ctrl.getAllProjects[i].usersGroup[j].userRoles.length; k++) {
                            ctrl.vvv = ctrl.getAllProjects[i].usersGroup[j].user;
                            ctrl.projectIDS = ctrl.getAllProjects[i].id;

                            if ((ctrl.getAllProjects[i].usersGroup[j].userRoles[k].permissions.indexOf("ADD_PROJECT") > -1) && (ctrl.vvv == ctrl.usernamepro)) {
                                ctrl.CreateProjects = true;
                            }
                            if ((ctrl.getAllProjects[i].usersGroup[j].userRoles[k].permissions.indexOf("VIEW_PROJECT_LIST") > -1) && (ctrl.vvv == ctrl.usernamepro)) {
                                ctrl.viewProjects = true;
                            }
                             if((ctrl.getAllProjects[i].usersGroup[j].userRoles[k].permissions.indexOf("USER_MANAGEMENT_VIEW") > -1) && (ctrl.vvv == ctrl.usernamepro)){
                               ctrl.usermanagementviews = true;
                            }

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
        function shareProjectController($uibModalInstance, proid, $route, $scope, id, $cookies) {
            var sp = this;
            sp.selected = '';
            sp.projectId = proid;
            sp.id = id;
            sp.usernameproject = $cookies.get('username');

            //Fetch all users
            projectData.fetchallusers(sp.id).then(function(response) {
                sp.getUserMaps = response;
            });

            //Fetch all dashboards
            projectData.fetchalldashboard(sp.usernameproject, sp.id).then(function(response) {
                response.data.selectedItemsDashboard = [];
                sp.getdashboards = response.data;
            });

            //Fetch all roles that is displayed in dual list
            projectData.fetchallroles().then(function(response) {
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
                if (sp.getRolesKey.selectedItems != 0) {
                    $http.post("/api/projectUsersMapping", (sp.projectUserPayl)).then(function(response) {
                        $uibModalInstance.dismiss("cancel");
                        $route.reload();
                        $uibModal.open({
                            templateUrl: 'app/dashboard/views/ConfirmationModals/adduserConfirm.html',
                            controller: 'projectMapController',
                            controllerAs: 'pm'
                        });
                    })
                } else {
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
                    projectData.editprojectfn(ctrl.editPayload).then(function(response) {
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
                projectData.deleteProjectsfn(pid).then(function(response) {
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

                    projectData.postProjectfn(dpmObjpos.payl).then(function(response) {
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
        $scope.showDahboardPage = function(ProId, ProName, projectspcID) {
            dashboardData.mydashboard(ProId, ctrl.usernamepro).then(function() {});
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

        ctrl.addDefect  = function(probj){
                $uibModal.open({
                templateUrl: 'app/dashboard/views/addDefect.html',
                windowClass: 'app-modal-window-defect',
                controller: addDefectController,
                controllerAs: 'adc',
                resolve: {
                    name: function() {
                        return probj.projectName;
                    },
                    id: function() {
                        return probj.projectId;
                    }
                }
            });
             }

             function addDefectController($uibModalInstance, $http, $route, $timeout, $scope, $cookies, name, id) {
             
                var ctrl = this;
                
              ctrl.postDefect = function(probj) {
                

                ctrl.payloadDefect = {
                "projectName": name,
                "metricsProjectId": id,
                "projectId": id,
                "valueAsOn": "test",

                "lowPriorityDefectsCount": ctrl.lowPriorityDefectsCount,
                "mediumPriorityDefectsCount": ctrl.mediumPriorityDefectsCount,
                "highPriorityDefectsCount": ctrl.highPriorityDefectsCount,
                "criticalPriorityDefectsCount": ctrl.criticalPriorityDefectsCount,

                "qaDefects": ctrl.QA_DefectsCount,
                "uatDefects": ctrl.UAT_DefectsCount,
                "prodDefects": ctrl.PROD_DefectsCount,

                "openDefectsWithHighPriorityAndAgeLessThanOrEQ15Days": ctrl.openDefectsWithHighPriorityAndAgeLessThanOrEQ15Days,
                "openDefectsWithLowPriorityAndAgeLessThanOrEQ15Days": ctrl.openDefectsWithLowPriorityAndAgeLessThanOrEQ15Days,
                "openDefectsWithMediumPriorityAndAgeLessThanOrEQ15Days": ctrl.openDefectsWithMediumPriorityAndAgeLessThanOrEQ15Days,
                "openDefectsWithCriticalPriorityAndAgeLessThanOrEQ15Days": ctrl.openDefectsWithCriticalPriorityAndAgeLessThanOrEQ15Days,
                

                "openDefectsWithHighPriorityAndAgeBetween15To30Days": ctrl.openDefectsWithHighPriorityAndAgeBetween15To30Days,
                "openDefectsWithLowPriorityAndAgeBetween15To30Days": ctrl.openDefectsWithLowPriorityAndAgeBetween15To30Days,
                "openDefectsWithMediumPriorityAndAgeBetween15To30Days": ctrl.openDefectsWithMediumPriorityAndAgeBetween15To30Days,
                "openDefectsWithCriticalPriorityAndAgeBetween15To30Days": ctrl.openDefectsWithCriticalPriorityAndAgeBetween15To30Days,
                
                "openDefectsWithHighPriorityAndAgeBetween30To60Days": ctrl.openDefectsWithHighPriorityAndAgeBetween30To60Days,
                "openDefectsWithLowPriorityAndAgeBetween30To60Days": ctrl.openDefectsWithLowPriorityAndAgeBetween30To60Days,
                "openDefectsWithMediumPriorityAndAgeBetween30To60Days": ctrl.openDefectsWithMediumPriorityAndAgeBetween30To60Days,
                "openDefectsWithCriticalPriorityAndAgeBetween30To60Days": ctrl.openDefectsWithCriticalPriorityAndAgeBetween30To60Days,
                
                "openDefectsWithHighPriorityAndAgeBetween60To90Days": ctrl.openDefectsWithHighPriorityAndAgeBetween60To90Days,
                "openDefectsWithLowPriorityAndAgeBetween60To90Days": ctrl.openDefectsWithLowPriorityAndAgeBetween60To90Days,
                "openDefectsWithMediumPriorityAndAgeBetween60To90Days": ctrl.openDefectsWithMediumPriorityAndAgeBetween60To90Days,
                "openDefectsWithCriticalPriorityAndAgeBetween60To90Days": ctrl.openDefectsWithCriticalPriorityAndAgeBetween60To90Days,
                
                "openDefectsWithHighPriorityAndAgeGreaterThan90": ctrl.openDefectsWithHighPriorityAndAgeGreaterThan90,
                "openDefectsWithLowPriorityAndAgeGreaterThan90": ctrl.openDefectsWithLowPriorityAndAgeGreaterThan90,
                "openDefectsWithMediumPriorityAndAgeGreaterThan90": ctrl.openDefectsWithMediumPriorityAndAgeGreaterThan90,
                "openDefectsWithCriticalPriorityAndAgeGreaterThan90": ctrl.openDefectsWithCriticalPriorityAndAgeGreaterThan90,
                
                "fixedDefectsWithHighPriorityAndResolutionLessThanOrEQ15Days": ctrl.fixedDefectsWithHighPriorityAndResolutionLessThanOrEQ15Days,
                "fixedDefectsWithLowPriorityAndResolutionLessThanOrEQ15Days": ctrl.fixedDefectsWithLowPriorityAndResolutionLessThanOrEQ15Days,
                "fixedDefectsWithMediumPriorityAndResolutionLessThanOrEQ15Days": ctrl.fixedDefectsWithMediumPriorityAndResolutionLessThanOrEQ15Days,
                "fixedDefectsWithCriticalPriorityAndResolutionLessThanOrEQ15Days": ctrl.fixedDefectsWithCriticalPriorityAndResolutionLessThanOrEQ15Days,
                
                "fixedDefectsWithHighPriorityAndResolutionBetween15To30Days": ctrl.fixedDefectsWithHighPriorityAndResolutionBetween15To30Days,
                "fixedDefectsWithLowPriorityAndResolutionBetween15To30Days": ctrl.fixedDefectsWithLowPriorityAndResolutionBetween15To30Days,
                "fixedDefectsWithMediumPriorityAndResolutionBetween15To30Days": ctrl.fixedDefectsWithMediumPriorityAndResolutionBetween15To30Days,
                "fixedDefectsWithCriticalPriorityAndResolutionBetween15To30Days": ctrl.fixedDefectsWithCriticalPriorityAndResolutionBetween15To30Days,
                
                "fixedDefectsWithHighPriorityAndResolutionBetween30To60Days": ctrl.fixedDefectsWithHighPriorityAndResolutionBetween30To60Days,
                "fixedDefectsWithLowPriorityAndResolutionBetween30To60Days": ctrl.fixedDefectsWithLowPriorityAndResolutionBetween30To60Days,
                "fixedDefectsWithMediumPriorityAndResolutionBetween30To60Days": ctrl.fixedDefectsWithMediumPriorityAndResolutionBetween30To60Days,
                "fixedDefectsWithCriticalPriorityAndResolutionBetween30To60Days": ctrl.fixedDefectsWithCriticalPriorityAndResolutionBetween30To60Days,


                "fixedDefectsWithHighPriorityAndResolutionBetween60To90Days": ctrl.fixedDefectsWithHighPriorityAndResolutionBetween60To90Days,
                "fixedDefectsWithLowPriorityAndResolutionBetween60To90Days": ctrl.fixedDefectsWithLowPriorityAndResolutionBetween60To90Days,
                "fixedDefectsWithMediumPriorityAndResolutionBetween60To90Days": ctrl.fixedDefectsWithMediumPriorityAndResolutionBetween60To90Days,
                "fixedDefectsWithCriticalPriorityAndResolutionBetween60To90Days": ctrl.fixedDefectsWithCriticalPriorityAndResolutionBetween60To90Days,
                
                "fixedDefectsWithHighPriorityAndResolutionGreaterThan90": ctrl.fixedDefectsWithHighPriorityAndResolutionGreaterThan90,
                "fixedDefectsWithLowPriorityAndResolutionGreaterThan90": ctrl.fixedDefectsWithLowPriorityAndResolutionGreaterThan90,
                "fixedDefectsWithMediumPriorityAndResolutionGreaterThan90": ctrl.fixedDefectsWithMediumPriorityAndResolutionGreaterThan90,
                "fixedDefectsWithCriticalPriorityAndResolutionGreaterThan90": ctrl.fixedDefectsWithCriticalPriorityAndResolutionGreaterThan90
            }
             projectData.postDefect(ctrl.payloadDefect).then(function(response) {
                $uibModalInstance.dismiss("cancel");
               $uibModal.open({
                            templateUrl: 'app/dashboard/views/ConfirmationModals/defectaddConfirm.html',
                            controller: 'projectMapController',
                            controllerAs: 'pm'
                        });

             })
            /* $http.post("/api/defectSummery", (ctrl.payloadDefect)).then(function(response) {
                alert("success");
             })*/

              }

             }

              ctrl.addRelease  = function(proje){
                $uibModal.open({
                templateUrl: 'app/dashboard/views/addRelease.html',
                windowClass: 'app-modal-window-defect',
                controller: addReleaseController,
                controllerAs: 'arc',
                 resolve: {
                    name: function() {
                        return proje.projectName;
                    },
                    id: function() {
                        return proje.projectId;
                    }
                }
                
            });


             }

             function addReleaseController($uibModalInstance, $http, $route, $timeout, $scope, $cookies, name, id) {
                var ctrl = this;
               
                ctrl.postRelease = function(proje) {
                 ctrl.releasePayload = {
                    "projectName":name,
                    "projectId":id,
                    "releaseId" : ctrl.releaseId,
                    "name":ctrl.name,

                    "startDate":ctrl.startDate,
                    "releaseDate":ctrl.releaseDate,
                    "released":ctrl.released,

                    "description":ctrl.description,

                    "criticalDefectsFound":ctrl.criticalDefectsFound,
                    "highDefectsFound":ctrl.highDefectsFound,
                    "mediumDefectsFound":ctrl.mediumDefectsFound,
                    "lowDefectsFound":ctrl.lowDefectsFound,

                    "criticalDefectsClosed":ctrl.criticalDefectsClosed,
                    "highDefectsClosed":ctrl.highDefectsClosed,
                    "mediumDefectsClosed":ctrl.mediumDefectsClosed,
                    "lowDefectsClosed":ctrl.lowDefectsClosed,

                    "criticalDefectsUnresolved":ctrl.criticalDefectsUnresolved,
                    "highDefectsUnresolved":ctrl.highDefectsUnresolved,
                    "mediumDefectsUnresolved":ctrl.mediumDefectsUnresolved,
                    "lowDefectsUnresolved":ctrl.lowDefectsUnresolved,

                    "noofStoryCompleted":ctrl.noofStoryCompleted,
                    "noofStoryCommitted":ctrl.noofStoryCommitted
                }
                 $http.post("/api/releaseMetrcis", (ctrl.releasePayload)).then(function(response) {
                    $uibModalInstance.dismiss("cancel");
                    $uibModal.open({
                            templateUrl: 'app/dashboard/views/ConfirmationModals/releaseaddConfirm.html',
                            controller: 'projectMapController',
                            controllerAs: 'pm'
                        });
             })

              }

             }

               ctrl.addSprint  = function(proje){
                $uibModal.open({
                templateUrl: 'app/dashboard/views/addSprint.html',
                windowClass: 'app-modal-window-defect',
                controller: addSprintController,
                controllerAs: 'asc',
                 resolve: {
                    name: function() {
                        return proje.projectName;
                    },
                    id: function() {
                        return proje.projectId;
                    }
                }
                
            });


             }


             function addSprintController($uibModalInstance, $http, $route, $timeout, $scope, $cookies, name, id) {
                var ctrl = this;
                
                ctrl.postSprint = function(proje) {

                 ctrl.sprintPayload = {
                "sprintId":ctrl.sprintId,
                "sprintName":ctrl.sprintName,
                "desc":ctrl.desc,

                "committedStoriesCount":ctrl.committedStoriesCount,
                "committedStoryPoints":ctrl.committedStoryPoints,
                "completedStoriesCount":ctrl.completedStoriesCount,
                "completedStoryPoints":ctrl.completedStoryPoints,

                "storiesAdded":ctrl.storiesAdded,
                "storypointsAdded":ctrl.storypointsAdded,

                "storiesRemoed":ctrl.storiesRemoed,
                "storypointsRemoed":ctrl.storypointsRemoed,

           
                "efforts":ctrl.efforts,

                "criticalDefectsFound":ctrl.criticalDefectsFound,
                "mediumDefectsFound":ctrl.mediumDefectsFound,
                "lowDefectsFound":ctrl.lowDefectsFound,
                "highDefectsFound":ctrl.highDefectsFound,

                "criticalDefectsClosed":ctrl.criticalDefectsClosed,
                "mediumDefectsClosed":ctrl.mediumDefectsClosed,
                "lowDefectsClosed":ctrl.lowDefectsClosed,
                "highDefectsClosed":ctrl.highDefectsClosed,

                "criticalDefectsUnresolved":ctrl.criticalDefectsUnresolved,
                "mediumDefectsUnresolved":ctrl.mediumDefectsUnresolved,
                "lowDefectsUnresolved":ctrl.lowDefectsUnresolved,
                "highDefectsUnresolved":ctrl.highDefectsUnresolved,
                
                "endDate":ctrl.endDate,
                "startDate":ctrl.startDate,
                "projectId":id,
                 "projectName":name
                }

                 $http.post("/api//sprintMetrics", (ctrl.sprintPayload)).then(function(response) {
                    $uibModalInstance.dismiss("cancel");
                    $uibModal.open({
                            templateUrl: 'app/dashboard/views/ConfirmationModals/releaseaddConfirm.html',
                            controller: 'projectMapController',
                            controllerAs: 'pm'
                        });
             })

              }

             }
    }
})();