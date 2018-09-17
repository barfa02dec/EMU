(function() {
    'use strict';
    angular
        .module(HygieiaConfig.module + '.core')
        .directive('stringToNumber', function() {
            return {
                require: 'ngModel',
                link: function(scope, element, attrs, ngModel) {
                    ngModel.$parsers.push(function(value) {
                        if(value){
                            return '' + value;
                        }
                        else{
                            return value;
                        }

                        
                    });
                    ngModel.$formatters.push(function(value) {
                        return parseFloat(value, 10);
                    });
                }
            };
        });
})();