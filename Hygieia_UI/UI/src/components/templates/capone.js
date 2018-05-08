/**
 * Controller for the dashboard route.
 * Render proper template.
 */
(function () {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('CapOneTemplateController', CapOneTemplateController);

    CapOneTemplateController.$inject = ['$location', '$cookies','$cookieStore','$http'];
    function CapOneTemplateController($location, $cookies,$cookieStore,$http) {
        var ctrl = this;
        ctrl.usernamepro = $cookies.get('username');
        ctrl.tabs = [
            { name: "Widget"},
            { name: "Pipeline"},
            { name: "Cloud"}
           ];

        ctrl.widgetView = ctrl.tabs[0].name;
        ctrl.toggleView = function (index) {
            ctrl.widgetView = typeof ctrl.tabs[index] === 'undefined' ? ctrl.tabs[0].name : ctrl.tabs[index].name;
        };
        ctrl.logout = function () {
            $cookieStore.remove("username");
            $cookieStore.remove("authenticated");
            $location.path('/');
        };

        $http.get("/api/projects/?username=" + ctrl.usernamepro)
            .then(function(response) {
                ctrl.getAllProjects = response.data;
                for (var i = 0; i < ctrl.getAllProjects.length; i++) {
                    for (var j = 0; j < ctrl.getAllProjects[i].usersGroup.length; j++) {
                        for (var k = 0; k < ctrl.getAllProjects[i].usersGroup[j].userRoles.length; k++) {
                            ctrl.vvv = ctrl.getAllProjects[i].usersGroup[j].user;
                            ctrl.projectIDS = ctrl.getAllProjects[i].id;
                            if((ctrl.getAllProjects[i].usersGroup[j].userRoles[k].permissions.indexOf("USER_MANAGEMENT_VIEW") > -1) && (ctrl.vvv == ctrl.usernamepro)){
                               ctrl.usermanagementviews = true;
                            }
                            
                        }
                    }
                }
            });
    }
})();
