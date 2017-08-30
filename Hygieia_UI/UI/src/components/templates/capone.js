/**
 * Controller for the dashboard route.
 * Render proper template.
 */
(function () {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('CapOneTemplateController', CapOneTemplateController);

    CapOneTemplateController.$inject = ['$location', '$cookies','$cookieStore'];
    function CapOneTemplateController($location, $cookies,$cookieStore) {
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
    }
})();
