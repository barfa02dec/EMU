/**
 * Build widget configuration
 */
(function () {
    'use strict';
    angular
        .module(HygieiaConfig.module)
        .controller('BuildWidgetConfigController', BuildWidgetConfigController);
    BuildWidgetConfigController.$inject = ['modalData', '$scope', 'collectorData', '$uibModalInstance','$cookies','$rootScope'];
    function BuildWidgetConfigController(modalData, $scope, collectorData, $uibModalInstance,$cookies,$rootScope) {
        var ctrl = this,
        widgetConfig = modalData.widgetConfig;
        
        // public variables
        ctrl.buildDurationThreshold = 3;
        ctrl.buildConsecutiveFailureThreshold = 5;
        ctrl.projectspcID = $cookies.get('ProSpId');
        $scope.getJobs = function (filter) {
            $cookies.put('getfilter', filter);
            $scope.projectspcID = $cookies.get('ProSpId');
            return collectorData.itemsByType('build',$scope.projectspcID,{"search": filter}).then(function (response){
                return response;
            });
        }
        
        loadSavedBuildJob();
        // set values from config
        if (widgetConfig) {
            if (widgetConfig.options.buildDurationThreshold) {
                ctrl.buildDurationThreshold = widgetConfig.options.buildDurationThreshold;
            }
            if (widgetConfig.options.consecutiveFailureThreshold) {
                ctrl.buildConsecutiveFailureThreshold = widgetConfig.options.consecutiveFailureThreshold;
            }
        }
        // public methods
        ctrl.submit = submitForm;

        // method implementations
        function loadSavedBuildJob(){
            var buildCollector = modalData.dashboard.application.components[0].collectorItems.Build,
            savedCollectorBuildJob = buildCollector ? buildCollector[0].description : null;
            if(savedCollectorBuildJob) { 
                $scope.getJobs(savedCollectorBuildJob).then(getBuildsCallback) 
            }
        }
        
        function getBuildsCallback(data) {
            ctrl.collectorItemId = data[0];
        }

        function submitForm(valid, collector) {
            $scope.collId = collector.collectorId;
            $cookies.put('mycollector', $scope.collId);
                 $rootScope.$broadcast('eventNameJenkins', { message: collector.id });
            console.log("Collector" + JSON.stringify(collector));
            if (valid) {
                var form = document.buildConfigForm;
                var postObj = {
                    name: 'build',
                    options: {
                        id: widgetConfig.options.id,
                        buildDurationThreshold: parseFloat(form.buildDurationThreshold.value),
                        consecutiveFailureThreshold: parseFloat(form.buildConsecutiveFailureThreshold.value)
                    },
                    componentId: modalData.dashboard.application.components[0].id,
                    collectorItemId: collector.id,
                };
                $cookies.put('colId', modalData.dashboard.application.components[0].id);
                // pass this new config to the modal closing so it's saved
                $uibModalInstance.close(postObj);
            }
        }
    }
})();
