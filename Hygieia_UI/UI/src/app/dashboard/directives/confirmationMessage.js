(function() {
    'use strict';
    angular
        .module(HygieiaConfig.module + '.core')
        .directive('confirmPopup', function($uibModal) {
            return {
                transclude: 'true',
                template: '<div class="col-md-12 main-header-prop">' +
                    '<div class="col-md-12 message-text-props">{{msg}}</div>' +
                    '<div class="col-md-12"></div>' +
                    '<div class="col-md-5"></div>' +
                    '<div class="col-md-2 ok-btn-prop">' +
                    ' <input type="button" name="namess" class={{icon}} value="OK" ng-click=action()>' +
                    '</div>' +
                    '</div>',
                scope: {
                    msg: "=",
                    action: "&",
                    icon: '@'
                },
                 link: function(scope,elem,attr){
            // code goes here ...
        }
            }
        });
})();
