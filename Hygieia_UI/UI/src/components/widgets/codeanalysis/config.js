/**
 * Code Analysis widget configuration
 */
(function () {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('CodeAnalysisConfigController', CodeAnalysisConfigController);

    CodeAnalysisConfigController.$inject = ['$rootScope', 'modalData', 'selectedNameSonar', '$scope', 'collectorData', '$uibModalInstance','$cookies'];
    function CodeAnalysisConfigController($rootScope, modalData, selectedNameSonar, $scope, collectorData, $uibModalInstance,$cookies) {
        var ctrl = this,
        widgetConfig = modalData.widgetConfig,
        component = modalData.dashboard.application.components[0];
        $scope.selectedNameSonar = selectedNameSonar;
        ctrl.saToolsDropdownPlaceholder = 'Loading Security Analysis Jobs...';
        ctrl.testToolsDropdownPlaceholder = 'Loading Functional Test Jobs...';

        // public methods
        ctrl.caLoading = true;
        ctrl.submit = submitForm;
        ctrl.addTestConfig = addTestConfig;
        ctrl.deleteTestConfig = deleteTestConfig;
        ctrl.projectspcID = $cookies.get('ProSpId');
        $scope.getCodeQualityCollectors = function(filter){
            $scope.projectspcID = $cookies.get('ProSpId');
        	return collectorData.itemsByType('codequality',ctrl.projectspcID,{"search": filter}).then(function (response){
        		return response;
        	});
        }

        loadSavedCodeQualityJob();

        // request all the codequality and test collector items
        collectorData.itemsByType('staticSecurityScan').then(processSaResponse);
        collectorData.itemsByType('test').then(processTestsResponse);

        function loadSavedCodeQualityJob(){
        	var codeQualityCollectorItems = component.collectorItems.CodeQuality,
            savedCodeQualityJob = codeQualityCollectorItems ? codeQualityCollectorItems[0].description : null;

            if(savedCodeQualityJob){
            	$scope.getCodeQualityCollectors(savedCodeQualityJob).then(getCodeQualityCollectorsCallback) ;
            }
        }

        function getCodeQualityCollectorsCallback(data) {
            ctrl.caCollectorItem = data[0];
        }

        function processSaResponse(data) {
            var saCollectorItems = component.collectorItems.StaticSecurityScan;
            var saCollectorItemId = _.isEmpty(saCollectorItems) ? null : saCollectorItems[0].id;

            ctrl.saJobs = data;
            ctrl.saCollectorItem = saCollectorItemId ? _.findWhere(ctrl.saJobs, {id: saCollectorItemId}) : null;
            ctrl.saToolsDropdownPlaceholder = data.length ? 'Select a Security Analysis Job' : 'No Security Analysis Job Found';
        }

        function processTestsResponse(data) {
            ctrl.testJobs = data;
            ctrl.testConfigs = [];
            var testCollectorItems = component.collectorItems.Test;
            var testCollectorItemIds = [];
            var testJobNamesFromWidget = [];
            // set values from config
            if (widgetConfig) {
                if (widgetConfig.options.testJobNames) {
                    var j;
                    for (j = 0; j < widgetConfig.options.testJobNames.length; ++j) {
                        testJobNamesFromWidget.push(widgetConfig.options.testJobNames[j]);
                    }
                }
            }
            var index;
            if (testCollectorItems != null) {
                for (index = 0; index < testCollectorItems.length; ++index) {
                    testCollectorItemIds.push(testCollectorItems[index].id);
                }
            }
            for (index = 0; index < testCollectorItemIds.length; ++index) {
                var testItem = testCollectorItemIds ? _.findWhere(ctrl.testJobs, {id: testCollectorItemIds[index]}) : null;
                ctrl.testConfigs.push({
                    testJobName: testJobNamesFromWidget[index],
                    testJob: ctrl.testJobs,
                    testCollectorItem: testItem
                });
            }
            ctrl.testToolsDropdownPlaceholder = data.length ? 'Select a Functional Test Job' : 'No Functional Test Jobs Found';
        }

        function submitForm(caCollectorItem, saCollectorItem, testConfigs) {
            //$cookies.put('selectedNameSonar', $scope.caWidget.caCollectorItem.id);
            $rootScope.$broadcast('eventName', { message: $scope.caWidget.caCollectorItem.id });
            // alert($scope.caWidget.caCollectorItem.id);
            $scope.sonarCollectr = caCollectorItem.collectorId;
            $cookies.put('sonarCollectrid', $scope.sonarCollectr);
            var collectorItems = [];
            var testJobNames = [];
            $scope.selectedNameSonar = $scope.caWidget.caCollectorItem.collectorId;
            if (caCollectorItem) collectorItems.push(caCollectorItem.id);
            if (saCollectorItem) collectorItems.push(saCollectorItem.id);
            if (testConfigs) {
                var index;
                for (index = 0; index < testConfigs.length; ++index) {
                    collectorItems.push(testConfigs[index].testCollectorItem.id);
                    testJobNames.push(testConfigs[index].testJobName);
                }
            }
            var form = document.configForm;
            var postObj = {
                name: 'codeanalysis',
                options: {
                    id: widgetConfig.options.id,
                    testJobNames: testJobNames
                },
                componentId: component.id,
                collectorItemIds: collectorItems
            };
            $cookies.put('compIdSonar', component.id);
            // pass this new config to the modal closing so it's saved
            $uibModalInstance.close(postObj);
        }


        function addTestConfig() {
            var newItemNo = ctrl.testConfigs.length + 1;
            ctrl.testConfigs.push({testJobName: 'Name' + newItemNo, testJob: ctrl.testJobs, testCollectorItem: null});
        }

        function deleteTestConfig(item) {
            ctrl.testConfigs.pop(item);
        }
    }
})();
