(function() {
    'use strict';

    angular
        .module(HygieiaConfig.module)
        .controller('detailesviewController', detailesviewController);

    detailesviewController.$inject = ['$scope', 'codeAnalysisData', 'testSuiteData', '$q', '$filter', '$uibModal', '$location', '$routeParams', '$http'];

    function detailesviewController($scope, codeAnalysisData, testSuiteData, $q, $filter, $uibModal, $location, $routeParams, $http) {
        var ctrl = this;
        ctrl.componentId = $routeParams.componentId;
        var apiHost = 'http://localhost:3000';
        var qahost = 'http://10.20.1.183:3000'

        // $http.get(apiHost+"/api/quality/static-analysis?componentId=58f8a165cc5b9d19142f9018&max=1")
        $http.get(apiHost + "/api/quality/static-analysis?componentId=" + ctrl.componentId + "&max=1")
            .then(processCaResponse);

        /*return $q.all([*/
        /*codeAnalysisData.staticDetails(caRequest).then(processCaResponse),*/
        /*codeAnalysisData.securityDetails(saRequest).then(processSaResponse),
        testSuiteData.details(testRequest).then(processTestResponse)*/
        /*]);*/


        function processCaResponse(response) {
            var deferred = $q.defer();
            var data = response.data.result[0].metrics || [];
            var caData = _.isEmpty(response.result) ? {} : response.result[0];

            ctrl.reportUrl = caData.url;
            ctrl.versionNumber = caData.version;

            ctrl.component_list = [];

            ctrl.component1 = {
                name: "componenet1",
                data: []
            };
            ctrl.component2 = {
                name: "componenet2",
                datavul: []
            };
            ctrl.component3 = {
                name: "componenet3",
                datasec: []
            };
            ctrl.component4 = {
                name: "componenet3",
                datadup: []
            }
            ctrl.component5 = {
                name: "componenet3",
                datasize: []
            }
            ctrl.component6 = {
                name: "componenet3",
                datacom: []
            }
            ctrl.component7 = {
                name: "componenet3",
                datadocu: []
            }
            ctrl.component8 = {
                name: "componenet3",
                datacovr: []
            }
            ctrl.component9 = {
                name: "componenet3",
                dataiss: []
            }
            ctrl.component10 = {
                name: "componenet3",
                datatc: []
            }


            angular.forEach(data, function(item) {

                if (item.name == "bugs" || item.name == "reliability_rating" || item.name == "reliability_remediation_effort") {
                    ctrl.component1.data.push(item);


                }
                if (item.name == "vulnerabilities" || item.name == "security_rating" || item.name == "security_remediation_effort") {
                    ctrl.component2.datavul.push(item);
                }
                if (item.name == "code_smells" || item.name == "sqale_rating" || item.name == "sqale_index") {
                    ctrl.component3.datasec.push(item);
                }
                if (item.name == "duplicated_lines" || item.name == "duplicated_lines_density" || item.name == "duplicated_blocks" || item.name == "duplicated_files") {
                    ctrl.component4.datadup.push(item);
                }
                if (item.name == "ncloc" || item.name == "lines" || item.name == "statements" || item.name == "functions" || item.name == "files" || item.name == "directories" || item.name == "classes") {
                    ctrl.component5.datasize.push(item);
                }
                if (item.name == "complexity" || item.name == "function_complexity" || item.name == "file_complexity" || item.name == "class_complexity") {
                    ctrl.component6.datacom.push(item);
                }
                if (item.name == "comment_lines" || item.name == "comment_lines_density" || item.name == "public_documented_api_density" || item.name == "public_undocumented_api" || item.name == "commented_out_code_lines") {
                    ctrl.component7.datadocu.push(item);
                }
                if (item.name == "coverage" || item.name == "line_coverage" || item.name == "branch_coverage" || item.name == "uncovered_lines" || item.name == "uncovered_conditions" || item.name == "lines_to_cover") {
                    ctrl.component8.datacovr.push(item);
                }
                if (item.name == "blocker_violations" || item.name == "critical_violations" || item.name == "major_violations" || item.name == "minor_violations" || item.name == "info_violations") {
                    ctrl.component9.dataiss.push(item);
                }
                if (item.name == "tests" || item.name == "test_execution_time" || item.name == "test_success_density" || item.name == "skipped_tests" || item.name == "test_errors" || item.name == "test_failures") {
                    ctrl.component10.datatc.push(item);
                }


            });
            ctrl.component1.data.sort(function() {
                //var dummyarray = ctrl.component1.data;
                angular.forEach(ctrl.component1.data, function(item) {
                    ctrl.newObj = {
                        name: "sdf",
                        newArr: []
                    };

                    ctrl.first = ctrl.component1.data[0];
                    ctrl.second = ctrl.component1.data[1];
                    ctrl.third = ctrl.component1.data[2];
                    ctrl.newObj.newArr.push(ctrl.first);
                    ctrl.newObj.newArr.push(ctrl.third);
                    ctrl.newObj.newArr.push(ctrl.second);

                    ctrl.securityNewObj = {
                        name: "securityWidget",
                        securityNewArray: []
                    };

                    ctrl.securityprop1 = ctrl.component2.datavul[0];
                    ctrl.securityprop2 = ctrl.component2.datavul[1];
                    ctrl.securityprop3 = ctrl.component2.datavul[2];
                    ctrl.securityNewObj.securityNewArray.push(ctrl.securityprop3);
                    ctrl.securityNewObj.securityNewArray.push(ctrl.securityprop2);
                    ctrl.securityNewObj.securityNewArray.push(ctrl.securityprop1);

                    ctrl.maintainabilityNewObj = {
                        name: "maintainabilityWidget",
                        maintainabilityNewArray: []
                    };

                    ctrl.maintainabilityprop1 = ctrl.component3.datasec[0];
                    ctrl.maintainabilityprop2 = ctrl.component3.datasec[1];
                    ctrl.maintainabilityprop3 = ctrl.component3.datasec[2];

                    ctrl.maintainabilityNewObj.maintainabilityNewArray.push(ctrl.maintainabilityprop3);
                    ctrl.maintainabilityNewObj.maintainabilityNewArray.push(ctrl.maintainabilityprop1);
                    ctrl.maintainabilityNewObj.maintainabilityNewArray.push(ctrl.maintainabilityprop2);

                    ctrl.duplicationsNewObj = {
                        name: "duplicationWidget",
                        duplicationsNewArray: []
                    };

                    ctrl.duplicationsprop1 = ctrl.component4.datadup[0];
                    ctrl.duplicationsprop2 = ctrl.component4.datadup[1];
                    ctrl.duplicationsprop3 = ctrl.component4.datadup[2];
                    ctrl.duplicationsprop4 = ctrl.component4.datadup[3];
                    ctrl.duplicationsNewObj.duplicationsNewArray.push(ctrl.duplicationsprop3);
                    ctrl.duplicationsNewObj.duplicationsNewArray.push(ctrl.duplicationsprop4);
                    ctrl.duplicationsNewObj.duplicationsNewArray.push(ctrl.duplicationsprop2);
                    ctrl.duplicationsNewObj.duplicationsNewArray.push(ctrl.duplicationsprop1);


                    ctrl.complexityNewObj = {
                        name: "complexityWidet",
                        complexityNewArray: []
                    };

                    ctrl.complexityprop1 = ctrl.component6.datacom[0];
                    ctrl.complexityprop2 = ctrl.component6.datacom[1];
                    ctrl.complexityprop3 = ctrl.component6.datacom[2];
                    ctrl.complexityprop4 = ctrl.component6.datacom[3];
                    ctrl.complexityNewObj.complexityNewArray.push(ctrl.complexityprop1);
                    ctrl.complexityNewObj.complexityNewArray.push(ctrl.complexityprop2);
                    ctrl.complexityNewObj.complexityNewArray.push(ctrl.complexityprop4);
                    ctrl.complexityNewObj.complexityNewArray.push(ctrl.complexityprop3);

                    ctrl.docuNewObj = {
                        name: "documentationWidget",
                        docuNewArray: []
                    };

                    ctrl.docuprop1 = ctrl.component7.datadocu[0];
                    ctrl.docuprop2 = ctrl.component7.datadocu[1];
                    ctrl.docuprop3 = ctrl.component7.datadocu[2];
                    ctrl.docuprop4 = ctrl.component7.datadocu[3];
                    ctrl.docuNewObj.docuNewArray.push(ctrl.docuprop1);
                    ctrl.docuNewObj.docuNewArray.push(ctrl.docuprop4);
                    ctrl.docuNewObj.docuNewArray.push(ctrl.docuprop2);
                    ctrl.docuNewObj.docuNewArray.push(ctrl.docuprop3);

                    ctrl.coverageNewObj = {
                        name: "coverageWidget",
                        coverageNewArray: []
                    };

                    ctrl.coverageprop1 = ctrl.component8.datacovr[0];
                    ctrl.coverageprop2 = ctrl.component8.datacovr[1];
                    ctrl.coverageprop3 = ctrl.component8.datacovr[2];
                    ctrl.coverageprop4 = ctrl.component8.datacovr[3];
                    ctrl.coverageprop5 = ctrl.component8.datacovr[4];
                    ctrl.coverageprop6 = ctrl.component8.datacovr[5];
                    ctrl.coverageNewObj.coverageNewArray.push(ctrl.coverageprop6);
                    ctrl.coverageNewObj.coverageNewArray.push(ctrl.coverageprop1);
                    ctrl.coverageNewObj.coverageNewArray.push(ctrl.coverageprop2);
                    ctrl.coverageNewObj.coverageNewArray.push(ctrl.coverageprop3);
                    ctrl.coverageNewObj.coverageNewArray.push(ctrl.coverageprop4);
                    ctrl.coverageNewObj.coverageNewArray.push(ctrl.coverageprop5);

                    ctrl.testcoverageNewObj = {
                        name: "testCoverageWidget",
                        testcoverageNewArray: []
                    };

                    ctrl.testcoverageprop1 = ctrl.component10.datatc[0];
                    ctrl.testcoverageprop2 = ctrl.component10.datatc[1];
                    ctrl.testcoverageprop3 = ctrl.component10.datatc[2];
                    ctrl.testcoverageprop4 = ctrl.component10.datatc[3];
                    ctrl.testcoverageprop5 = ctrl.component10.datatc[4];
                    ctrl.testcoverageprop6 = ctrl.component10.datatc[5];
                    ctrl.testcoverageNewObj.testcoverageNewArray.push(ctrl.testcoverageprop1);
                    ctrl.testcoverageNewObj.testcoverageNewArray.push(ctrl.testcoverageprop2);
                    ctrl.testcoverageNewObj.testcoverageNewArray.push(ctrl.testcoverageprop3);
                    ctrl.testcoverageNewObj.testcoverageNewArray.push(ctrl.testcoverageprop4);
                    ctrl.testcoverageNewObj.testcoverageNewArray.push(ctrl.testcoverageprop5);
                    ctrl.testcoverageNewObj.testcoverageNewArray.push(ctrl.testcoverageprop6);

                    ctrl.issueNewObj = {
                        name: "issueWidget",
                        issueNewArray: []
                    };

                    ctrl.issuecoverageprop1 = ctrl.component9.dataiss[0];
                    ctrl.issuecoverageprop2 = ctrl.component9.dataiss[1];
                    ctrl.issuecoverageprop3 = ctrl.component9.dataiss[2];
                    ctrl.issuecoverageprop4 = ctrl.component9.dataiss[3];
                    ctrl.issuecoverageprop5 = ctrl.component9.dataiss[4];
                    ctrl.issuecoverageprop6 = ctrl.component9.dataiss[5];
                    ctrl.issueNewObj.issueNewArray.push(ctrl.issuecoverageprop1);
                    ctrl.issueNewObj.issueNewArray.push(ctrl.issuecoverageprop2);
                    ctrl.issueNewObj.issueNewArray.push(ctrl.issuecoverageprop3);
                    ctrl.issueNewObj.issueNewArray.push(ctrl.issuecoverageprop4);
                    ctrl.issueNewObj.issueNewArray.push(ctrl.issuecoverageprop5);
                    ctrl.issueNewObj.issueNewArray.push(ctrl.issuecoverageprop6);

                    ctrl.sizeNewObj = {
                        name: "sizeWidget",
                        sizeNewArray: []
                    };

                    ctrl.sizecoverageprop1 = ctrl.component5.datasize[0];
                    ctrl.sizecoverageprop2 = ctrl.component5.datasize[1];
                    ctrl.sizecoverageprop3 = ctrl.component5.datasize[2];
                    ctrl.sizecoverageprop4 = ctrl.component5.datasize[3];
                    ctrl.sizecoverageprop5 = ctrl.component5.datasize[4];
                    ctrl.sizecoverageprop6 = ctrl.component5.datasize[5];
                    ctrl.sizecoverageprop7 = ctrl.component5.datasize[6];
                    ctrl.sizeNewObj.sizeNewArray.push(ctrl.sizecoverageprop1);
                    ctrl.sizeNewObj.sizeNewArray.push(ctrl.sizecoverageprop2);
                    ctrl.sizeNewObj.sizeNewArray.push(ctrl.sizecoverageprop3);
                    ctrl.sizeNewObj.sizeNewArray.push(ctrl.sizecoverageprop4);
                    ctrl.sizeNewObj.sizeNewArray.push(ctrl.sizecoverageprop5);
                    ctrl.sizeNewObj.sizeNewArray.push(ctrl.sizecoverageprop6);
                    ctrl.sizeNewObj.sizeNewArray.push(ctrl.sizecoverageprop7);



                });
            });
            ctrl.component_list.push(ctrl.newObj);
            ctrl.component_list.push(ctrl.securityNewObj);
            ctrl.component_list.push(ctrl.maintainabilityNewObj);
            ctrl.component_list.push(ctrl.duplicationsNewObj);
            ctrl.component_list.push(ctrl.complexityNewObj);
            ctrl.component_list.push(ctrl.sizeNewObj);
            ctrl.component_list.push(ctrl.docuNewObj);
            ctrl.component_list.push(ctrl.coverageNewObj);
            ctrl.component_list.push(ctrl.issueNewObj);
            ctrl.component_list.push(ctrl.testcoverageNewObj);
            console.log(ctrl.component_list);
            console.log(ctrl.component3.datasec);
            console.log(ctrl.newObj);




        }



    }
})();