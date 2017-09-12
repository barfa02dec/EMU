/**
 * Widget directives should be used in layout fines to define the
 * specific type of widget to be used in that space
 */
(function() {
    'use strict';

    angular
        .module(HygieiaConfig.module + '.core')

        // used by widgets to set their current state
        // CONFIGURE will render the common config screen instead of the widget content
        .constant('WidgetState', {
            READY: 1,
            CONFIGURE: 2,
            WAITING: 3,
            NOT_COLLECTING: 4
        })

        // constant to be used by widgets to set their state
        // ERROR: causes the widget's panel to use the 'panel-danger' class
        .constant('DisplayState', {
            DEFAULT: 1,
            ERROR: 2
        })
        .directive('widget', widgetDirective);

    widgetDirective.$inject = ['$controller', '$http', '$templateCache', '$compile', 'widgetManager', '$uibModal', 'WidgetState', 'DisplayState', '$interval', 'dashboardData', '$cookies'];

    function widgetDirective($controller, $http, $templateCache, $compile, widgetManager, $uibModal, WidgetState, DisplayState, $interval, dashboardData, $cookies) {

        return {
            templateUrl: 'app/dashboard/views/widget.html',
            require: '^widgetContainer',
            restrict: 'E',
            controller: controller,
            scope: {
                widget: '=',
                title: '@widgetTitle'
            },
            link: link
        };

        function link(scope, element, attrs, containerController) {
            // make it so name is not case sensitive
            attrs.name = attrs.name.toLowerCase();

            scope.$widgetEl = element;
            scope.container = containerController;
            scope.widgetDefinition = widgetManager.getWidget(attrs.name);
            scope.title = attrs.title || scope.widgetDefinition.view.defaults.title;
            scope.header = attrs.header ? attrs.header != 'false' : true;


            // when the widget loads, register it with the container which will then call back to process
            // the widget with the proper config value if it's already been configured on the dashboard
            containerController.registerWidget({
                callback: processWidget,
                element: element,
                attrs: attrs
            });

            // determine what state it's in based on the passed config and load accordingly
            function processWidget(configFromApi, widgetConfig, dashboard) {
                // make sure widget has access to dashboard and config
                scope.dashboard = dashboard;
                scope.widgetConfig = widgetConfig;

                // when the widget registers and sets a 'getState' method use that
                // instead of the default logic to determine whether the widget should be loaded
                if (scope.widgetDefinition.getState) {
                    scope.state = scope.widgetDefinition.getState(widgetConfig);
                } else if (!configFromApi) {
                    if (scope.widgetDefinition.config) {
                        scope.state = WidgetState.CONFIGURE;
                    }
                }

                scope.init();
            }
        }

        function controller($scope, $element, $routeParams,$cookies) {
            $scope.selectedDashboard = $routeParams.id;
            $scope.selectedName = $cookies.get('selectedName');
             $scope.selectedNameSonar = $cookies.get('selectedNameSonar');
          
            
            //debugger;
            $scope.widget_state = WidgetState;
            $scope.display_state = DisplayState;

            // default variables
            $scope.title = '';
            $scope.state = WidgetState.READY;
            $scope.display = DisplayState.DEFAULT;

            // to be set by link
            $scope.widgetConfig = null;
            $scope.widgetDefinition = null;
            $scope.dashboard = null;
            $scope.container = null;
            $scope.owner = null;

            $scope.alerts = [];


            $scope.upsertWidget = upsertWidget;
            $scope.closeAlert = function(index) {
                $scope.alerts.splice(index, 1);
            };

            $scope.lastUpdatedDisplay = '';

            // public methods
            $scope.configModal = configModal;
            $scope.setState = setState;
            $scope.init = init;
            $scope.checkPermission = checkPermission;
            $scope.wid = $cookies.get('widId');
            $scope.usernamepro = $cookies.get('username');

            function checkPermission() {
                $scope.displayViewAll = true;
                dashboardData.myowner($scope.dashboard.title).then(processmyownerresponse);


            }

            //Get All Projects
            $scope.projID = $cookies.get('ProId');
        $http.get("/api/getProjectsByUser/?username=" + $scope.usernamepro)
            .then(function(response) {
                $scope.getAllProjects = response.data;

                for (var i = 0; i < $scope.getAllProjects.length; i++) {
                    for (var j = 0; j < $scope.getAllProjects[i].usersGroup.length; j++) {
                        for (var k = 0; k < $scope.getAllProjects[i].usersGroup[j].userRoles.length; k++) {
                            $scope.vvv = $scope.getAllProjects[i].usersGroup[j].user;
                            $scope.projectIDS = $scope.getAllProjects[i].id;
                            if (($scope.getAllProjects[i].usersGroup[j].userRoles[k].permissions.indexOf("CONFIGURE_WIDGET") > -1) && ($scope.vvv == $scope.usernamepro) && ($scope.projectIDS == $scope.projID)) {
                                $scope.editProjectflag = true;
                            }
                        }
                    }
                }
            });

            $scope.clid = $cookies.get('mycollector');
            dashboardData.getCollectorItem($scope.clid).then(function(data) {
                $scope.collectorDetails = data;
                $scope.colll = $cookies.get('colId');
            });

            $scope.clIdSnr = $cookies.get('sonarCollectrid');
            dashboardData.getCollectorItemSonar($scope.clIdSnr).then(function(data) {
                $scope.collectorDetailsSonar = data;
                //$scope.colll = $cookies.get('colId');
            });
            if(!$cookies.setCookieSelectedName || $cookies.setCookieSelectedName.length == 0)
                $cookies.setCookieSelectedName = [];

            $scope.check = function(selectedName) {
                //selectedName= JSON.parse(selectedName);
                $scope.dashBoardIds = $routeParams.id;
                $scope.colll = $cookies.get('colId');
                $scope.dashIds = $cookies.get('dashIdToJenkins');
                $scope.wid = $cookies.get('widId');
                $scope.present = 0;
                //$cookies.putObject('cookieSelectedName',[]);
                $cookies.put('selectedName', selectedName);
               
                $scope.postPayload = {
                    name: 'build',
                    options: {
                             },
                    componentId: $scope.colll,
                    collectorItemIds: [selectedName]
                }
                //asd43343434-dashboard name
                //1234asd1234asd-projectname
                //username-was1234
                $http.put("/api/dashboard/" + $scope.dashBoardIds + "/widgetType/build" , ($scope.postPayload)).then(function(response) {

                    refreshJenkins();
                 }) 
                


            }

            $scope.checkSonar = function(selectedNameSonar) {
                //selectedNameSonar=JSON.parse(selectedNameSonar);
                $scope.dashBoardIds = $routeParams.id;
                $scope.wid = $cookies.get('widId');
                $scope.idComp = $cookies.get('compIdSonar');
                $cookies.put('selectedNameSonar', selectedNameSonar);


                $scope.postObjsonar = {
                    name: 'codeanalysis',
                    options: {
                        id: 'codeanalysis0',
                        testJobNames: []
                    },
                    componentId: $scope.idComp,
                    collectorItemIds: [selectedNameSonar]
                };
                //asd43343434-dashboard name
                //1234asd1234asd-projectname
                //username-was1234
               $http.put("/api/dashboard/" + $scope.dashBoardIds + "/widgetType/codeanalysis", ($scope.postObjsonar)).then(function(response) {

                    refreshSonar();
				 }) 
               


            }

            function processmyownerresponse(data) {

                $scope.owner = data;
                if ($scope.owner == $cookies.get('username') || $cookies.get('username') == 'admin') {
                    configModal();
                } else {
                    if ($scope.alerts.length == 0) {
                        $scope.alerts.push({
                            type: 'info',
                            msg: 'You are not authorized'
                        });
                    }
                }
            }

            // method implementations
            function configModal() {
                // load up a modal in the context of the settings defined in
                // the config property when the widget was registered
                var modalConfig = angular.extend({
                    controllerAs: 'ctrl',
                    resolve: {
                        modalData: function() {
                            return {
                                dashboard: $scope.dashboard,
                                widgetConfig: $scope.widgetConfig
                            };
                        }
                    }
                }, $scope.widgetDefinition.config);

                // when the widget closes if an object is passed we'll assume it's an updated
                // widget configuration so try and send it to the api or update the existing one
                $uibModal.open(modalConfig).result.then(upsertWidget);
            }

            function upsertWidget(newWidgetConfig) {
                if (newWidgetConfig) {
                    // use existing values if they're not defined
                    angular.extend($scope.widgetConfig, newWidgetConfig);
                    $cookies.put('widId', $scope.widgetConfig.id);
                    // support single value or array values for collectorItemId
                    if ($scope.widgetConfig.collectorItemId) {
                        $scope.widgetConfig.collectorItemIds = [$scope.widgetConfig.collectorItemId];
                        delete $scope.widgetConfig.collectorItemId;
                    }

                    console.log('New Widget Config', $scope.widgetConfig);
                    dashboardData
                        .upsertWidget($scope.dashboard.id, $scope.widgetConfig)
                        .then(function(response) {

                            // response comes back with two properties, a widget and a component
                            // we need to update the component on the dashboard so that when the
                            // widget loads it will be able to get to the collector data. we
                            // then need to update the widget configuration stored on the container

                            // add or update the widget from the response.
                            // required when a new widget id is created
                            if (response.widget !== null && typeof response.widget == 'object') {
                                angular.extend($scope.widgetConfig, response.widget);
                            }

                            // save the widget locally
                            $scope.container.upsertWidget($scope.widgetConfig);
                            $scope.container.upsertComponent(response.component);

                            // TODO: should probably call back to the widget's getState method
                            $scope.state = WidgetState.READY;

                            init();
                        });
                }
            }

            // redraws the widget which forces it to go through the entire flow
            // TODO: this method causes the screen to flash and should probably just render and replace content
            function init() {
                stopInterval();

                // don't request if widget is not in the read state
                if ($scope.state != WidgetState.READY) {
                    return;
                }

                // grab values from the registered configuration
                var templateUrl = $scope.widgetDefinition.view.templateUrl;
                var controllerName = $scope.widgetDefinition.view.controller;
                var controllerAs = $scope.widgetDefinition.view.controllerAs || 'ctrl';

                // create the widget's controller based on config values
                $scope.widgetViewController = $controller(controllerName + ' as ' + controllerAs, {
                    $scope: $scope
                });

                if (!$scope.widgetViewController.load) {
                    throw new Error(controllerName + ' must define a load method');
                }

                // load the widget with content from the given template url
                $http.get(templateUrl, {
                        cache: $templateCache
                    })
                    .then(function(response) {
                        //TODO: widget implementation should actually start this up after all the data is loaded
                        startInterval();

                        // request the content and add it to the placeholder
                        var $contentEl = angular.element($scope.$widgetEl[0].querySelector('.widget-body-main'));
                        $contentEl.html(response.data);
                        $contentEl.children().data('$ngControllerController', $scope.widgetViewCtrl);
                        $compile($contentEl.contents())($scope);

                        // Ask the widget to update itself
                        refresh();
                    });
            }

            function setState(state) {
                $scope.state = state;
                stopInterval();
            }

            var refreshInterval;

            function startInterval() {
                stopInterval();

                // TODO: make timeout a setting in the widget configuration
                if ($scope.widgetViewController && $scope.widgetViewController.load) {
                    refreshInterval = $interval(refresh, HygieiaConfig.refresh * 1000);
                }
            }

            function stopInterval() {
                $interval.cancel(refreshInterval);
            }

            function refresh() {
                var load = $scope.widgetViewController.load();
                if (load && load.then) {
                    load.then(function(result) {
                        var lastUpdated = angular.isArray(result) ? _.max(result) : result;
                        $scope.lastUpdatedDisplay = moment(lastUpdated).dash('ago');
                    });
                }
            }

            function refreshJenkins() {
                $cookies.get("selectedName");
                var load = $scope.widgetViewController.loadJenkins();
                if (load && load.then) {
                    load.then(function(result) {
                        var lastUpdated = angular.isArray(result) ? _.max(result) : result;
                        $scope.lastUpdatedDisplay = moment(lastUpdated).dash('ago');
                    });
                }
            }

            function refreshSonar() {
                var load = $scope.widgetViewController.loadSonar();
                if (load && load.then) {
                    load.then(function(result) {
                        var lastUpdated = angular.isArray(result) ? _.max(result) : result;
                        $scope.lastUpdatedDisplay = moment(lastUpdated).dash('ago');
                    });
                }
            }
            
            // prevent intervals from continuing to be called when changing pages
            $scope.$on('$routeChangeStart', stopInterval);
        }
    }
})();