/**
 * Controller for choosing or creating a new dashboard
 */
(function() {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('SiteController', SiteController);

    SiteController.$inject = ['$scope', '$q', '$uibModal', 'dashboardData', '$location', '$cookies', '$cookieStore', 'DashboardType', '$http'];

    function SiteController($scope, $q, $uibModal, dashboardData, $location, $cookies, $cookieStore, DashboardType, $http) {
        var ctrl = this;

        // public variables
        ctrl.search = '';
        ctrl.myadmin = '';
        ctrl.username = $cookies.get('username');
        ctrl.showAuthentication = $cookies.get('authenticated');
        ctrl.templateUrl = 'app/dashboard/views/navheader.html';
        ctrl.dashboardTypeEnum = DashboardType;
        ctrl.ppiid = $cookies.get('ProId');
        // public methods
        ctrl.createDashboard = createDashboard;
        ctrl.deleteDashboard = deleteDashboard;
        ctrl.open = open;
        ctrl.logout = logout;
        ctrl.admin = admin;
        ctrl.setType = setType;
        ctrl.filterNotOwnedList = filterNotOwnedList;
        ctrl.filterDashboards = filterDashboards;
        ctrl.renameDashboard = renameDashboard;
        ctrl.usernamepro = $cookies.get('username');
        ctrl.projectName = $cookies.get('ProName');
        $scope.deleteDashboard = "Dashboard deleted successfully";
        if (ctrl.username === 'admin') {
            ctrl.myadmin = true;
        }
        checkPassThrough();

        (function() {
            // set up the different types of dashboards with a custom icon
            var types = dashboardData.types();
            _(types).forEach(function(item) {
                if (item.id == DashboardType.PRODUCT) {
                    item.icon = 'fa-cubes';
                }
            });

            ctrl.dashboardTypes = types;

            // request dashboards
            dashboardData.search().then(processDashboardResponse, processDashboardError);

            // request my dashboards
            dashboardData.mydashboard(ctrl.ppiid, ctrl.usernamepro).then(processMyDashboardResponse, processMyDashboardError);
        })();

        function setType(type) {
            ctrl.dashboardType = type;
        }

        function filterDashboards(item) {
            var matchesSearch = (!ctrl.search || item.name.toLowerCase().indexOf(ctrl.search.toLowerCase()) !== -1);
            if (ctrl.dashboardType == DashboardType.PRODUCT) {
                return item.isProduct && matchesSearch;
            }

            if (ctrl.dashboardType == DashboardType.TEAM) {
                return !item.isProduct && matchesSearch;
            }

            return matchesSearch;
        }
        ctrl.projID = $cookies.get('ProId');
        
        $http.get("/api/projects/?username=" + ctrl.usernamepro)
            .then(function(response) {
                ctrl.getAllProjects = response.data;

                for (var i = 0; i < ctrl.getAllProjects.length; i++) {
                    for (var j = 0; j < ctrl.getAllProjects[i].usersGroup.length; j++) {
                        for (var k = 0; k < ctrl.getAllProjects[i].usersGroup[j].userRoles.length; k++) {
                            ctrl.vvv = ctrl.getAllProjects[i].usersGroup[j].user;
                            ctrl.projectIDS = ctrl.getAllProjects[i].id;
                            if ((ctrl.getAllProjects[i].usersGroup[j].userRoles[k].permissions.indexOf("EDIT_DASHBOARD") > -1) && (ctrl.vvv == ctrl.usernamepro) && (ctrl.projectIDS == ctrl.projID)) {
                                ctrl.editDashboard = true;
                            }
                            if((ctrl.getAllProjects[i].usersGroup[j].userRoles[k].permissions.indexOf("DELETE_DASHBOARD") > -1) && (ctrl.vvv == ctrl.usernamepro) && (ctrl.projectIDS == ctrl.projID)){
                               ctrl.deleteDashboards = true;
                            }
                            if((ctrl.getAllProjects[i].usersGroup[j].userRoles[k].permissions.indexOf("CREATE_DASHBOARD") > -1) && (ctrl.vvv == ctrl.usernamepro) && (ctrl.projectIDS == ctrl.projID)){
                               ctrl.createDashboards = true;
                            }
                            if((ctrl.getAllProjects[i].usersGroup[j].userRoles[k].permissions.indexOf("DASHBOARD_LIST_VIEW") > -1) && (ctrl.vvv == ctrl.usernamepro) && (ctrl.projectIDS == ctrl.projID)){
                               ctrl.dashboardListViews = true;
                            }
                            if((ctrl.getAllProjects[i].usersGroup[j].userRoles[k].permissions.indexOf("VIEW_DASHBOARD") > -1) && (ctrl.vvv == ctrl.usernamepro) && (ctrl.projectIDS == ctrl.projID)){
                               ctrl.dashboardViewWidget = true;
                            }
                            if((ctrl.getAllProjects[i].usersGroup[j].userRoles[k].permissions.indexOf("USER_MANAGEMENT_VIEW") > -1) && (ctrl.vvv == ctrl.usernamepro)){
                               ctrl.usermanagementviews = true;
                            }
                        }
                    }
                }



            });

        function checkPassThrough() {
            if (angular.isUndefined(ctrl.username) || angular.isUndefined(ctrl.showAuthentication) || ctrl.showAuthentication == false) {
                console.log('Authentication failed, redirecting to login page');
                $location.path('/login');
            }

        }

        function admin() {
            console.log('sending to admin page');
            $location.path('/admin');
        }

        function logout() {
            $cookieStore.remove("username");
            $cookieStore.remove("authenticated");
            $location.path('/');
        }

        // method implementations
        function createDashboard(ProId) {
            // open modal for creating a new dashboard
            $uibModal.open({
                templateUrl: 'app/dashboard/views/createDashboard.html',
                controller: 'CreateDashboardController',
                controllerAs: 'ctrl'
            });
        }

        function renameDashboard(item) {
            // open modal for renaming dashboard
            $uibModal.open({
                templateUrl: 'app/dashboard/views/renameDashboard.html',
                controller: 'RenameDashboardController',
                controllerAs: 'ctrl',
                resolve: {
                    dashboardId: function() {
                        return item.id;
                    },
                    dashboardName: function() {
                        return item.name;
                    }
                }
            });
        }


        function open(dashboardId) {
            $cookies.get("selectedName");
            $cookies.get("selectedNameSonar");

            $location.path('/dashboard/' + dashboardId);
            $cookies.put('dashboardidpa', dashboardId);
        }

        function processDashboardResponse(data) {
            // add dashboards to list
            ctrl.dashboards = [];
            var dashboards = [];
            for (var x = 0; x < data.length; x++) {
                var board = {
                    id: data[x].id,
                    name: data[x].title,
                    isProduct: data[x].type && data[x].type.toLowerCase() === DashboardType.PRODUCT.toLowerCase()
                };

                if (board.isProduct) {
                    //console.log(board);
                }
                dashboards.push(board);
            }

            ctrl.dashboards = dashboards;
        }

        function processDashboardError(data) {
            ctrl.dashboards = [];
        }

        function processMyDashboardResponse(mydata) {
            var projectName;
            var projectId;
            for(var i = 0; i < mydata.length; i++){
                for (var j = 0; j < mydata[i].widgets.length; j++) {
                   if(mydata[i].widgets[j].name == "feature"){
                        projectName = mydata[i].widgets[j].options.projectName;
                        projectId = mydata[i].widgets[j].options.projectId;
                       
                         $cookies.put('projectNameJira', projectName);
                          $cookies.put('projectIdJira', projectId);
                   }
                }

            }

            ctrl.rawdata = mydata;
            // add dashboards to list
            ctrl.mydash = [];
            var dashboards = [];
            for (var x = 0; x < mydata.length; x++) {

                dashboards.push({
                    id: mydata[x].id,
                    name: mydata[x].title,
                    type: mydata[x].type,
                    owner: mydata[x].owner,
                    createdOn: mydata[x].createdOn,
                    isProduct: mydata[x].type && mydata[x].type.toLowerCase() === DashboardType.PRODUCT.toLowerCase()
                });
            }

            ctrl.mydash = dashboards;
        }

        function processMyDashboardError(data) {
            ctrl.mydash = [];
        }




        function deleteDashboard(item) {
            var id = item.id;
            dashboardData.delete(id).then(function() {
                _.remove(ctrl.dashboards, {
                    id: id
                });
                _.remove(ctrl.mydash, {
                    id: id
                });
            }, function(response) {
                var msg = 'An error occurred while deleting the dashboard';

                if (response.status > 204 && response.status < 500) {
                    msg = 'The Team Dashboard is currently being used by a Product Dashboard/s. You cannot delete at this time.';
                }

                swal(msg);
            });
        }

        function filterNotOwnedList(db1, db2) {

            console.log("size before is:" + db1.length);

            var jointArray = db1.concat(db2);

            console.log("size after is:" + jointArray.length);

            var uniqueArray = jointArray.filter(function(elem, pos) {
                return jointArray.indexOf(elem) == pos;
            });

            console.log("size after reduction  is:" + uniqueArray.length);
            ctrl.dashboards = uniqueArray;
        }

        ctrl.deleteDashboardModel = function(myitem) {

            ctrl.dashdataData = myitem;


            $uibModal.open({
                templateUrl: 'app/dashboard/views/deleteDahboardPopup.html',
                controller: deleteDashboardController,
                controllerAs: 'ddc',
                resolve: {
                    dashid: function() {
                        return myitem.id;
                    }
                }

            });
        }

        function deleteDashboardController($uibModalInstance, dashid, $route, $http) {
            var ddc = this;
            ddc.deleteDashboardfn = function(pro) {
                $http.delete('/api/dashboard/' + dashid).then(function(response) {
                    $uibModalInstance.dismiss("cancel");
                    $route.reload();
                    $uibModal.open({
                        template: '<confirm-popup msg="deleteDashboard" icon="btn btn-info project-map-add-btn inner-btn-prop" action="$close()"></confirm-popup>',
                        controller: 'SiteController',
                        controllerAs: 'ctrl'
                    });

                });

            };
        }

        /* function deleteDahboardPopupController($uibModalInstance, dashid, $route,dashboardData) {

             var dpmObjs = this;
            var id = dashid;
             dashboardData.search().then(processDashboardResponse, processDashboardError);

              function processDashboardResponse(data) {
            // add dashboards to list
            var id = data.id;
            dpmObjs.dashboards = [];
            var dashboards = [];
            for (var x = 0; x < data.length; x++) {
                var board = {
                    id: data[x].id,
                    name: data[x].title,
                    isProduct: data[x].type && data[x].type.toLowerCase() === DashboardType.PRODUCT.toLowerCase()
                };

                if(board.isProduct) {
                    //console.log(board);
                }
                dashboards.push(board);
            }

            dpmObjs.dashboards = dashboards;
        }

         function processDashboardError(data,id) {
            dpmObjs.dashboards = [];
        }

           dpmObjs.deletesubmit = function (id) {
            dashboardData.delete(id).then(function () {
                _.remove(dpmObjs.dashboards, {id: id});
                _.remove(dpmObjs.mydash, {id: id});
            }, function(response) {
                var msg = 'An error occurred while deleting the dashboard';

                if(response.status > 204 && response.status < 500) {
                    msg = 'The Team Dashboard is currently being used by a Product Dashboard/s. You cannot delete at this time.';
                }

                swal(msg);
            });
        }

           

        }*/
    }


})();