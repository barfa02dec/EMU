/**
 * Controller for performing signup a new user */
(function () {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('changePasswordController', changePasswordController);

    changePasswordController.$inject = ['$scope', 'signupData', '$location', '$cookies', '$uibModal', '$cookieStore', 'projectData'];
    function changePasswordController($scope, signupData, $location, $cookies, $uibModal, $cookieStore, projectData) {
        var vm = this;
        vm.usernamepro = $cookies.get('username');
        vm.changePasswordPayload = {};
        //logout functionality
        vm.logout = function () {
            $cookieStore.remove("username");
            $cookieStore.remove("authenticated");
            $location.path('/');
        };
        vm.changePassword = function() {
            //vm.changePasswordPayload = {};
            projectData.changePasswordFn(vm.changePasswordPayload).then(function (response) {
                    alert("Password changed successfully");
                })
        }
    }
})();
