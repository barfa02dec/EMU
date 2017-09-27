(function() {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('userManagementController', userManagementController);

    userManagementController.$inject = ['$scope', 'codeAnalysisData', 'testSuiteData', '$q', '$filter', '$uibModal', '$location', '$routeParams', '$http', '$route', '$cookies', '$timeout', '$cookieStore'];

    function userManagementController($scope, codeAnalysisData, testSuiteData, $q, $filter, $uibModal, $location, $routeParams, $http, $route, $cookies, $timeout, $cookieStore) {
        var ctrl = this;
        ctrl.usernamepro = $cookies.get('username');

        //Fetch All Application Users
        $http.get("/api/getApplicationUsers")
            .then(function(response) {
                ctrl.getAppUsers = response.data;
            });

        //Tabs Settings
        $scope.tabs = [{
                title: 'User Management',
                url: 'one.tpl.html'
            }, {
                title: 'Role Management',
                url: 'two.tpl.html'
            }
            /*, {
                        title: 'Permissions',
                        url: 'three.tpl.html'
                    }*/
        ];

        $scope.currentTab = 'one.tpl.html';

        $scope.onClickTab = function(tab) {
            $scope.currentTab = tab.url;
        }

        $scope.isActiveTab = function(tabUrl) {
            return tabUrl == $scope.currentTab;
        }

        //Adding Role to User Dual List Functionality
        ctrl.transfer = function(from, to, index) {
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



        ctrl.demoOptions = {
            //title: 'Demo: Recent World Cup Winners',
            filterPlaceHolder: 'Start typing to filter the lists below.',
            labelAll: 'All Items',
            labelSelected: 'Selected Items',
            helpMessage: ' Click items to transfer them between fields.',
            orderProperty: 'name',
            items: [{
                'id': '50',
                'name': 'Admin'
            }, {
                'id': '45',
                'name': 'Developer'
            }, {
                'id': '66',
                'name': 'Tester'
            }, {
                'id': '30',
                'name': 'Manager'
            }, {
                'id': '41',
                'name': 'Director'
            }, {
                'id': '34',
                'name': 'Lead'
            }],
            selectedItems: []

        };

        //Fetch All Active Permissions
        $http.get("/api/allActiveEngineeringDashboardPermissions")
            .then(function(response) {
                ctrl.getAllPermissions = response.data;
            });

        function processPerResponse(response) {

        }

        //Open Create Permission Modal
        ctrl.openCreatePermission = function() {
            $uibModal.open({
                templateUrl: 'app/dashboard/views/createPermission.html',
                controller: postPermissionController,
                controllerAs: 'ppc'
            });

        }

        //Create Permission controller
        function postPermissionController($uibModalInstance, $http, $route, $timeout, $scope, $cookies) {
            var ctrl = this;
            ctrl.permission = {
                "createdBy": ctrl.createdBy,
                "createdOn": ctrl.createdOn,
                "description": ctrl.description,
                "lastUpdatedOn": ctrl.lastUpdatedOn,
                "name": ctrl.name,
                "status": true,
                "updatedBy": ctrl.updatedBy
            }

            //POST call to create Permissions
            ctrl.postPermission = function() {
                $http.post('/api/engineeringDashboardPermission ', (ctrl.permission)).then(function(response) {
                    $route.reload();
                    $uibModalInstance.dismiss("cancel");
                    /* $uibModal.open({
                         templateUrl: 'app/dashboard/views/postconfirm.html',
                         controller: 'projectMapController',
                         controllerAs: 'pm'
                     });*/
                })
            };

        }

        //Open Delete Permission Model
        ctrl.deletePermissionModel = function(per) {
            //ctrl.proData = pro;
            $uibModal.open({
                templateUrl: 'app/dashboard/views/deletePermission.html',
                controller: delPermissionController,
                controllerAs: 'delpc',
                resolve: {
                    pid: function() {
                        return per.name;
                    }
                }
            });
        }

        //Deactivate Permission
        function delPermissionController($uibModalInstance, pid, $route) {
            var ctrl = this;
            ctrl.deletePermissions = function(per) {

                $http.post('/api/deactivateEngineeringDashboardPermission?name=' + pid).then(function(response) {
                    $uibModalInstance.dismiss("cancel");
                    $route.reload();
                    $uibModal.open({
                        templateUrl: 'app/dashboard/views/deletepermissionConfirmation.html',
                        controller: delPermissionController,
                        controllerAs: 'delpc'
                    });
                })
            };

        }

        //Open Create Role Pop up
        ctrl.openCreateRole = function() {
            $uibModal.open({
                templateUrl: 'app/dashboard/views/createRole.html',
                controller: postRoleController,
                controllerAs: 'prc'
            });

            /* $http.get("/api/allActiveEngineeringDashboardPermissions")
                 .then(function(response) {
                     response.data.selectedItems = [];
                     ctrl.selItem = response.data.selectedItems;
                     ctrl.newArr = [];
                     for (var i = 0; i < ctrl.selItem.length; i++) {
                         //if (ctrl.selItem[i][0].hasOwnProperty("name")) {
                             ctrl.newArr.push(ctrl.selItem[i].name);
                         //}

                     }
                     ctrl.getAllPermissions = response.data;
                 });

             ctrl.transfer = function(from, to, index) {
                 if (index >= 0) {
                     to.push(from[index]);
                     from.splice(index, 1);
                 } else {
                     for (var i = 0; i < from.length; i++) {
                         to.push(from[i]);
                     }
                     from.length = 0;
                 }
             };*/
        }

        //Create Roles Functionality
        function postRoleController($uibModalInstance, $http, $route, $timeout, $scope, $cookies) {
            var ctrl = this;
            ctrl.usernamepro = $cookies.get('username');

            //Fetch All Active Permissions to display in dual list
            $http.get("/api/allActiveEngineeringDashboardPermissionKeys")
                .then(function(response) {

                    ctrl.getAllPermissions = response.data;
                    ctrl.selectedItems = [];
                    /*for(var i=0;i<ctrl.getAllPermissions;i++){
                        if (ctrl.getAllPermissions[i].hasOwnProperty("name")) {
                        selectedItems.push(ctrl.getAllPermissions[i].name);
                    }
                    }*/
                    ctrl.role = {
                        "createdBy": ctrl.usernamepro,
                        "description": ctrl.description,
                        "enabled": true,
                        "permissions": ctrl.selectedItems,
                        "roleKey": ctrl.roleKey,
                        "updatedBy": ctrl.usernamepro
                    }
                });
            //Create Role Payload Object

            //Create role post API call
            if (ctrl.selectedItems != 0) {
                ctrl.postRole = function() {
                    ctrl.usernamepro = $cookies.get('username');

                    $http.post('/api/engineeringDashboardUserRole ', (ctrl.role)).then(function(response) {
                        $route.reload();
                        $uibModalInstance.dismiss("cancel");
                        console.log("saas" + ctrl.role);
                    })
                };
            } else {
                $uibModal.open({
                    templateUrl: 'app/dashboard/views/ConfirmationModals/selectpermissionrequired.html',
                    controller: 'userManagementController',
                    controllerAs: 'umc'
                });
            }

            //Adding Permissions to User Dual List Functionality
            ctrl.transfer = function(from, to, index) {
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
            angular.forEach(ctrl.getAllPermissions, function(value, key) {
                if (value.id === vall) {
                    value.editorEnabled = true;
                }
            });

        };

        ctrl.editpermission = function(info) {
            info.editorEnabled = false;
            $http.post('/api/engineeringDashboardPermissionUpdate', (info)).then(function(response) {
                alert("Edited Successfully");

                //info.editorEnabled = true;
            })

        }

        //Fetch all Roles
        $http.get("/api/allActiveEngineeringDashboardUserRoles")
            .then(function(response) {
                ctrl.getRoles = response.data;
                ctrl.getOnlyPerArray = [];
                for (var i = 0; i < response.data.length; i++) {
                    if (response.data[i].permissions != undefined) {
                        //ctrl.getOnlyPerArray.push(response.data[i].permissions)
                        ctrl.permisArray = _.keys(response.data[i].permissions)
                    }
                }
                ctrl.getOnlyPermission = response.data;

            });

        //Open Delete Role Model
        ctrl.deleteRoleModel = function(per) {
            //ctrl.proData = pro;
            $uibModal.open({
                templateUrl: 'app/dashboard/views/deleteRoleModel.html',
                controller: deleteRoleController,
                controllerAs: 'delrole',
                resolve: {
                    pid: function() {
                        return per.roleKey;
                    }
                }
            });
        }

        //Delete Role Functionality
        function deleteRoleController($uibModalInstance, pid, $route) {
            var ctrl = this;
            ctrl.deleteRoles = function(per) {
                $http.post('/api/deactivateEngineeringDashboardUserRoles?key=' + pid).then(function(response) {
                    $uibModalInstance.dismiss("cancel");
                    $route.reload();
                    $uibModal.open({
                        templateUrl: 'app/dashboard/views/ConfirmationModals/roleconfirmationMessage.html',
                        controller: userManagementController,
                        controllerAs: 'umc'
                    });
                })
            };
        }

        //Fetch all Projects and its respective Users.
        $http.get("/api/getProjects")
            .then(function(response) {
                ctrl.getprojectsUsers = response.data;

                //ctrl.getAllPermissions = response.data;
                ctrl.newArr = [];
                for (var i = 0; i < ctrl.getprojectsUsers.length; i++) {
                    if (ctrl.getprojectsUsers != undefined) {
                        ctrl.newArr.push(ctrl.getprojectsUsers[i].usersGroup);
                    }
                }

                ctrl.newArrusergrop = [];
                for (var i = 0; i < ctrl.newArr.length; i++) {
                    if (ctrl.newArr != undefined) {
                        ctrl.newArrusergrop.push(ctrl.newArr[i].user);
                    }
                }
                //console.log(ctrl.newArrusergrop);
            });

        //logout functionality
        ctrl.logout = function() {
            $cookieStore.remove("username");
            $cookieStore.remove("authenticated");
            $location.path('/');
        };

        ctrl.editRoleModel = function(rolename, roleObj) {

            $uibModal.open({
                templateUrl: 'app/dashboard/views/editRoleTemplate.html',
                controller: editRoleController,
                controllerAs: 'erc',
                resolve: {
                    rolename: function() {
                        return rolename;
                    },
                    roleObj: function() {
                        return roleObj;
                    }
                }
            });

        }

        function editRoleController(rolename, roleObj, $uibModalInstance) {
            var ctrl = this;

            $http.get("/api/allActiveEngineeringDashboardPermissionKeys")
                .then(function(response) {
                    ctrl.getAllPermissions = response.data;
                    ctrl.roleObj = roleObj;
                    ctrl.arrayconverted = _.keys(ctrl.roleObj.permissions);
                    ctrl.usernamepro = $cookies.get('username');
                    ctrl.role = {
                        "createdBy": ctrl.usernamepro,
                        "description": ctrl.roleObj.description,
                        "enabled": true,
                        "permissions": ctrl.arrayconverted,
                        "roleKey": ctrl.roleObj.roleKey,
                        "updatedBy": ctrl.usernamepro
                    }
                    ctrl.arraySelectedPermission = [];

                    ctrl.arraySelectedPermission.push(ctrl.roleObj.permissions);

                });
            ctrl.transfer = function(from, to, index) {
                if (index >= -1) {
                    to.push(from[index]);
                    from.splice(index, 1);
                } else {
                    for (var i = 0; i < from.length; i++) {
                        to.push(from[i]);
                    }
                    from.length = 0;
                }
            };

            ctrl.editRoleCall = function() {
                $http.post('/api/engineeringDashboardUserRoleEdit ', (ctrl.role)).then(function(response) {
                    $route.reload();
                    $uibModalInstance.dismiss("cancel");
                })
            }

        }

        //edit user modal opens//
        ctrl.editUserPopUp = function(wholeObj, userkey, projectidkey, projectidkeystring) {

            $uibModal.open({
                templateUrl: 'app/dashboard/views/editUserModal.html',
                controller: editUserController,
                controllerAs: 'euc',
                resolve: {
                    wholeObj: function() {
                        return wholeObj;
                    },
                    userkey: function() {
                        return userkey;
                    },
                    projectidkey: function() {
                        return projectidkey;
                    },
                    projectidkeystring: function() {
                        return projectidkeystring;
                    }

                }
            });
        }

        //Edit User functionality
        function editUserController($uibModalInstance, wholeObj, $route, $scope, userkey, projectidkey, $cookies, projectidkeystring) {
            var ctrl = this;
            ctrl.wholeObj = wholeObj;
            ctrl.userkey = userkey;
            ctrl.projectidkey = projectidkey;
            ctrl.onlyroles = _.keys(ctrl.wholeObj.userRoles);
            ctrl.projectidkeystring = projectidkeystring;

            //Fetch all roles that is displayed in dual list
            $http.get("/api/allActiveEngineeringDashboardUserRoles")
                .then(function(response) {
                    ctrl.getRolesKey = response.data;
                    /*ctrl.rolekeyarray = [];
                    for (var i = 0; i < ctrl.getRolesKey.length; i++) {
                        ctrl.rolekeyarray.push(ctrl.getRolesKey[i].roleKey);
                    }*/
                });

             var fetchselectedrolesforuser = "/api/getProjectRoles";
            $http.get(fetchselectedrolesforuser+"/"+ctrl.projectidkeystring+"/"+ctrl.userkey)
                .then(function(response) {
                    ctrl.getalluserroles = response.data;
                
                });
           
            //Fetch all user specific dashboards
            var mydashboardRouteProMap = "/api/dashboard/mydashboard";
            $http.get(mydashboardRouteProMap + "?username=" + ctrl.userkey + "&projectId=" + ctrl.projectidkey)
                .then(function(response) {
                    ctrl.selectedItemsDashboard = [];
                    ctrl.selectedItemsDashboard = response.data;
                    /*for (var i = 0; i < ctrl.getUsrSpcificDashboards.length; i++) {
                        ctrl.selectedItemsDashboard.push(ctrl.getUsrSpcificDashboards[i].title);
                    }*/

                })

            //Fetch all dashboards for that project
            var fetchevrydashboard = "/api/dashboard/projectdashboard ";
            $http.get(fetchevrydashboard + "?projectId=" + ctrl.projectidkey)
                .then(function(response) {
                    //ctrl.selectedItemsDashboardSpecific = [];
                    ctrl.selectedItemsDashboardSpecific = response.data;

                   /* for (var i = 0; i < ctrl.geteverydashboard.length; i++) {
                        ctrl.selectedItemsDashboardSpecific.push(ctrl.geteverydashboard[i].title);
                    }*/

                    ctrl.transfer = function(from, to, index) {
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

                })

            ctrl.editUserCall = function() {

                ctrl.projectUserPayl = {
                    "user": ctrl.userkey,
                    "projectId": ctrl.projectidkeystring,
                    "userRoles": ctrl.getalluserroles,
                    "dashboardsToAssign": ctrl.selectedItemsDashboard
                }

                $http.post("/api/projectUsersMapping", (ctrl.projectUserPayl)).then(function(response) {
                    alert("created");
                })
            }
        }
    }
})();