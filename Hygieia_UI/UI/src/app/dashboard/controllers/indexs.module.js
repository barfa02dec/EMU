(function() {
    'use strict';
    angular.module(HygieiaConfig.module);
})();

(function() {
    'use strict';

    angular.module(HygieiaConfig.module)
        .controller('customdashboardController', ['dashboardConstant', 'dashboardService', '$q', '$scope',
            function(dashboardConstant, dashboardService, $q, $scope) {
                var vm = this,
                    promises = {
                        dashboardState: dashboardService.getDashboardState(),
                        gadgetList: dashboardService.getGadgetList(),
                        layoutList: dashboardService.getLayoutList()
                    };
                $q.all(promises).then(function(values) {
                    vm.layoutOptions = values.layoutList.layouts;
                    vm.selectedLayout = vm.layoutOptions[0];
                    vm.widgetList = dashboardService.getWidgetList(values.dashboardState.dashboards[0].gadgetPositions,
                        values.gadgetList.gadgetList);
                    $scope.models = dashboardService.getDefaultModel(values.dashboardState.dashboards[0],
                        values.gadgetList.gadgetList, vm.selectedLayout.name).model;
                });
                vm.isEditLayout = true;
            }
        ])
})();

(function() {
    'use strict';
    angular.module(HygieiaConfig.module)
        .constant('dashboardConstant', {
            CONTAINERS: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R'],
            FAKE_RESPONSE: {
                DASHBOARD_STATE: { "dashboards": [{ "id": 109, "layoutId": 1, "name": "test.rex211 - Dashboard", "gadgetPositions": { "A": ["Completeness"], "B": [], "C": [], "D": [], "E": [], "F": [], "G": [], "H": [], "I": [], "J": [], "K": [], "L": [], "M": [], "N": [], "O": [], "P": [], "Q": [], "R": [] }, "derived": false }] },
                LAYOUT: {
                    "layouts": [
                        { "id": 1, "name": "FIRST", "file": "FILE", "image": "IMAGE" },
                        { "id": 2, "name": "SECOND", "file": null, "image": "layout_2" },
                        { "id": 3, "name": "THIRD", "file": null, "image": "layout_3" },
                        { "id": 4, "name": "FOUR", "file": null, "image": "layout_4" },
                        { "id": 5, "name": "FIFTH", "file": null, "image": "layout_5" }
                    ]
                },
                WIDGET_LIST: {
                    "gadgetList": [
                        { "id": 1, "name": "Completeness Report", "description": "Completeness demo widget", "viewName": "Completeness", "image": "issue_summary_gadget" }
                    ]
                }
            }
        })
})();

(function() {
    'use strict';
    angular.module(HygieiaConfig.module)
        .directive('dndWrapper', ['$uibModal', function($uibModal) {
            return {
                restrict: 'E',
                scope: {
                    dndDropZone: '@',
                    dndModel: '=',
                    ctrlScope: '=',
                    isEditMode: '=',
                    widgetList: '='
                },
                templateUrl: 'app/dashboard/views/dndWrapper.dir.html',
                link: function(scope) {
                    var uibInstance,
                        setAddedValue = function(widget) {
                            for (var i = 0; i < scope.widgetList.length; i++) {
                                if (scope.widgetList[i].name === widget.name) {
                                    scope.widgetList[i].added = false;
                                    break;
                                }
                            }
                        };
                    scope.showAddGadgetModal = function() {
                        uibInstance = $uibModal.open({
                            animation: 'true',
                            templateUrl: 'add-gadget.html',
                            scope: scope,
                            size: 'dnd-wrapper'
                        })
                    };

                    scope.addGadget = function(widget) {
                        scope.showMenu = false;
                        widget.added = true;
                        if (!scope.dndModel.dropzones[scope.dndDropZone]) {
                            scope.dndModel.dropzones[scope.dndDropZone] = [];
                        }
                        if (scope.dndModel.dropzones[scope.dndDropZone].indexOf(widget) !== -1) {
                            return;
                        }
                        scope.dndModel.dropzones[scope.dndDropZone].push(widget);
                        uibInstance.close();
                    };

                    scope.componentMoved = function(index) {
                        scope.dndModel.dropzones[scope.dndDropZone].splice(index, 1);
                    }

                    scope.removeComponent = function() {
                        var widget = scope.dndModel.dropzones[scope.dndDropZone][0];
                        setAddedValue(widget);
                        scope.dndModel.dropzones[scope.dndDropZone].splice(0, 1);
                        scope.showMenu = false;
                    }
                }
            }
        }]);
})();

(function() {
    'use strict';
    angular.module(HygieiaConfig.module)
        .service('dashboardService', ['$q', 'dashboardConstant', function($q, dashboardConstant) {

            var getGadgetObj = function(viewName, gadgetList) {
                for (var i = 0; i < gadgetList.length; i++) {
                    if (viewName === gadgetList[i].viewName) {
                        return gadgetList[i];
                    }
                }
            };


            //put in to resolve
            this.getDashboardState = function() {
                return $q(function(resolve) {
                    setTimeout(function() {
                        resolve(dashboardConstant.FAKE_RESPONSE.DASHBOARD_STATE);
                    }, 1000);
                });
            };

            this.getGadgetList = function() {
                return $q(function(resolve) {
                    setTimeout(function() {
                        resolve(dashboardConstant.FAKE_RESPONSE.WIDGET_LIST);
                    }, 1000);
                });
            };

            this.getLayoutList = function() {
                return $q(function(resolve) {
                    setTimeout(function() {
                        resolve(dashboardConstant.FAKE_RESPONSE.LAYOUT);
                    }, 1000);
                });
            };

            //put in service
            this.getWidgetList = function(dashboardWidgets, widgetList, isReset) {
                var widgets = [];
                if (isReset) {
                    angular.forEach(widgetList, function(obj) {
                        obj.added = false;
                    })
                }
                angular.forEach(dashboardWidgets, function(list, zone) {
                    var viewName;
                    if (list.length) {
                        viewName = isReset ? list[0].viewName : list[0];
                        angular.forEach(widgetList, function(obj) {
                            if (obj.viewName === viewName) {
                                obj.added = true;
                            }
                        });
                    }
                });
                return widgetList;
            };

            this.getDefaultModel = function(dashboard, gadgetList, selectedLayout) {
                var model = {
                        selected: null,
                        dropzones: {}
                    },
                    isDashboardEmpty = true;
                if (!dashboard.derived) {
                    model.id = dashboard.id;
                }
                model.layoutId = dashboard.layoutId;
                angular.forEach(dashboardConstant.LAYOUTS, function(zone) {
                    model.dropzones[zone] = [];
                });
                if (dashboard.gadgetPositions) {
                    angular.forEach(dashboard.gadgetPositions, function(gadget, zone) {
                        model.dropzones[zone] = [];
                        if (gadget.length) {
                            isDashboardEmpty = false;
                            angular.forEach(gadget, function(viewName) {
                                model.dropzones[zone].push(getGadgetObj(viewName, gadgetList))
                            })
                        }
                    });
                }
                return {
                    model: model,
                    isDashboardEmpty: isDashboardEmpty
                };
            };
        }]);
})();
