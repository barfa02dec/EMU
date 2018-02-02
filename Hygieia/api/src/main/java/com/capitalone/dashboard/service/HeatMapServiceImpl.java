package com.capitalone.dashboard.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.model.HeatMap;
import com.capitalone.dashboard.model.ProjectHeatmapData;
import com.capitalone.dashboard.repository.HeatMapRepository;
import com.capitalone.dashboard.request.HeatMapRequest;

import java.text.ParseException;

@Service
public class HeatMapServiceImpl implements HeatMapService{

	private final HeatMapRepository heatMaprepository;

	@Autowired
	public HeatMapServiceImpl(HeatMapRepository repository) {
		this.heatMaprepository = repository;
	}

	@Override
	public List<HeatMap> getHeatmaps(String projectId) {
		return (List<HeatMap>) heatMaprepository.findByProjectId(projectId);
	}

	@Override
	public HeatMap createProjectHeatmap(HeatMapRequest heatMapRequest){

		mapHeatMapRequestToCreateHeatMap(heatMapRequest);
		return heatMaprepository.save(mapHeatMapRequestToCreateHeatMap(heatMapRequest));
	}

	private HeatMap mapHeatMapRequestToCreateHeatMap(HeatMapRequest re) {

		HeatMap heat = heatMaprepository.findByOneProjectId(re.getProjectId());
		if(null==heat){
			heat = new HeatMap();
			heat.setHeatmapId(re.getHeatmapId());
			heat.setProjectId(re.getProjectId());
			try {
				heat.setSubmissionDate(new SimpleDateFormat("dd-MM-yyyy").parse(re.getSubmissionDate()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		ProjectHeatmapData projectHeatmapData = new ProjectHeatmapData();

		ProjectHeatmapData.CustomerWSR customerWSR = projectHeatmapData.new CustomerWSR();
		customerWSR.setCustomerWSRStatus(re.getCustomerWSRStatus());
		projectHeatmapData.setCustomerWSR(customerWSR);

		ProjectHeatmapData.ArchitectureFocus architectureFocus = projectHeatmapData.new ArchitectureFocus();
		architectureFocus.setArchitectureFocusStatus(re.getArchitectureFocusStatus());
		projectHeatmapData.setArchitectureFocus(architectureFocus);

		ProjectHeatmapData.AutomatedUnitTesting automatedUnitTesting = projectHeatmapData.new AutomatedUnitTesting();
		automatedUnitTesting.setAutomatedUnitTestingStatus(re.getAutomatedUnitTestingStatus());
		automatedUnitTesting.setAutomatedUnitTestingStatusPercentage(re.getAutomatedUnitTestingPercentage());
		projectHeatmapData.setAutomatedUnitTesting(automatedUnitTesting);

		ProjectHeatmapData.CodeCoverage codeCoverage = projectHeatmapData.new CodeCoverage();
		codeCoverage.setCodeCoverageStatus(re.getCodeCoverageStatus());
		codeCoverage.setCodeCoveragePercentage(re.getCodeCoveragePercentage());
		projectHeatmapData.setCodeCoverage(codeCoverage);

		ProjectHeatmapData.ContinuousIntegration continuousIntegration = projectHeatmapData.new ContinuousIntegration();
		continuousIntegration.setContinuousIntegrationStatus(re.getContinuousIntegrationStatus());
		continuousIntegration.setContinuousIntegrationIndex(re.getContinuousIntegrationIndex());
		projectHeatmapData.setContinuousIntegration(continuousIntegration);

		ProjectHeatmapData.DesignFocus designFocus = projectHeatmapData.new DesignFocus();
		designFocus.setDesignFocusStatus(re.getDesignFocusStatus());
		projectHeatmapData.setDesignFocus(designFocus);

		ProjectHeatmapData.DomainKnowledge domainKnowledge = projectHeatmapData.new DomainKnowledge();
		domainKnowledge.setDomainKnowledgeStatus(re.getDomainKnowlwdgeStatus());
		projectHeatmapData.setDomainKnowledge(domainKnowledge);

		ProjectHeatmapData.ManualCodeReview manualCodeReview = projectHeatmapData.new ManualCodeReview();
		manualCodeReview.setManualCodeReviewStatus(re.getManualCodeReviewStatus());
		manualCodeReview.setManualCodeReviewStatusCount(re.getManualCodeReviewCount());
		projectHeatmapData.setManualCodeReview(manualCodeReview);

		ProjectHeatmapData.Metrics metrics = projectHeatmapData.new Metrics();
		metrics.setMetricsStatus(re.getMetricsStatus());
		projectHeatmapData.setMetrics(metrics);

		ProjectHeatmapData.PerformanceAssessment performanceAssessment = projectHeatmapData.new PerformanceAssessment();
		performanceAssessment.setPerformanceAssessmentStatus(re.getPerformanceAssessmentStatus());
		performanceAssessment.setPerformanceAssessmentPercentage(re.getPerformanceAssessmentPercentage());
		projectHeatmapData.setPerformanceAssessment(performanceAssessment);

		ProjectHeatmapData.ProductKnowledge productKnowledge = projectHeatmapData.new ProductKnowledge();
		productKnowledge.setProductKnowledgeIndex(re.getProductKnowledgeIndex());
		projectHeatmapData.setProductKnowledge(productKnowledge);

		ProjectHeatmapData.ReleaseProcess releaseProcess = projectHeatmapData.new ReleaseProcess();
		releaseProcess.setReleaseProcessStatus(re.getReleaseProcessStatus());
		projectHeatmapData.setReleaseProcess(releaseProcess);

		ProjectHeatmapData.Requirements requirements = projectHeatmapData.new Requirements();
		requirements.setRequirementsStatus(re.getRequirementsStatus());
		projectHeatmapData.setRequirements(requirements);

		ProjectHeatmapData.SecurityAssessment securityAssessment = projectHeatmapData.new SecurityAssessment();
		securityAssessment.setSecurityAssessmentStatus(re.getSecurityAssessmentStatus());
		securityAssessment.setSecurityAssessmentIndex(re.getSecurityAssessmentIndex());
		projectHeatmapData.setSecurityAssessment(securityAssessment);

		ProjectHeatmapData.StaticCodeAnalysis staticCodeAnalysis = projectHeatmapData.new StaticCodeAnalysis();
		staticCodeAnalysis.setStaticCodeAnalysisStatus(re.getSecurityAssessmentStatus());
		staticCodeAnalysis.setStaticCodeAnalysisIndex(re.getStaticCodeAnalysisIndex());
		projectHeatmapData.setStaticCodeAnalysis(staticCodeAnalysis);

		ProjectHeatmapData.TeamSize teamSize = projectHeatmapData.new TeamSize();
		teamSize.setTesting(re.getTesting());
		teamSize.setDevelopment(re.getDevelopment());
		projectHeatmapData.setTeamSize(teamSize);

		ProjectHeatmapData.TestingProcess testingProcess = projectHeatmapData.new TestingProcess();
		testingProcess.setTestingProcessStatus(re.getTestingProcessStatus());
		projectHeatmapData.setTestingProcess(testingProcess);

		ProjectHeatmapData.TestAutomation testAutomation = projectHeatmapData.new TestAutomation();
		testAutomation.setTestAutomationStatus(re.getTestAutomationStatus());
		testAutomation.setTestAutomationPercentage(re.getAutomatedUnitTestingPercentage());
		projectHeatmapData.setTestAutomation(testAutomation);

		heat.setProjectHeatmapData(projectHeatmapData);

		return heat;
	}

	@Override
	public HeatMap updateProjectHeatmap(HeatMapRequest re) {

		try{

			HeatMap heat = heatMaprepository.findByOneProjectId(re.getProjectId());

			if(null!=heat)
			{
				mapHeatMapRequestToUpdateHeatMap(re, heat);
				return heatMaprepository.save(heat);
			}

		}catch (Exception e) {
			return null;
		}
		return null;
	}

	private HeatMap mapHeatMapRequestToUpdateHeatMap(HeatMapRequest re, HeatMap heat) {

		heat.setHeatmapId(re.getHeatmapId());
		heat.setProjectId(re.getProjectId());
		try {
			heat.setSubmissionDate(new SimpleDateFormat("dd-MM-yyyy").parse(re.getSubmissionDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		ProjectHeatmapData projectHeatmapData = new ProjectHeatmapData();

		ProjectHeatmapData.CustomerWSR customerWSR = projectHeatmapData.new CustomerWSR();
		customerWSR.setCustomerWSRStatus(re.getCustomerWSRStatus());
		projectHeatmapData.setCustomerWSR(customerWSR);

		ProjectHeatmapData.ArchitectureFocus architectureFocus = projectHeatmapData.new ArchitectureFocus();
		architectureFocus.setArchitectureFocusStatus(re.getArchitectureFocusStatus());
		projectHeatmapData.setArchitectureFocus(architectureFocus);

		ProjectHeatmapData.AutomatedUnitTesting automatedUnitTesting = projectHeatmapData.new AutomatedUnitTesting();
		automatedUnitTesting.setAutomatedUnitTestingStatus(re.getAutomatedUnitTestingStatus());
		automatedUnitTesting.setAutomatedUnitTestingStatusPercentage(re.getAutomatedUnitTestingPercentage());
		projectHeatmapData.setAutomatedUnitTesting(automatedUnitTesting);

		ProjectHeatmapData.CodeCoverage codeCoverage = projectHeatmapData.new CodeCoverage();
		codeCoverage.setCodeCoverageStatus(re.getCodeCoverageStatus());
		codeCoverage.setCodeCoveragePercentage(re.getCodeCoveragePercentage());
		projectHeatmapData.setCodeCoverage(codeCoverage);

		ProjectHeatmapData.ContinuousIntegration continuousIntegration = projectHeatmapData.new ContinuousIntegration();
		continuousIntegration.setContinuousIntegrationStatus(re.getContinuousIntegrationStatus());
		continuousIntegration.setContinuousIntegrationIndex(re.getContinuousIntegrationIndex());
		projectHeatmapData.setContinuousIntegration(continuousIntegration);

		ProjectHeatmapData.DesignFocus designFocus = projectHeatmapData.new DesignFocus();
		designFocus.setDesignFocusStatus(re.getDesignFocusStatus());
		projectHeatmapData.setDesignFocus(designFocus);

		ProjectHeatmapData.DomainKnowledge domainKnowledge = projectHeatmapData.new DomainKnowledge();
		domainKnowledge.setDomainKnowledgeStatus(re.getDomainKnowlwdgeStatus());
		projectHeatmapData.setDomainKnowledge(domainKnowledge);

		ProjectHeatmapData.ManualCodeReview manualCodeReview = projectHeatmapData.new ManualCodeReview();
		manualCodeReview.setManualCodeReviewStatus(re.getManualCodeReviewStatus());
		manualCodeReview.setManualCodeReviewStatusCount(re.getManualCodeReviewCount());
		projectHeatmapData.setManualCodeReview(manualCodeReview);

		ProjectHeatmapData.Metrics metrics = projectHeatmapData.new Metrics();
		metrics.setMetricsStatus(re.getMetricsStatus());
		projectHeatmapData.setMetrics(metrics);

		ProjectHeatmapData.PerformanceAssessment performanceAssessment = projectHeatmapData.new PerformanceAssessment();
		performanceAssessment.setPerformanceAssessmentStatus(re.getPerformanceAssessmentStatus());
		performanceAssessment.setPerformanceAssessmentPercentage(re.getPerformanceAssessmentPercentage());
		projectHeatmapData.setPerformanceAssessment(performanceAssessment);

		ProjectHeatmapData.ProductKnowledge productKnowledge = projectHeatmapData.new ProductKnowledge();
		productKnowledge.setProductKnowledgeIndex(re.getProductKnowledgeIndex());
		projectHeatmapData.setProductKnowledge(productKnowledge);

		ProjectHeatmapData.ReleaseProcess releaseProcess = projectHeatmapData.new ReleaseProcess();
		releaseProcess.setReleaseProcessStatus(re.getReleaseProcessStatus());
		projectHeatmapData.setReleaseProcess(releaseProcess);

		ProjectHeatmapData.Requirements requirements = projectHeatmapData.new Requirements();
		requirements.setRequirementsStatus(re.getRequirementsStatus());
		projectHeatmapData.setRequirements(requirements);

		ProjectHeatmapData.SecurityAssessment securityAssessment = projectHeatmapData.new SecurityAssessment();
		securityAssessment.setSecurityAssessmentStatus(re.getSecurityAssessmentStatus());
		securityAssessment.setSecurityAssessmentIndex(re.getSecurityAssessmentIndex());
		projectHeatmapData.setSecurityAssessment(securityAssessment);

		ProjectHeatmapData.StaticCodeAnalysis staticCodeAnalysis = projectHeatmapData.new StaticCodeAnalysis();
		staticCodeAnalysis.setStaticCodeAnalysisStatus(re.getSecurityAssessmentStatus());
		staticCodeAnalysis.setStaticCodeAnalysisIndex(re.getStaticCodeAnalysisIndex());
		projectHeatmapData.setStaticCodeAnalysis(staticCodeAnalysis);

		ProjectHeatmapData.TeamSize teamSize = projectHeatmapData.new TeamSize();
		teamSize.setTesting(re.getTesting());
		teamSize.setDevelopment(re.getDevelopment());
		projectHeatmapData.setTeamSize(teamSize);

		ProjectHeatmapData.TestingProcess testingProcess = projectHeatmapData.new TestingProcess();
		testingProcess.setTestingProcessStatus(re.getTestingProcessStatus());
		projectHeatmapData.setTestingProcess(testingProcess);

		ProjectHeatmapData.TestAutomation testAutomation = projectHeatmapData.new TestAutomation();
		testAutomation.setTestAutomationStatus(re.getTestAutomationStatus());
		testAutomation.setTestAutomationPercentage(re.getAutomatedUnitTestingPercentage());
		projectHeatmapData.setTestAutomation(testAutomation);

		heat.setProjectHeatmapData(projectHeatmapData);
		return heat;

	}

	@Override
	public void deletePrjectHeatMap(Long heatmapId) {

		HeatMap heatMap = heatMaprepository.findByHeatMapId(heatmapId);

		if(heatMap !=null) {
			heatMaprepository.delete(heatMap);
		}

	}

}
