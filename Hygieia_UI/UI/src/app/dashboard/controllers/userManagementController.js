(function() {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('userManagementController', userManagementController);

    userManagementController.$inject = ['$scope', 'codeAnalysisData', 'testSuiteData', '$q', '$filter', '$uibModal', '$location', '$routeParams', '$http', '$route', '$cookies', '$timeout', '$cookieStore','usermanagementData'];

    function userManagementController($scope, codeAnalysisData, testSuiteData, $q, $filter, $uibModal, $location, $routeParams, $http, $route, $cookies, $timeout, $cookieStore,usermanagementData) {
        var ctrl = this;
        ctrl.usernamepro = $cookies.get('username');
        
        //Fetch All Application Users
        $http.get("/api/getApplicationUsers")
            .then(function(response) {
                ctrl.getAppUsers = response.data;
            });

        //Tabs Settings
        $scope.tabs = [{
                title: 'Project-Users',
                url: 'one.tpl.html'
            }, {
                title: 'Roles',
                url: 'two.tpl.html'
            },
             {
                title: 'Users',
                url: 'three.tpl.html'
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

         $http.get("/api/getProjectsByUser/?username=" + ctrl.usernamepro)
            .then(function(response) {
                ctrl.getAllProjects = response.data;
                for (var i = 0; i < ctrl.getAllProjects.length; i++) {
                    for (var j = 0; j < ctrl.getAllProjects[i].usersGroup.length; j++) {
                        for (var k = 0; k < ctrl.getAllProjects[i].usersGroup[j].userRoles.length; k++) {
                            ctrl.vvv = ctrl.getAllProjects[i].usersGroup[j].user;
                            ctrl.projectIDS = ctrl.getAllProjects[i].id;
                            if((ctrl.getAllProjects[i].usersGroup[j].userRoles[k].permissions.indexOf("DELETE_ROLE") > -1) && (ctrl.vvv == ctrl.usernamepro)){
                               ctrl.deleteRole = true;
                            }
                            
                        }
                    }
                }
            });

        

        //Fetch All Active Permissions
        $http.get("/api/allActiveEngineeringDashboardPermissions")
            .then(function(response) {
                ctrl.getAllPermissions = response.data;
            });

        //Edit permission
        ctrl.editpermission = function(info) {
            info.editorEnabled = false;
            $http.post('/api/engineeringDashboardPermissionUpdate', (info)).then(function(response) {
                alert("Edited Successfully");
            })
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
        }

        //Create Roles Functionality
        function postRoleController($uibModalInstance, $http, $route, $timeout, $scope, $cookies) {
            var ctrl = this;
            ctrl.usernamepro = $cookies.get('username');

            //Fetch All Active Permissions to display in dual list
            usermanagementData.fetchallACtivePermissions().then(function(response) {
                ctrl.getAllPermissions = response;
                    ctrl.selectedItems = [];
                    ctrl.role = {
                        "createdBy": ctrl.usernamepro,
                        "description": ctrl.description,
                        "enabled": true,
                        "permissions": ctrl.selectedItems,
                        "roleKey": ctrl.roleKey,
                        "updatedBy": ctrl.usernamepro,
                        "exposetoApi":true
                    }
            });

            //Create role post API call
        
                ctrl.postRole = function() {
                    ctrl.usernamepro = $cookies.get('username');
                        if (ctrl.selectedItems != 0) {
                    usermanagementData.createRolefn(ctrl.role).then(function(response) {
                        $route.reload();
                        $uibModalInstance.dismiss("cancel");
                        $uibModal.open({
                        templateUrl: 'app/dashboard/views/ConfirmationModals/postroleconfirmmodal.html',
                        controller: 'userManagementController',
                        controllerAs: 'umc'
                });

                    })

                }
                else{
                   $uibModal.open({
                    templateUrl: 'app/dashboard/views/ConfirmationModals/selectpermissionrequired.html',
                    controller: 'userManagementController',
                    controllerAs: 'umc'
                });
                }
                 
        };
             /*else {
                $uibModal.open({
                    templateUrl: 'app/dashboard/views/ConfirmationModals/selectpermissionrequired.html',
                    controller: 'userManagementController',
                    controllerAs: 'umc'
                });
            }
*/
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
        ctrl.showmorelink = true;
        ctrl.enableEditor = function(vall) {
            angular.forEach(ctrl.getAllPermissions, function(value, key) {
                if (value.id === vall) {
                    value.editorEnabled = true;
                }
            });

        };

        

        //Fetch all Roles
        usermanagementData.fetchallRoles().then(function(response) {
            ctrl.getRoles = response.data;
                for (var i = 0; i < response.data.length; i++) {
                    if (response.data [i] != undefined) {
                       response.data[i].permission  = Object.keys(response.data[i].permissions).splice(0,2);
                    }
                }

                for (var i = 0; i < response.data.length; i++) {
                    if (response.data [i] != undefined) {
                       response.data[i].permissions  = Object.keys(response.data[i].permissions).splice(2);
                    }
                }
        })
      

             ctrl.onclickmore = true;
             ctrl.onclickless = false;
             ctrl.showmorelink = false;
             ctrl.showlesslink = true;
        ctrl.showMoreFlag =function(){
            ctrl.showmorelink = true;
            ctrl.onclickmore = false;
            ctrl.onclickless = true;
        }
        ctrl.showLessFlag =function(){
            ctrl.showmorelink = false;
             ctrl.showlesslink = true;
             ctrl.onclickmore = true;
            ctrl.onclickless = false;
        }

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
                usermanagementData.deleteRolesfn(pid).then(function(response) {
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

            usermanagementData.getAllPermissionsinedit().then(function(response) {
                  ctrl.getAllPermissions = response.data;
                    ctrl.roleObj = roleObj;
                    ctrl.arrayconverted = ctrl.roleObj.permission.concat(ctrl.roleObj.permissions);
                    ctrl.usernamepro = $cookies.get('username');
                    ctrl.role = {
                        "createdBy": ctrl.usernamepro,
                        "description": ctrl.roleObj.description,
                        "enabled": true,
                        "permissions": ctrl.arrayconverted,
                        "roleKey": ctrl.roleObj.roleKey,
                        "updatedBy": ctrl.usernamepro,
                         "exposetoApi":true
                    }
                    ctrl.getAllPermissions = _.difference(ctrl.getAllPermissions, ctrl.arrayconverted);
                    ctrl.arraySelectedPermission = [];
                    ctrl.arraySelectedPermission.push(ctrl.roleObj.permissions);
            })
         

               /* function differenceArray (parentArr, childArr) {
                    var diffArray = [];
                  parentArr.sort();
                  childArr.sort();
                  for (var i = 0; i < parentArr.length; i += 1) {
                    if (childArr.indexOf(parentArr[i]) > -1) {
                      diffArray.push(parentArr[i]);
                    }
                  }
                  return diffArray;
                    }; */

            

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

            ctrl.editRoleCall = function() {
                usermanagementData.editrolefn(ctrl.role).then(function(response) {
                     $route.reload();
                    $uibModalInstance.dismiss("cancel");
                    $uibModal.open({
                        templateUrl: 'app/dashboard/views/ConfirmationModals/updateRoleConfirmModal.html',
                        controller: userManagementController,
                        controllerAs: 'umc'
                    });

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

             /*function for filtering difference array when passing two arrays*/
        function differenceArray (parentArr, childArr) {
          var diffArray = [];
          parentArr.sort();
          childArr.sort();
          for (var i = 0; i < childArr.length; i += 1) {
             var index = (parentArr && parentArr[i].id).indexOf(childArr);
                parentArr.splice(index,1);
             }
          return parentArr;
        }; 

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
                    ctrl.selectedItemsDashboard = response.data;
                    /*for (var i = 0; i < ctrl.getUsrSpcificDashboards.length; i++) {
                        ctrl.selectedItemsDashboard.push(ctrl.getUsrSpcificDashboards[i].title);
                    }*/

                });


             //Fetch all dashboards for that project
            var fetchevrydashboard = "/api/dashboard/projectdashboard ";
            $http.get(fetchevrydashboard + "?projectId=" + ctrl.projectidkey)
                .then(function(response) {
                    ctrl.selectedItemsDashboardSpecific = response.data;
                    //ctrl.selectedItemsDashboardSpecific = [];
                    /*if (response && response.data) {
                         ctrl.selectedItemsDashboardSpecific =differenceArray(response.data,ctrl.selectedItemsDashboard);
                    }*/
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



            ctrl.editUserCall = function() {
                ctrl.projectUserPayl = {
                    "user": ctrl.userkey,
                    "projectId": ctrl.projectidkeystring,
                    "userRoles": ctrl.getalluserroles,
                    "dashboardsToAssign": ctrl.selectedItemsDashboard
                }

                $http.post("/api/projectUsersMapping", (ctrl.projectUserPayl)).then(function(response) {
                   $route.reload();
                   $uibModalInstance.dismiss("cancel");
                     $uibModal.open({
                        templateUrl: 'app/dashboard/views/ConfirmationModals/updateUserModal.html',
                        controller: userManagementController,
                        controllerAs: 'umc'
                    });

                })
            }
        }

         //delete user modal opens//
        ctrl.deleteUserPopUp = function(wholeObj, userkey, projectidkey, projectidkeystring) {

            $uibModal.open({
                templateUrl: 'app/dashboard/views/ConfirmationModals/deleteUserModal.html',
                controller: deleteUserController,
                controllerAs: 'duc',
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

        function deleteUserController($uibModalInstance, wholeObj, $route, $scope, userkey, projectidkey, $cookies, projectidkeystring){
            var ctrl = this;
            ctrl.editUserCall = function() {
                $http.delete("/api/disassociatedUserFromProject/" + userkey + "/" + projectidkeystring).then(function(response) {
                    $route.reload();
                   $uibModalInstance.dismiss("cancel");
                    $uibModal.open({
                        templateUrl: 'app/dashboard/views/ConfirmationModals/deleteUserConfirmModal.html',
                        controller: userManagementController,
                        controllerAs: 'umc'
                    });
                     
                })
            }
        }
        ctrl.createGlobalDelivery = function(fetchPro){
            $http.post('/api/createGlobalDeliveryUser?username=' + fetchPro).then(function(response) {
                alert("Created Successfully");
            })
        }

        ctrl.revokeAppUser = function(fetchPro){
             $http.delete('/api/RevokeAppUserAccess?username=' + fetchPro).then(function(response) {
                alert("Revoked Successfully");
            })
        }

        ctrl.createGlobalDeliverySysAdmin = function(fetchPro){
           $http.post('/api/createGlobalDeliverySysAdmin?username=' + fetchPro).then(function(response) {
                alert("Created Successfully");
            })
        }

        ctrl.purgeUser = function(fetchPro){
           $http.delete('/api/purgeAppUser?username=' + fetchPro).then(function(response) {
                alert("Removed Successfully");
            })
        }

        
    }
})();