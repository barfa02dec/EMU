/**
 * Communicates with dashboard methods on the api
 */
(function () {
    'use strict';

    angular
        .module(HygieiaConfig.module + '.core')
        .constant('DashboardType', {
            PRODUCT: 'product',
            TEAM: 'team'
        })
        .factory('usermanagementData', usermanagementData);

         function usermanagementData($http) {
            return {
                fetchallACtivePermissions: fetchallACtivePermissions,
                createRolefn:createRolefn,
                fetchallRoles:fetchallRoles,
                deleteRolesfn:deleteRolesfn,
                getAllPermissionsinedit:getAllPermissionsinedit,
                editrolefn:editrolefn


            };

         function fetchallACtivePermissions(){
            return   $http.get('/api/allActiveEngineeringDashboardPermissionKeys')
                    .then(function(response) {
                       return response.data;
                    });
            }

         function createRolefn(payload){
            return   $http.post('/api/engineeringDashboardUserRole ', (payload))
                    .then(function(response) {
                       
                            return response.data;
                    });

            
        }

        function fetchallRoles(){
            return   $http.get('/api/allActiveEngineeringDashboardUserRoles')
                    .then(function(response) {
                       
                            return response;
                    });

            
        }

        function deleteRolesfn(pid){
            return   $http.post('/api/deactivateEngineeringDashboardUserRoles?key=' + pid)
                    .then(function(response) {
                       
                            return response;
                    });

            
        }

         function getAllPermissionsinedit(){
            return   $http.get('/api/allActiveEngineeringDashboardPermissionKeys')
                    .then(function(response) {
                       
                            return response;
                    });

            
        }

          function editrolefn(payload){
            return   $http.post('/api/engineeringDashboardUserRoleEdit ', (payload))
                    .then(function(response) {
                       
                            return response;
                    });

            
        }

        
      

        
        

}
})();
