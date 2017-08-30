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
        }, {
            title: 'Permissions',
            url: 'three.tpl.html'
        }];

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
            .then(function(response){
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
            $http.get("/api/allActiveEngineeringDashboardPermissions")
                .then(function(response) {
                    response.data.selectedItems = [];
                    ctrl.getAllPermissions = response.data;

                    /*ctrl.getAllPermissions = response.data;
                    ctrl.newArr = [];
                    for (var i = 0; i < ctrl.getAllPermissions.length; i++) {
                        if (ctrl.getAllPermissions[i].name != undefined) {
                            ctrl.newArr.push(ctrl.getAllPermissions[i].name);
                        }
                    }
*/                    
                });

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

            //Create Role Payload Object
            ctrl.role = {
                "createdBy": ctrl.usernamepro,
                //"createdOn": "string",
                "description": ctrl.description,
                "enabled": true,
                /*"id": {
                    "counter": 0,
                    "date": "2017-07-19T10:03:24.900Z",
                    "machineIdentifier": 0,
                    "processIdentifier": 0,
                    "time": 0,
                    "timeSecond": 0,
                    "timestamp": 0
            },*/
                /*"lastUpdatedOn": "string",*/
                "permissions":ctrl.getAllPermissions,
                "roleKey": ctrl.roleKey,
                "updatedBy": ctrl.usernamepro
            }

            //Create role post API call
            console.log(ctrl.role);
            ctrl.postRole = function() {
                $http.post('/api/engineeringDashboardUserRole ', (ctrl.role)).then(function(response) {
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
                for(var i=0;i<response.data.length;i++){
                    if(response.data[i].permissions != undefined){
                        ctrl.getOnlyPerArray.push(response.data[i].permissions)
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
                    /* $uibModal.open({
                         templateUrl: 'app/dashboard/views/deleteconfirm.html',
                         controller: 'projectMapController',
                         controllerAs: 'pm'
                     });*/
                })
            };
        }

        //Fetch all Projects and its respective Users.
        $http.get("/api/getProjects")
            .then(function(response) {
                ctrl.getprojectsUsers = response.data;
                console.log(ctrl.getprojectsUsers);

                //ctrl.getAllPermissions = response.data;
                    ctrl.newArr = [];
                    for (var i = 0; i < ctrl.getprojectsUsers.length; i++) {
                        if (ctrl.getprojectsUsers != undefined) {
                            ctrl.newArr.push(ctrl.getprojectsUsers[i].usersGroup);
                        }
                    }
                    console.log(ctrl.newArr);
                    ctrl.newArrusergrop = [];
                    for (var i = 0; i < ctrl.newArr.length; i++) {
                        if (ctrl.newArr != undefined) {
                            ctrl.newArrusergrop.push(ctrl.newArr[i].user);
                        }
                    }
                    //console.log(ctrl.newArrusergrop);
            });

    }
})();