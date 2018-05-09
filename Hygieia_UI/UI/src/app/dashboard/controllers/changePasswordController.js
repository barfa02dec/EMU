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
        vm.cancel = function() {
            $location.path('/projects');
        }
        vm.changePassword = function() {
            //vm.changePasswordPayload = {};
            vm.changePasswordPayload.username = vm.usernamepro;
            projectData.changePasswordFn(vm.changePasswordPayload).then(function (response) {
                    if(response == "User Does not Exist"){
                         $uibModal.open({
                                templateUrl: 'app/dashboard/views/ConfirmationModals/validationExistingPassword.html',
                                controller: 'changePasswordController',
                                controllerAs: 'cpc'
                            });
                    }
                    else if(response == "Existing and new password both are same, Please insert new Password."){
                        $uibModal.open({
                                templateUrl: 'app/dashboard/views/ConfirmationModals/validationNewPassword.html',
                                controller: 'changePasswordController',
                                controllerAs: 'cpc'
                            });
                    }
                    else{
                         $uibModal.open({
                                templateUrl: 'app/dashboard/views/ConfirmationModals/confirmationChangePassword.html',
                                controller: 'changePasswordController',
                                controllerAs: 'cpc'
                            });
                    }
                    
                })
        }
    }
})();
