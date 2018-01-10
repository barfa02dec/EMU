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
            postRelease:postRelease
           
        };

         function fetchallprojects(usrnam){

            return   $http.get("/api/getProjectsByUser/?username=" + usrnam)
                    .then(function(response) {
                       
                            return response.data;
                    });

            
        }
       function deleteProjectsfn(pid){

            return   $http.delete('/api//deleteProject/' + pid)
                    .then(function(response) {
                       
                            return response.data;
                    });

            
        }

        function postProjectfn(payload){
            return   $http.post('/api/createProject ', (payload))
                    .then(function(response) {
                       
                            return response.data;
                    });

        }

        function editprojectfn(payload){
            return   $http.post('/api/updateProject', (payload))
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
            return   $http.post('/api/defectSummery', (payload))
                    .then(function(response) {
                       return response.data;
                    });
        }

         function postSprint(payload){
            return   $http.post('/api/sprintMetrics', (payload))
                    .then(function(response) {
                       return response.data;
                    });
        }

        function postRelease(payload){
            return   $http.post('/api/releaseMetrcis', (payload))
                    .then(function(response) {
                       return response.data;
                    });
        }
        

}
})();
