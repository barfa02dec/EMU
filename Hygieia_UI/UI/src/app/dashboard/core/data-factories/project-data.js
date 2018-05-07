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
        .factory('projectData', projectData);

    function projectData($http) {
       

        return {
            fetchallprojects: fetchallprojects,
            deleteProjectsfn:deleteProjectsfn,
            postProjectfn:postProjectfn,
            editprojectfn:editprojectfn,
            fetchallusers:fetchallusers,
            fetchalldashboard:fetchalldashboard,
            fetchallroles:fetchallroles,
            postDefect:postDefect,
            postSprint:postSprint,
            postRelease:postRelease,
            postHeatMap:postHeatMap,
            updateHeatMap:updateHeatMap,
            fetchallCustomers:fetchallCustomers,
            changePasswordFn:changePasswordFn,
            updateSprint:updateSprint,
            updateRelease:updateRelease,
            updateDefect:updateDefect
           
        };

         function fetchallprojects(usrnam){

            return   $http.get("/api/projects/?username=" + usrnam)
                    .then(function(response) {
                       return response.data;
            });

            
        }
       function deleteProjectsfn(pid){

            return   $http.delete('/api//projects/' + pid)
                    .then(function(response) {
                       
                            return response.data;
                    });

            
        }

        function postProjectfn(payload){
            return   $http.post('/api/projects', (payload))
                    .then(function(response) {
                       
                            return response.data;
                    });

        }

        function editprojectfn(payload){
            return   $http.put('/api/projects', (payload))
                    .then(function(response) {
                       
                            return response.data;
                    });

        }

         function fetchallusers(id){
            return   $http.get('/api/getApplicationUsers/' + id)
                    .then(function(response) {
                       
                            return response.data;
                    });

        }
        function fetchalldashboard(username,id){
             var mydashboardRouteProMap = '/api/dashboard/mydashboard';
            return   $http.get(mydashboardRouteProMap + '?username=' + username + '&projectId=' + id)
                    .then(function(response) {
                       
                            return response;
                    });
        }

        function fetchallroles(){
            return   $http.get('/api/allActiveEngineeringDashboardUserRoles')
                    .then(function(response) {
                       
                            return response;
                    });
        }

        function postDefect(payload){
            return   $http.post('/api/defectSummary', (payload))
                    .then(function(response) {
                       return response.data;
                    });
        }

        function updateDefect(payload){
            return   $http.put('/api/defectSummary', (payload))
                    .then(function(response) {
                       return response.data;
                    });
        }

         function postSprint(payload){
            return   $http.post('/api/sprints', (payload))
                    .then(function(response) {
                       return response.data;
                    });
        }

        function updateSprint(payload){
            return   $http.put('/api/sprints', (payload))
                    .then(function(response) {
                       return response.data;
                    });
        }

        function postRelease(payload){
            return   $http.post('/api/releases', (payload))
                    .then(function(response) {
                       return response.data;
                    });
        }

        function updateRelease(payload){
            return   $http.put('/api/releases', (payload))
                    .then(function(response) {
                       return response.data;
                    });
        }
        
        function postHeatMap(payload) {
            return   $http.post('/api/heatmaps', (payload))
                    .then(function(response) {
                       return response.data;
                    });
        }

        function updateHeatMap(payload) {
            http://localhost:3001/api/projectheatmaps/update/{objectId}
            return   $http.put('/api/heatmaps', (payload))
                    .then(function(response) {
                       return response.data;
                    });
        }

        function fetchallCustomers(){
            return   $http.get('/api/customers')
                    .then(function(response) {
                       return response.data;
                    });
                }
        function changePasswordFn(payload){
            return   $http.post('/api/changePassword', (payload))
                    .then(function(response) {
                       
                            return response.data;
                    });

        }
        }
})();
