(function () {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('projectMapController', projectMapController);

    projectMapController.$inject = ['$scope', 'codeAnalysisData', 'testSuiteData', '$q', '$filter', '$uibModal', '$location', '$routeParams', '$http', '$route', '$cookies', '$timeout', '$cookieStore', '$rootScope', 'dashboardData', 'projectData', 'featureData'];

    function projectMapController($scope, codeAnalysisData, testSuiteData, $q, $filter, $uibModal, $location, $routeParams, $http, $route, $cookies, $timeout, $cookieStore, $rootScope, dashboardData, projectData, featureData) {
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
        ctrl.pageSize = 10;


        $scope.adduser = "User created succcessfully";

        $scope.addSprint = "Sprint added succcessfully";

        $scope.updateSprint = "Sprint updated succcessfully";

        $scope.addRelease = "Release added succcessfully";

        $scope.updateRelease = "Release updated succcessfully";

        $scope.addDefect = "Defect added succcessfully";

        $scope.updateDefect = "Defect updated succcessfully";

        $scope.postProject = "Project created successfully";

        $scope.deleteProject = "Project deleted successfully";

        $scope.adduserrequired = "Please fill the required fields";

        $scope.validationrequired = "Field cannot be empty";

        $scope.editConfirm = "Project updated successfully";

        $scope.addHeatMap = "Heatmap added succcessfully";

        $scope.updateHeatMap = "Heatmap updated successfully";

        $scope.confirmButton = function () {
            alert("asas");
            $scope.$modalInstance.close();
            $scope.$modalInstance.dismiss('cancel');
        };

        $scope.cancel = function () {
            $scope.$modalInstance.dismiss('cancel');
        };


        //Get All Projects
        projectData.fetchallprojects(ctrl.usernamepro).then(function (response) {
            ctrl.getAllProjects = response;
            ctrl.numberOfPages = Math.ceil(ctrl.getAllProjects.length / ctrl.pageSize);
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
                            if ((ctrl.getAllProjects[i].usersGroup[j].userRoles[k].permissions.indexOf("USER_MANAGEMENT_VIEW") > -1) && (ctrl.vvv == ctrl.usernamepro)) {
                                ctrl.usermanagementviews = true;
                            }

                        }
                    }
                }
            }
        });




        angular.module(HygieiaConfig.module).filter('pagination', function () {
            return function (input, start) {
                start = +start;
                return input.slice(start);
            };
        });

        //open create Project Modal
        ctrl.createDashboard = function () {
            $uibModal.open({
                templateUrl: 'app/dashboard/views/createProject.html',
                controller: postProjetController,
                controllerAs: 'cpr',
                backdrop: 'static'
            });
        }

        //open add user Modal
        ctrl.shareProjectPopUp = function (prob) {
            ctrl.ProObjData = prob;
            $uibModal.open({
                templateUrl: 'app/dashboard/views/shareProject.html',
                controller: shareProjectController,
                controllerAs: 'spc',
                resolve: {
                    proid: function () {
                        return prob.projectId;
                    },
                    id: function () {
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
            projectData.fetchallusers(sp.id).then(function (response) {
                sp.getUserMaps = response;
            });

            //Fetch all dashboards
            projectData.fetchalldashboard(sp.usernameproject, sp.id).then(function (response) {
                response.data.selectedItemsDashboard = [];
                sp.getdashboards = response.data;
            });

            //Fetch all roles that is displayed in dual list
            projectData.fetchallroles().then(function (response) {
                response.data.selectedItems = [];
                sp.getRolesKey = response.data;
            });

            sp.shareProjects = function (prob) {
                var aa = sp.selected;
                sp.projectId = proid;
                sp.projectUserPayl = {
                    "user": sp.selected,
                    "projectId": sp.projectId,
                    "userRoles": sp.getRolesKey.selectedItems,
                    "dashboardsToAssign": sp.getdashboards.selectedItemsDashboard
                }
                if (sp.getRolesKey.selectedItems != 0) {
                    $http.post("/api/projectUsersMapping", (sp.projectUserPayl)).then(function (response) {
                        $uibModalInstance.dismiss("cancel");
                        $route.reload();
                        $uibModal.open({
                            template: '<confirm-popup msg="adduser" icon="btn btn-info project-map-add-btn inner-btn-prop" action="$close()"></confirm-popup>',
                            controller: 'projectMapController',
                            controllerAs: 'pm'
                        });
                    })
                } else {
                    $uibModal.open({
                        template: '<confirm-popup msg="adduserrequired" icon="btn btn-warning" action="$close()"></confirm-popup>',
                        controller: 'projectMapController',
                        controllerAs: 'pm'
                    });
                }
            };

            //Adding Role to User Dual List Functionality
            sp.transfer = function (from, to, index) {
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
        ctrl.enableEditor = function (vall) {
            angular.forEach(ctrl.getAllProjects, function (value, key) {
                if (value.id === vall) {
                    value.editorEnabled = true;
                }
            });
        };

        //Disable Edit
        ctrl.disableEditor = function () {
            ctrl.editorEnabled = false;
        };

        //Edit Project 
        ctrl.editproject = function (info) {
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
            var apiHost = ' http://localhost:3000';
            var qahost = 'http://10.20.1.183:3000';
            if ((info.businessUnit) && (info.projectId) && (info.client) && (info.projectOwner)) {
                if ((info.businessUnit.length >= 3) && (info.projectId.length >= 3) && (info.client.length >= 3) && (info.projectOwner.length >= 3) && (info.program.length >= 3)) {
                    projectData.editprojectfn(ctrl.editPayload).then(function (response) {
                        $uibModal.open({
                            template: '<confirm-popup msg="editConfirm" icon="btn btn-info project-map-add-btn inner-btn-prop" action="$close()"></confirm-popup>',
                            controller: 'projectMapController',
                            controllerAs: 'pm'
                        });

                    }, function (response) {
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
                    template: '<confirm-popup msg="validationrequired" icon="btn btn-warning" action="$close()"></confirm-popup>',
                    controller: 'projectMapController',
                    controllerAs: 'pm'
                });
                info.editorEnabled = true;
            }
        }

        //logout functionality
        ctrl.logout = function () {
            $cookieStore.remove("username");
            $cookieStore.remove("authenticated");
            $location.path('/');
        };

        //Open Delete Project Modal
        ctrl.deleteProjectModel = function (pro) {
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

        //Delete Project Functionality
        function delProjetController($uibModalInstance, pid, $route) {
            var dpmObj = this;
            dpmObj.deleteProjects = function (pro) {
                projectData.deleteProjectsfn(pid).then(function (response) {
                    $uibModalInstance.dismiss("cancel");
                    $route.reload();
                    $uibModal.open({
                        template: '<confirm-popup msg="deleteProject" icon="btn btn-info project-map-add-btn inner-btn-prop" action="$close()"></confirm-popup>',
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
            $scope.names = ["PES", "DBS", "IMSS", "CROSS BU"];
            projectData.fetchallCustomers().then(function (data) {
                dpmObjpos.getCustomers = data;
            });
            dpmObjpos.payl = {
                "projectId": ctrl.projectId,
                "projectName": ctrl.projectName,
                "projectOwner": ctrl.projectOwner,
                "client": ctrl.client,
                "businessUnit": ctrl.businessUnit,
                "program": ctrl.program,
                "user": dpmObjpos.usernamepro,
                //"id": "5addb5d20a78cb24c049ed5f",
                "customerName": ctrl.client,
                "customerCode": "87779",
                //"activate": "1",
                //"deactivate": "1"
            }
            dpmObjpos.postProject = function () {
                var apiHost = 'http://localhost:3000';
                if (dpmObjpos.createProModel.$valid == true) {

                    projectData.postProjectfn(dpmObjpos.payl).then(function (response) {
                        $route.reload();
                        $uibModalInstance.dismiss("cancel");
                        $uibModal.open({
                            template: '<confirm-popup msg="postProject" icon="btn btn-info project-map-add-btn inner-btn-prop" action="$close()"></confirm-popup>',
                            controller: 'projectMapController',
                            controllerAs: 'pm'
                        });

                    }, function (response) {
                        if (response.status == 409) {
                            $uibModal.open({
                                templateUrl: 'app/dashboard/views/ConfirmationModals/createErrorModal.html',
                                controller: 'projectMapController',
                                controllerAs: 'pm'
                            });
                        }
                    })

                } else { }
            };
        }

        //Show List of Dashboards Page for that particular project
        $scope.showDahboardPage = function (ProId, ProName, projectspcID) {
            dashboardData.mydashboard(ProId, ctrl.usernamepro).then(function () { });
            $location.path('/site/');
            $cookies.put('ProId', ProId);
            $cookies.put('ProName', ProName);
            $cookies.put('ProSpId', projectspcID);
            //featureData.ProSpId = projectspcID;
            //ctrl.ProSpId = featureData.ProSpId;
        }

        //Add user Popup which matches the user to the project
        ctrl.UserMap = function () {
            $uibModal.open({
                templateUrl: 'app/dashboard/views/userPermissionMap.html',
                controller: 'projectMapController',
                controllerAs: 'pm'
            });
        }

        ctrl.addDefect = function (probj) {
            $uibModal.open({
                templateUrl: 'app/dashboard/views/addDefect.html',
                windowClass: 'app-modal-window-defect',
                controller: addDefectController,
                controllerAs: 'adc',
                resolve: {
                    name: function () {
                        return probj.projectName;
                    },
                    id: function () {
                        return probj.projectId;
                    }
                }
            });
        }

        ctrl.editDefect = function (probj) {
            $uibModal.open({
                templateUrl: 'app/dashboard/views/editDefect.html',
                windowClass: 'app-modal-window-defect',
                controller: editDefectController,
                controllerAs: 'edc',
                resolve: {
                    name: function () {
                        return probj.projectName;
                    },
                    id: function () {
                        return probj.projectId;
                    }
                }
            });
        }

        function addDefectController($uibModalInstance, $http, $route, $timeout, $scope, $cookies, name, id) {
            var ctrl = this;
            ctrl.usernamepro = $cookies.get('username');
            ctrl.postDefect = function (probj) {
                ctrl.payloadDefect = {
                    "user": ctrl.usernamepro,
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
                projectData.postDefect(ctrl.payloadDefect).then(function (response) {
                    $uibModalInstance.dismiss("cancel");
                    $uibModal.open({
                        template: '<confirm-popup msg="addDefect" icon="btn btn-info project-map-add-btn inner-btn-prop" action="$close()"></confirm-popup>',
                        controller: 'projectMapController',
                        controllerAs: 'pm'
                    });
                })
            }
        }

        function editDefectController($uibModalInstance, $http, $route, $timeout, $scope, $cookies, name, id) {
            ctrl = this;
            ctrl.usernamepro = $cookies.get('username');
            featureData.jiraData(id).then(jiraDataFetch);

            function jiraDataFetch(data) {
                ctrl.dataEdit = data;
            }

            ctrl.editDefectCall = function () {
                ctrl.editDefectPayl = {
                    "user": ctrl.usernamepro,
                    "objectId": ctrl.dataEdit.id,
                    "projectName": name,
                    "metricsProjectId": id,
                    "projectId": id,
                    "valueAsOn": "test",
                    "lowPriorityDefectsCount": ctrl.dataEdit.defectsByProirity.Low,
                    "mediumPriorityDefectsCount": ctrl.dataEdit.defectsByProirity.Medium,
                    "highPriorityDefectsCount": ctrl.dataEdit.defectsByProirity.High,
                    "criticalPriorityDefectsCount": ctrl.dataEdit.defectsByProirity.Critical,
                    "qaDefects": ctrl.dataEdit.defectsByEnvironment.QA,
                    "uatDefects": ctrl.dataEdit.defectsByEnvironment.UAT,
                    "prodDefects": ctrl.dataEdit.defectsByEnvironment.PROD,
                    "openDefectsWithHighPriorityAndAgeLessThanOrEQ15Days": Number(ctrl.dataEdit.openDefectsByAge.Range1[0].High),
                    "openDefectsWithLowPriorityAndAgeLessThanOrEQ15Days": Number(ctrl.dataEdit.openDefectsByAge.Range1[0].Low),
                    "openDefectsWithMediumPriorityAndAgeLessThanOrEQ15Days": Number(ctrl.dataEdit.openDefectsByAge.Range1[0].Medium),
                    "openDefectsWithCriticalPriorityAndAgeLessThanOrEQ15Days": Number(ctrl.dataEdit.openDefectsByAge.Range1[0].Critical),
                    "openDefectsWithHighPriorityAndAgeBetween15To30Days": Number(ctrl.dataEdit.openDefectsByAge.Range2[0].High),
                    "openDefectsWithLowPriorityAndAgeBetween15To30Days": Number(ctrl.dataEdit.openDefectsByAge.Range2[0].Low),
                    "openDefectsWithMediumPriorityAndAgeBetween15To30Days": Number(ctrl.dataEdit.openDefectsByAge.Range2[0].Medium),
                    "openDefectsWithCriticalPriorityAndAgeBetween15To30Days": Number(ctrl.dataEdit.openDefectsByAge.Range2[0].Critical),
                    "openDefectsWithHighPriorityAndAgeBetween30To60Days": Number(ctrl.dataEdit.openDefectsByAge.Range3[0].High),
                    "openDefectsWithLowPriorityAndAgeBetween30To60Days": Number(ctrl.dataEdit.openDefectsByAge.Range3[0].Low),
                    "openDefectsWithMediumPriorityAndAgeBetween30To60Days": Number(ctrl.dataEdit.openDefectsByAge.Range3[0].Medium),
                    "openDefectsWithCriticalPriorityAndAgeBetween30To60Days": Number(ctrl.dataEdit.openDefectsByAge.Range3[0].Critical),
                    "openDefectsWithHighPriorityAndAgeBetween60To90Days": Number(ctrl.dataEdit.openDefectsByAge.Range4[0].High),
                    "openDefectsWithLowPriorityAndAgeBetween60To90Days": Number(ctrl.dataEdit.openDefectsByAge.Range4[0].Low),
                    "openDefectsWithMediumPriorityAndAgeBetween60To90Days": Number(ctrl.dataEdit.openDefectsByAge.Range4[0].Medium),
                    "openDefectsWithCriticalPriorityAndAgeBetween60To90Days": Number(ctrl.dataEdit.openDefectsByAge.Range4[0].Critical),
                    "openDefectsWithHighPriorityAndAgeGreaterThan90": Number(ctrl.dataEdit.openDefectsByAge.Range5[0].High),
                    "openDefectsWithLowPriorityAndAgeGreaterThan90": Number(ctrl.dataEdit.openDefectsByAge.Range5[0].Low),
                    "openDefectsWithMediumPriorityAndAgeGreaterThan90": Number(ctrl.dataEdit.openDefectsByAge.Range5[0].Medium),
                    "openDefectsWithCriticalPriorityAndAgeGreaterThan90": Number(ctrl.dataEdit.openDefectsByAge.Range5[0].Critical),
                    "fixedDefectsWithHighPriorityAndResolutionLessThanOrEQ15Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range1[0].High),
                    "fixedDefectsWithLowPriorityAndResolutionLessThanOrEQ15Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range1[0].Low),
                    "fixedDefectsWithMediumPriorityAndResolutionLessThanOrEQ15Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range1[0].Medium),
                    "fixedDefectsWithCriticalPriorityAndResolutionLessThanOrEQ15Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range1[0].Critical),
                    "fixedDefectsWithHighPriorityAndResolutionBetween15To30Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range2[0].High),
                    "fixedDefectsWithLowPriorityAndResolutionBetween15To30Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range2[0].Low),
                    "fixedDefectsWithMediumPriorityAndResolutionBetween15To30Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range2[0].Medium),
                    "fixedDefectsWithCriticalPriorityAndResolutionBetween15To30Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range2[0].Critical),
                    "fixedDefectsWithHighPriorityAndResolutionBetween30To60Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range3[0].High),
                    "fixedDefectsWithLowPriorityAndResolutionBetween30To60Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range3[0].Low),
                    "fixedDefectsWithMediumPriorityAndResolutionBetween30To60Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range3[0].Medium),
                    "fixedDefectsWithCriticalPriorityAndResolutionBetween30To60Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range3[0].Critical),
                    "fixedDefectsWithHighPriorityAndResolutionBetween60To90Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range4[0].High),
                    "fixedDefectsWithLowPriorityAndResolutionBetween60To90Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range4[0].Low),
                    "fixedDefectsWithMediumPriorityAndResolutionBetween60To90Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range4[0].Medium),
                    "fixedDefectsWithCriticalPriorityAndResolutionBetween60To90Days": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range4[0].Critical),
                    "fixedDefectsWithHighPriorityAndResolutionGreaterThan90": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range5[0].High),
                    "fixedDefectsWithLowPriorityAndResolutionGreaterThan90": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range5[0].Low),
                    "fixedDefectsWithMediumPriorityAndResolutionGreaterThan90": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range5[0].Medium),
                    "fixedDefectsWithCriticalPriorityAndResolutionGreaterThan90": Number(ctrl.dataEdit.fixeddefectsByResolutions.Range5[0].Critical)
                }

                projectData.updateDefect(ctrl.editDefectPayl).then(function (response) {
                    $rootScope.ttt = true;
                    $uibModalInstance.dismiss("cancel");
                    $uibModal.open({
                        template: '<confirm-popup msg="updateDefect" icon="btn btn-info project-map-add-btn inner-btn-prop" action="$close()"></confirm-popup>',
                        controller: 'projectMapController',
                        controllerAs: 'pm'
                    });
                })
            }
        }

        ctrl.addRelease = function (proje) {
            $uibModal.open({
                templateUrl: 'app/dashboard/views/addRelease.html',
                windowClass: 'app-modal-window-defect',
                controller: addReleaseController,
                controllerAs: 'arc',
                resolve: {
                    name: function () {
                        return proje.projectName;
                    },
                    id: function () {
                        return proje.projectId;
                    }
                }
            });
        }

        function addReleaseController($uibModalInstance, $http, $route, $timeout, $scope, $cookies, name, id) {
            var ctrl = this;
            ctrl.title = "List of Releases";
            ctrl.namerelease = name;
            ctrl.id = id;
            ctrl.usernamepro = $cookies.get('username');
            $scope.options = [{
                value: false,
                label: 'Closed'
            }, {
                value: true,
                label: 'Open'
            },];

            ctrl.prevousText = true;
            ctrl.normalformText = false;
            ctrl.prevHeader = false;
            ctrl.hideForm = function () {
                ctrl.prevousText = true;
                ctrl.normalformText = false;
                ctrl.prevHeader = false;
                ctrl.title = "List of Releases";
            }
            ctrl.showForm = function () {
                ctrl.prevousText = false;
                ctrl.normalformText = true;
                ctrl.prevHeader = true;
                ctrl.title = "Add Release";
                ctrl.releasePayload = {};
                ctrl.releaseId = "";
                ctrl.name = "";
                ctrl.released = "";
                ctrl.criticalDefectsFound = "";
                ctrl.highDefectsFound = "";
                ctrl.mediumDefectsFound = "";
                ctrl.lowDefectsFound = "";
                ctrl.criticalDefectsClosed = "";
                ctrl.highDefectsClosed = "";
                ctrl.mediumDefectsClosed = "";
                ctrl.lowDefectsClosed = "";
                ctrl.noofStoryCommitted = "";
                ctrl.noofStoryCompleted = "";
            }

            ctrl.updateRelease = function (releaseidUpdation) {
                ctrl.nname = releaseidUpdation.releaseId;
                ctrl.id = id;
                ctrl.projectId = releaseidUpdation.projectId;
                ctrl.projectName = releaseidUpdation.projectName;
                ctrl.action_name = "Update";
                ctrl.addScreen = false;
                ctrl.updateScreen = true;
                ctrl.aaaa = releaseidUpdation;
                $uibModalInstance.dismiss("cancel");
                $uibModal.open({
                    templateUrl: 'app/dashboard/views/updateReleasehtml.html',
                    windowClass: 'app-modal-window-defect',
                    controller: updateReleaseController,
                    controllerAs: 'urc',
                    resolve: {
                        sprinpayl: function () {
                            return ctrl.aaaa;
                        },
                        names: function () {
                            return ctrl.id;
                        },
                        ids: function () {
                            return ctrl.nname;
                        },
                        projectidsprint: function () {
                            return ctrl.projectId;
                        }
                    }
                });
            }

            featureData.ReleaseData(ctrl.id, ctrl.namerelease).then(ReleaseDataProcessing);

            function ReleaseDataProcessing(data) {
                ctrl.releasegraph = data;
            }

            ctrl.cancelPopUp =function() {
                ctrl.hideForm();
               featureData.ReleaseData(ctrl.id, ctrl.namerelease).then(ReleaseDataProcessing);
                function ReleaseDataProcessing(data) {
                    ctrl.releasegraph = data;
            } 

            }
            ctrl.postRelease = function (proje) {
               var endDateMilli = Date.parse(ctrl.releaseDate);
               var startDateMilli = Date.parse(ctrl.startDate);
                ctrl.releasePayload = {
                    "user": ctrl.usernamepro,
                    "projectName": name,
                    "projectId": id,
                    "releaseId": ctrl.releaseId,
                    "name": ctrl.name,
                    "startDate": startDateMilli,
                    "releaseDate": endDateMilli,
                    "released": ctrl.released,
                    "description": ctrl.description,

                    "criticalDefectsFound": ctrl.criticalDefectsFound,
                    "highDefectsFound": ctrl.highDefectsFound,
                    "mediumDefectsFound": ctrl.mediumDefectsFound,
                    "lowDefectsFound": ctrl.lowDefectsFound,

                    "criticalDefectsClosed": ctrl.criticalDefectsClosed,
                    "highDefectsClosed": ctrl.highDefectsClosed,
                    "mediumDefectsClosed": ctrl.mediumDefectsClosed,
                    "lowDefectsClosed": ctrl.lowDefectsClosed,

                   /* "criticalDefectsUnresolved": ctrl.criticalDefectsUnresolved,
                    "highDefectsUnresolved": ctrl.highDefectsUnresolved,
                    "mediumDefectsUnresolved": ctrl.mediumDefectsUnresolved,
                    "lowDefectsUnresolved": ctrl.lowDefectsUnresolved,*/
                    "noofStoryCompleted": ctrl.noofStoryCommitted,
                    "noofStoryCommitted": ctrl.noofStoryCompleted
                }

                if (ctrl.createRelease.$valid == true) {
                projectData.postRelease(ctrl.releasePayload).then(function (response) {
                    ctrl.hideForm();
                    featureData.ReleaseData(ctrl.id, ctrl.namerelease).then(ReleaseDataProcessing);
                        function ReleaseDataProcessing(data) {
                            ctrl.releasegraph = data;
                    }
                },
                    function (response) {
                        if (response.status == 409) {
                            $uibModal.open({
                                templateUrl: 'app/dashboard/views/ConfirmationModals/errorDuplicateRelease.html',
                                controller: 'projectMapController',
                                controllerAs: 'pm'
                            });
                        }
                    })
            }
            }
        }

        ctrl.addSprint = function (proje) {
            $uibModal.open({
                templateUrl: 'app/dashboard/views/addSprint.html',
                windowClass: 'app-modal-window-defect',
                controller: addSprintController,
                controllerAs: 'asc',
                resolve: {
                    name: function () {
                        return proje.projectName;
                    },
                    id: function () {
                        return proje.projectId;
                    }
                }
            });
        }

        function addSprintController($uibModalInstance, $http, $route, $timeout, $scope, $cookies, name, id) {
            var ctrl = this;
            ctrl.title = "List of Sprints";
            ctrl.name = name;
            ctrl.id = id;
            ctrl.projectpath = $cookies.get('projectNameJira');
            ctrl.projectpathId = $cookies.get('projectIdJira');
            ctrl.projectiddefects = $cookies.get('ProSpId');
            ctrl.usernamepro = $cookies.get('username');
            $scope.options = [{
                value: true,
                label: 'Closed'
            }, {
                value: false,
                label: 'Open'
            },];

            ctrl.prevousText = true;
            ctrl.normalformText = false;
            ctrl.prevHeader = false;
            ctrl.update = false;
            ctrl.hideForm = function () {
                ctrl.prevousText = true;
                ctrl.normalformText = false;
                ctrl.prevHeader = false;
                ctrl.title = "List of Sprints";
            }
            ctrl.showForm = function () {
                ctrl.prevousText = false;
                ctrl.normalformText = true;
                ctrl.prevHeader = true;
                ctrl.sprintPayload = {};
                $rootScope.action_name = "Add";
                ctrl.addScreen = true;
                ctrl.updateScreen = false;
                ctrl.title = "Add Sprint";
            }



            featureData.sprintDta(ctrl.id, ctrl.name).then(sprintdataProcess);

            function sprintdataProcess(data) {
                ctrl.spAllDetails = data;
            }

            ctrl.closePopUp = function() {
                ctrl.hideForm();
                featureData.sprintDta(ctrl.id, ctrl.name).then(sprintdataProcess);
                    function sprintdataProcess(data) {
                        ctrl.spAllDetails = data;
                }
            }

            ctrl.updateSprint = function (sprintidUpdation) {
                ctrl.nname = sprintidUpdation.sid;
                ctrl.id = id;
                ctrl.projectId = sprintidUpdation.projectId;
                ctrl.action_name = "Update";
                ctrl.addScreen = false;
                ctrl.updateScreen = true;
                ctrl.aaaa = sprintidUpdation;
                $uibModalInstance.dismiss("cancel");
                $uibModal.open({
                    templateUrl: 'app/dashboard/views/updateSprintmodal.html',
                    windowClass: 'app-modal-window-defect',
                    controller: updateSprintController,
                    controllerAs: 'usc',
                    resolve: {
                        sprinpayl: function () {
                            return ctrl.aaaa;
                        },
                        names: function () {
                            return ctrl.id;
                        },
                        ids: function () {
                            return ctrl.nname;
                        },
                        projectidsprint: function () {
                            return ctrl.projectId;
                        }
                    }
                });
            }

            ctrl.postSprint = function (proje) {
                
                
                if (ctrl.addSprint.$valid == true) {
                ctrl.sprintPayload.projectName = name;
                ctrl.sprintPayload.projectId = id;
                ctrl.sprintPayload.user = ctrl.usernamepro;
                projectData.postSprint(ctrl.sprintPayload).then(function (response) {
                    ctrl.hideForm();
                    featureData.sprintDta(ctrl.id, ctrl.name).then(sprintdataProcess);
                    function sprintdataProcess(data) {
                        ctrl.spAllDetails = data;
                    }           
                },
                    function (response) {
                        if (response.status == 409) {
                            $uibModal.open({
                                templateUrl: 'app/dashboard/views/ConfirmationModals/errormodelDuplicate.html',
                                controller: 'projectMapController',
                                controllerAs: 'pm'
                            });
                        }
                    })
                }
            }
        }

        function updateReleaseController($uibModalInstance, $http, $route, $timeout, $scope, $cookies, sprinpayl, names, ids, projectidsprint) {
            var ctrl = this;
            ctrl.ids = ids;
            ctrl.names = names;
            ctrl.usernamepro = $cookies.get('username');
            $scope.options = [{
                value: false,
                label: 'Closed'
            }, {
                value: true,
                label: 'Open'
            },];

            featureData.updateReleaseDta(ctrl.ids, ctrl.names).then(function (data) {
                ctrl.fetchReleasedetails = data;
                ctrl.releaseStartDate = ctrl.fetchReleasedetails.versionData.startDate;
                ctrl.releaseEndDate = ctrl.fetchReleasedetails.versionData.releaseDate;

                var releaseStartDateFormat = new Date(ctrl.fetchReleasedetails.versionData.startDate),
                startDateFormat = releaseStartDateFormat.getDate(),
                startMonthFormat  = releaseStartDateFormat.getMonth(),
                startYearFormat = releaseStartDateFormat.getFullYear();
                ctrl.releaseStartDate = startYearFormat  + '-' + ('0' + (startMonthFormat+1)).slice(-2) + '-' + ('0' + (startDateFormat)).slice(-2); 
                var releaseEndDateFormat = new Date(ctrl.fetchReleasedetails.versionData.releaseDate),
                endDateFormat = releaseEndDateFormat.getDate(),
                endMonthFormat  = releaseEndDateFormat.getMonth(),
                endYearFormat = releaseEndDateFormat.getFullYear();
                ctrl.releaseEndDate = endYearFormat  + '-' + ('0' + (endMonthFormat+1)).slice(-2) + '-' + ('0' + (endDateFormat)).slice(-2); 
                }) 

            ctrl.postRelease = function (proje) {
                var startDateMilliRel = Date.parse(ctrl.releaseStartDate);
                var endDateMilliRel = Date.parse(ctrl.releaseEndDate);

                ctrl.releasePayload = {
                    "user": ctrl.usernamepro,
                    "objectId":ctrl.fetchReleasedetails.id,
                    "projectName": ctrl.names,
                    "projectId": ctrl.names,
                    "releaseId": ctrl.fetchReleasedetails.releaseId,
                    "name": ctrl.fetchReleasedetails.name,
                    "startDate": startDateMilliRel,
                    "releaseDate": endDateMilliRel,
                    "released": ctrl.fetchReleasedetails.released,
                    "description": ctrl.description,
                    "criticalDefectsFound": ctrl.fetchReleasedetails.versionData.defectsFound.severity[3].value,
                    "highDefectsFound": ctrl.fetchReleasedetails.versionData.defectsFound.severity[0].value,
                    "mediumDefectsFound": ctrl.fetchReleasedetails.versionData.defectsFound.severity[2].value,
                    "lowDefectsFound": ctrl.fetchReleasedetails.versionData.defectsFound.severity[1].value,
                    "criticalDefectsClosed": ctrl.fetchReleasedetails.versionData.defectsResolved.severity[3].value,
                    "highDefectsClosed": ctrl.fetchReleasedetails.versionData.defectsResolved.severity[0].value,
                    "mediumDefectsClosed": ctrl.fetchReleasedetails.versionData.defectsResolved.severity[2].value,
                    "lowDefectsClosed": ctrl.fetchReleasedetails.versionData.defectsResolved.severity[1].value,
                    "criticalDefectsUnresolved": ctrl.fetchReleasedetails.versionData.defectsUnresolved.severity[3].value,
                    "highDefectsUnresolved": ctrl.fetchReleasedetails.versionData.defectsUnresolved.severity[0].value,
                    "mediumDefectsUnresolved": ctrl.fetchReleasedetails.versionData.defectsUnresolved.severity[2].value,
                    "lowDefectsUnresolved": ctrl.fetchReleasedetails.versionData.defectsUnresolved.severity[1].value,
                    "noofStoryCompleted": ctrl.fetchReleasedetails.versionData.noofStoryCompleted,
                    "noofStoryCommitted": ctrl.fetchReleasedetails.versionData.noofStoryPoints
                }

                if (ctrl.updateRelease.$valid == true) {

                projectData.updateRelease(ctrl.releasePayload).then(function (response) {
                    $uibModalInstance.dismiss("cancel");
                    $uibModal.open({
                        template: '<confirm-popup msg="updateRelease" icon="btn btn-info project-map-add-btn inner-btn-prop" action="$close()"></confirm-popup>',
                        controller: 'projectMapController',
                        controllerAs: 'pm'
                    });
                })
            }
            }
        }

        function updateSprintController($uibModalInstance, $http, $route, $timeout, $scope, $cookies, sprinpayl, names, ids, projectidsprint) {
            var ctrl = this;
            ctrl.names = names;
            ctrl.ids = ids;
            ctrl.ppid = projectidsprint;
            ctrl.usernamepro = $cookies.get('username');
            $scope.options = [{
                value: true,
                label: 'Closed'
            }, {
                value: false,
                label: 'Open'
            },];
            ctrl.fetchdetails = {};

            featureData.updateSprintDta(ctrl.names, ctrl.ids).then(function (data) {
            ctrl.fetchdetails = data;
            ctrl.startDate = ctrl.fetchdetails.sprintData.startDate;
            ctrl.endDate = ctrl.fetchdetails.sprintData.endDate;

            var sprintStartDateFormat = new Date(ctrl.fetchdetails.sprintData.startDate),
            sprintStartDateFormatDate = sprintStartDateFormat.getDate(),
            sprintStartDateFormatMonth = sprintStartDateFormat.getMonth(),
            sprintStartDateFormatYear = sprintStartDateFormat.getFullYear();
            ctrl.startDate = sprintStartDateFormatYear + '-' + ('0' + (sprintStartDateFormatMonth+1)).slice(-2) + '-' + ('0' + (sprintStartDateFormatDate)).slice(-2); 
            var sprintEndDateFormat = new Date(ctrl.fetchdetails.sprintData.endDate),
            sprintEndDateFormatDate = sprintEndDateFormat.getDate(),
            sprintEndDateFormatMonth = sprintEndDateFormat.getMonth(),
            sprintEndDateFormatYear = sprintEndDateFormat.getFullYear();
            ctrl.endDate = sprintEndDateFormatYear + '-' + ('0' + (sprintEndDateFormatMonth+1)).slice(-2) + '-' + ('0' + (sprintEndDateFormatDate)).slice(-2);
            }) 

            
            
            ctrl.updateSprintCall = function () {
                var startDateMilliSprint = Date.parse(ctrl.startDate);
                var endDateMilliSprint = Date.parse(ctrl.endDate);

                ctrl.sprintEditPayload = {
                    "user": ctrl.usernamepro,
                    "objectId": ctrl.fetchdetails.id,
                    "projectId": projectidsprint,
                    "projectName": ctrl.names,
                    "sprintId": ctrl.fetchdetails.sid,
                    "sprintName": ctrl.fetchdetails.sprintData.sprintName,

                    "committedStoryPoints": ctrl.fetchdetails.sprintData.committedStoryPoints,
                    "completedStoryPoints": ctrl.fetchdetails.sprintData.completedStoryPoints,
                    "committedStoriesCount": ctrl.fetchdetails.sprintData.committedIssueCount,
                    "completedIssueCount": ctrl.fetchdetails.sprintData.completedIssueCount,
                    "committedIssueCount": ctrl.fetchdetails.sprintData.committedIssueCount,

                    "released": ctrl.fetchdetails.closed,
                    "criticalDefectsFound": ctrl.fetchdetails.sprintData.defectsFound.severity[3].value,
                    "mediumDefectsFound": ctrl.fetchdetails.sprintData.defectsFound.severity[1].value,
                    "lowDefectsFound": ctrl.fetchdetails.sprintData.defectsFound.severity[0].value,
                    "highDefectsFound": ctrl.fetchdetails.sprintData.defectsFound.severity[2].value,

                    "criticalDefectsClosed": ctrl.fetchdetails.sprintData.defectsResolved.severity[3].value,
                    "mediumDefectsClosed": ctrl.fetchdetails.sprintData.defectsResolved.severity[1].value,
                    "lowDefectsClosed": ctrl.fetchdetails.sprintData.defectsResolved.severity[0].value,
                    "highDefectsClosed": ctrl.fetchdetails.sprintData.defectsResolved.severity[2].value,

                    "criticalDefectsUnresolved": ctrl.fetchdetails.sprintData.defectsUnresolved.severity[3].value,
                    "mediumDefectsUnresolved": ctrl.fetchdetails.sprintData.defectsUnresolved.severity[1].value,
                    "lowDefectsUnresolved": ctrl.fetchdetails.sprintData.defectsUnresolved.severity[0].value,
                    "highDefectsUnresolved": ctrl.fetchdetails.sprintData.defectsUnresolved.severity[2].value,

                    "endDate": endDateMilliSprint,
                    "startDate": startDateMilliSprint,
                    "storiesAdded": ctrl.fetchdetails.sprintData.burndown.issuesAdded.count,
                    "storiesRemoed": ctrl.fetchdetails.sprintData.burndown.issuesRemoved.count,
                    "storiesAddedPoints": ctrl.fetchdetails.sprintData.burndown.issuesAdded.storyPoints,
                    "storiesRemovedPoints": ctrl.fetchdetails.sprintData.burndown.issuesRemoved.storyPoints,

                    "sprintCriticalDefectsResolved": ctrl.fetchdetails.sprintData.sprintDefectsResolved.severity[3].value,
                    "sprintMediumDefectsResolved": ctrl.fetchdetails.sprintData.sprintDefectsResolved.severity[1].value,
                    "sprintLowDefectsResolved": ctrl.fetchdetails.sprintData.sprintDefectsResolved.severity[0].value,
                    "sprintHighDefectsResolved": ctrl.fetchdetails.sprintData.sprintDefectsResolved.severity[2].value
                }

                if (ctrl.updateSprint.$valid == true) {
                projectData.updateSprint(ctrl.sprintEditPayload).then(function (response) {
                    $uibModalInstance.dismiss("cancel");
                    $uibModal.open({
                        template: '<confirm-popup msg="updateSprint"icon="btn btn-info project-map-add-btn inner-btn-prop"  action="$close()"></confirm-popup>',
                        controller: 'projectMapController',
                        controllerAs: 'pm'
                    });
                })
            }
            }
        }

        ctrl.heatMap = function (probj) {
            $uibModal.open({
                templateUrl: 'app/dashboard/views/heatmap.html',
                windowClass: 'app-modal-window-defect',
                controller: heatMapController,
                controllerAs: 'hmc',
                resolve: {
                    name: function () {
                        return probj.projectName;
                    },
                    id: function () {
                        return probj.projectId;
                    }
                }
            });
        }

        function heatMapController($uibModalInstance, $http, $route, $timeout, $scope, $cookies, name, id) {
            var ctrl = this;
            ctrl.title = "List of HeatMap";
            ctrl.prevousText = true;
            ctrl.normalformText = false;
            ctrl.prevHeader = false;
            ctrl.update = false;
            ctrl.usernamepro = $cookies.get('username');
            //ctrl.title="Add Heat Map";
            var nextMonth, prevMonth, prevYear, nextYear;

            var monthNames = ["January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
            ];  
            var currentdate = new Date();
            var currentMonth = Number(currentdate.getMonth());
            var currentYear = Number(currentdate.getFullYear());
            var currentMonthLabel = monthNames[currentMonth];
                           if(currentMonth == 0){
                        nextYear =  currentYear;
                prevYear =  currentYear-1;     
                prevMonth = monthNames[11];
                 nextMonth = currentMonth<11 ? monthNames[(currentMonth+1)] : monthNames[0];
            } else if(currentMonth ==11){
                prevYear =  currentYear;
                nextYear =  currentYear+1;
                nextMonth = monthNames[0];
                prevMonth = currentMonth>0 ? monthNames[(currentMonth-1)] : monthNames[11];
            } else {
                prevYear = currentYear;
                nextYear = currentYear;
                prevMonth = currentMonth>0 ? monthNames[(currentMonth-1)] : monthNames[11];
                nextMonth = currentMonth<11 ? monthNames[(currentMonth+1)] : monthNames[0];

            }


            ctrl.monthOptions = [];

            ctrl.monthOptions.push(prevMonth+","+prevYear);
            ctrl.monthOptions.push(currentMonthLabel+","+currentYear);
            ctrl.monthOptions.push(nextMonth+","+nextYear);

            $scope.color = [
                { value: 'Green', name: 'Green' },
                { value: 'Amber', name: 'Amber' },
                { value: 'Red', name: 'Red' },
                { value: 'NA', name: 'NA' }
            ];


            ctrl.hideForm = function () {
                ctrl.prevousText = true;
                ctrl.normalformText = false;
                ctrl.prevHeader = false;
                ctrl.title = "List of HeatMap";
            }
            ctrl.showForm = function () {
                ctrl.prevousText = false;
                ctrl.normalformText = true;
                ctrl.prevHeader = true;
                ctrl.heatMapPayload = {};
                $rootScope.action_name = "Add";
                ctrl.addScreen = true;
                ctrl.updateScreen = false;
                ctrl.title = "Add Heat Map";
            }

            featureData.heatMapData(id).then(heatMapDataProcess);

            function heatMapDataProcess(data) {
                ctrl.heatMapDetails = data;
            }

            ctrl.closePopup = function() {
                ctrl.hideForm();
                featureData.heatMapData(id).then(heatMapDataProcess);
                    function heatMapDataProcess(data) {
                    ctrl.heatMapDetails = data;
                }
            }
            ctrl.updateHeatMap = function (releaseidUpdation) {
                 $uibModalInstance.dismiss("cancel");

                $uibModal.open({
                    templateUrl: 'app/dashboard/views/updateHeatMap.html',
                    windowClass: 'app-modal-window-defect',
                    controller: updateHeatMapController,
                    controllerAs: 'uhc',
                    resolve: {
                        data: function () {
                            return releaseidUpdation;
                        }
                    }
                });
            }
            
            function getDate(date){
                 var year = date.split(",")[1]
                 var month = date.split(",")[0]
                 var monthNames = ["January", "February", "March", "April", "May", "June",
                                    "July", "August", "September", "October", "November", "December"
                                  ];
                  for(var i = 0;i<monthNames.length;i++){
                    if(month == monthNames[i]){
                        month = i+1;
                    }
                  }
                  if(month < 10){
                    month = "0"+month;
                  }

                    return month+"-"+"01-"+year;
                }

            ctrl.postHeatMap = function () {
                

                if (ctrl.addHeatmapPage.$valid == true) {

                ctrl.heatMapPayload.submissionDate = getDate(ctrl.heatMapPayload.submissionDate);
                //ctrl.heatMapPayload.submissionDate = Date.parse(ctrl.subdta);
                ctrl.heatMapPayload.projectId = id;
                ctrl.heatMapPayload.user = ctrl.usernamepro;
                projectData.postHeatMap(ctrl.heatMapPayload).then(function (response) {
                    ctrl.hideForm();
                    featureData.heatMapData(id).then(heatMapDataProcess);
                    function heatMapDataProcess(data) {
                        ctrl.heatMapDetails = data;
            }
                    /*$uibModalInstance.dismiss("cancel");
                    $uibModal.open({
                        template: '<confirm-popup msg="addHeatMap" icon="btn btn-info project-map-add-btn inner-btn-prop" action="$close()"></confirm-popup>',
                        controller: 'projectMapController',
                        controllerAs: 'pm'
                    });*/
                })
            }
            }

        }

        function updateHeatMapController($uibModalInstance, $http, $route, $timeout, $scope, $cookies, data) {
            var ctrl = this;
            ctrl.title="Update Heat Map"
            $scope.color = [
                { value: 'Green', name: 'Green' },
                { value: 'Amber', name: 'Amber' },
                { value: 'Red', name: 'Red' },
                { value: 'NA', name: 'NA' }
            ];
            ctrl.usernamepro = $cookies.get('username');

            /*var monthName = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
            var d1 = new Date(data.submissionDate),
            m = d1.getMonth(),
            y = d1.getFullYear();

            var dateString = monthName[m] + "," + y; */

            ctrl.updateHeatmapPayload = {
                "user": ctrl.usernamepro,
                "objectId":data.id,
                "projectId": data.projectId,
                "submissionDate":data.submissionDate, 
                "customerWSRStatus": data.projectHeatmapData.customerWSR.customerWSRStatus,
                "architectureFocusStatus": data.projectHeatmapData.architectureFocus.architectureFocusStatus,
                "automatedUnitTestingStatus": data.projectHeatmapData.automatedUnitTesting.automatedUnitTestingStatus,
                "codeCoveragePercentage": data.projectHeatmapData.codeCoverage.codeCoveragePercentage,
                "continuousIntegrationIndex": data.projectHeatmapData.continuousIntegration.continuousIntegrationIndex,

                "domainKnowlwdgeStatus": data.projectHeatmapData.domainKnowledge.domainKnowledgeStatus,
                "manualCodeReviewStatus": data.projectHeatmapData.manualCodeReview.manualCodeReviewStatus,
                //"manualCodeReviewCount" : data.projectHeatmapData,
                "metricsStatus": data.projectHeatmapData.metrics.metricsStatus,
                //"performanceAssessmentStatus" : data.projectHeatmapData,
                "performanceAssessmentPercentage": data.projectHeatmapData.performanceAssessment.performanceAssessmentPercentage,

                "requirementsStatus": data.projectHeatmapData.requirements.requirementsStatus,
                "releaseProcessStatus": data.projectHeatmapData.releaseProcess.releaseProcessStatus,
                //"securityAssessmentStatus" : data.projectHeatmapData,
                "securityAssessmentIndex": data.projectHeatmapData.securityAssessment.securityAssessmentIndex,
                //"staticCodeAnalysisStatus" : data.projectHeatmapData,
                "staticCodeAnalysisIndex" : data.projectHeatmapData.staticCodeAnalysis.staticCodeAnalysisIndex,
                "testingProcessStatus": data.projectHeatmapData.testingProcess.testingProcessStatus,
                //"testAutomationStatus" : data.projectHeatmapData,
                "testAutomationPercentage" : data.projectHeatmapData.testAutomation.testAutomationPercentage,
                "designFocusStatus": data.projectHeatmapData.designFocus.designFocusStatus,
                "productKnowledgeIndex": data.projectHeatmapData.productKnowledge.productKnowledgeIndex,
                "development":data.projectHeatmapData.teamSize.development,
                "testing":data.projectHeatmapData.teamSize.testing



            }

            ctrl.updateHeatMap = function () {
                if (ctrl.updateHeatMapPage.$valid == true) {
                projectData.updateHeatMap(ctrl.updateHeatmapPayload).then(function (response) {
                    $uibModalInstance.dismiss("cancel");
                    $uibModal.open({
                        template: '<confirm-popup msg="updateHeatMap" icon="btn btn-info project-map-add-btn inner-btn-prop" action="$close()"></confirm-popup>',
                        controller: 'projectMapController',
                        controllerAs: 'pm'
                    });
                })
            }
            }

        }

    }
})();