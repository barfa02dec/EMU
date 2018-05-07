package com.capitalone.dashboard.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.model.HeatMap;
import com.capitalone.dashboard.model.ProjectHeatmapData;
import com.capitalone.dashboard.repository.HeatMapRepository;
import com.capitalone.dashboard.request.HeatMapRequest;

@Service
public class HeatMapServiceImpl implements HeatMapService{

	private static final Logger LOGGER = LoggerFactory.getLogger(HeatMapServiceImpl.class);

	private final HeatMapRepository heatMaprepository;

	@Autowired
	public HeatMapServiceImpl(HeatMapRepository repository) {
		this.heatMaprepository = repository;
	}

	@Override
	public List<HeatMap> getHeatmaps(String projectId) {
		LOGGER.info("Show All the heat maps for ", projectId);
		List<HeatMap> heatMaps = heatMaprepository.getByProjectId(projectId);
		return heatMaps;
	}

	@Override
	public HeatMap createHeatmap(HeatMapRequest heatMapRequest){
		LOGGER.debug("Adding heatmap");
		return heatMaprepository.save(convertHeatmapRequestToHeatmapModel(heatMapRequest));
	}

	private HeatMap convertHeatmapRequestToHeatmapModel(HeatMapRequest hre) {

		LOGGER.debug("Find heatmap for" , hre.getProjectId());

		HeatMap heat = heatMaprepository.findByOneProjectId(hre.getProjectId(),
				hre.getSubmissionDate());
		
		if (null == heat) {
			heat = new HeatMap();
			heat.setProjectId(hre.getProjectId());
			heat.setSubmissionDate(hre.getSubmissionDate());
		}
		
		ProjectHeatmapData projectHeatmapData = new ProjectHeatmapData();

		convertHeatmapRequest(projectHeatmapData, hre);

		heat.setProjectHeatmapData(projectHeatmapData);
		return heat;
	}

	@Override
	public HeatMap updateHeatmap(HeatMapRequest hre) {

		LOGGER.info("Update heatmap for ", hre.getProjectId());

		HeatMap heat = heatMaprepository.findOne(new ObjectId(hre.getObjectId()));

		if(heat != null) {
			convertHeatmapRequestToHeatMapModel(hre, heat);
			return heatMaprepository.save(heat);
		}else{
			return null;
		}
			
	}

	private HeatMap convertHeatmapRequestToHeatMapModel(HeatMapRequest hre,
			HeatMap heat) {

		heat.setProjectId(hre.getProjectId());
		heat.setSubmissionDate(hre.getSubmissionDate());
		ProjectHeatmapData projectHeatmapData = new ProjectHeatmapData();

		convertHeatmapRequest(projectHeatmapData, hre);

		heat.setProjectHeatmapData(projectHeatmapData);
		return heat;
	}

	private void convertHeatmapRequest(ProjectHeatmapData projectHeatmapData, HeatMapRequest hre) {
		
		ProjectHeatmapData.CustomerWSR customerWSR = projectHeatmapData.new CustomerWSR();
		customerWSR.setCustomerWSRStatus(hre.getCustomerWSRStatus());
		projectHeatmapData.setCustomerWSR(customerWSR);

		ProjectHeatmapData.ArchitectureFocus architectureFocus = projectHeatmapData.new ArchitectureFocus();
		architectureFocus.setArchitectureFocusStatus(hre.getArchitectureFocusStatus());
		projectHeatmapData.setArchitectureFocus(architectureFocus);

		ProjectHeatmapData.AutomatedUnitTesting automatedUnitTesting = projectHeatmapData.new AutomatedUnitTesting();
		automatedUnitTesting.setAutomatedUnitTestingStatus(hre.getAutomatedUnitTestingStatus());
		automatedUnitTesting.setAutomatedUnitTestingStatusPercentage(hre.getAutomatedUnitTestingPercentage());
		projectHeatmapData.setAutomatedUnitTesting(automatedUnitTesting);

		ProjectHeatmapData.CodeCoverage codeCoverage = projectHeatmapData.new CodeCoverage();
		codeCoverage.setCodeCoverageStatus(hre.getCodeCoverageStatus());
		codeCoverage.setCodeCoveragePercentage(hre.getCodeCoveragePercentage());
		projectHeatmapData.setCodeCoverage(codeCoverage);

		ProjectHeatmapData.ContinuousIntegration continuousIntegration = projectHeatmapData.new ContinuousIntegration();
		continuousIntegration.setContinuousIntegrationStatus(hre.getContinuousIntegrationStatus());
		continuousIntegration.setContinuousIntegrationIndex(hre.getContinuousIntegrationIndex());
		projectHeatmapData.setContinuousIntegration(continuousIntegration);

		ProjectHeatmapData.DesignFocus designFocus = projectHeatmapData.new DesignFocus();
		designFocus.setDesignFocusStatus(hre.getDesignFocusStatus());
		projectHeatmapData.setDesignFocus(designFocus);

		ProjectHeatmapData.DomainKnowledge domainKnowledge = projectHeatmapData.new DomainKnowledge();
		domainKnowledge.setDomainKnowledgeStatus(hre.getDomainKnowlwdgeStatus());
		projectHeatmapData.setDomainKnowledge(domainKnowledge);

		ProjectHeatmapData.ManualCodeReview manualCodeReview = projectHeatmapData.new ManualCodeReview();
		manualCodeReview.setManualCodeReviewStatus(hre.getManualCodeReviewStatus());
		manualCodeReview.setManualCodeReviewStatusCount(hre.getManualCodeReviewCount());
		projectHeatmapData.setManualCodeReview(manualCodeReview);

		ProjectHeatmapData.Metrics metrics = projectHeatmapData.new Metrics();
		metrics.setMetricsStatus(hre.getMetricsStatus());
		projectHeatmapData.setMetrics(metrics);

		ProjectHeatmapData.PerformanceAssessment performanceAssessment = projectHeatmapData.new PerformanceAssessment();
		performanceAssessment.setPerformanceAssessmentStatus(hre.getPerformanceAssessmentStatus());
		performanceAssessment.setPerformanceAssessmentPercentage(hre.getPerformanceAssessmentPercentage());
		projectHeatmapData.setPerformanceAssessment(performanceAssessment);

		ProjectHeatmapData.ProductKnowledge productKnowledge = projectHeatmapData.new ProductKnowledge();
		productKnowledge.setProductKnowledgeIndex(hre.getProductKnowledgeIndex());
		projectHeatmapData.setProductKnowledge(productKnowledge);

		ProjectHeatmapData.ReleaseProcess releaseProcess = projectHeatmapData.new ReleaseProcess();
		releaseProcess.setReleaseProcessStatus(hre.getReleaseProcessStatus());
		projectHeatmapData.setReleaseProcess(releaseProcess);

		ProjectHeatmapData.Requirements requirements = projectHeatmapData.new Requirements();
		requirements.setRequirementsStatus(hre.getRequirementsStatus());
		projectHeatmapData.setRequirements(requirements);

		ProjectHeatmapData.SecurityAssessment securityAssessment = projectHeatmapData.new SecurityAssessment();
		securityAssessment.setSecurityAssessmentStatus(hre.getSecurityAssessmentStatus());
		securityAssessment.setSecurityAssessmentIndex(hre.getSecurityAssessmentIndex());
		projectHeatmapData.setSecurityAssessment(securityAssessment);

		ProjectHeatmapData.StaticCodeAnalysis staticCodeAnalysis = projectHeatmapData.new StaticCodeAnalysis();
		staticCodeAnalysis.setStaticCodeAnalysisStatus(hre.getSecurityAssessmentStatus());
		staticCodeAnalysis.setStaticCodeAnalysisIndex(hre.getStaticCodeAnalysisIndex());
		projectHeatmapData.setStaticCodeAnalysis(staticCodeAnalysis);

		ProjectHeatmapData.TeamSize teamSize = projectHeatmapData.new TeamSize();
		teamSize.setTesting(hre.getTesting());
		teamSize.setDevelopment(hre.getDevelopment());
		projectHeatmapData.setTeamSize(teamSize);

		ProjectHeatmapData.TestingProcess testingProcess = projectHeatmapData.new TestingProcess();
		testingProcess.setTestingProcessStatus(hre.getTestingProcessStatus());
		projectHeatmapData.setTestingProcess(testingProcess);

		ProjectHeatmapData.TestAutomation testAutomation = projectHeatmapData.new TestAutomation();
		testAutomation.setTestAutomationStatus(hre.getTestAutomationStatus());
		testAutomation.setTestAutomationPercentage(hre.getTestAutomationPercentage());
		projectHeatmapData.setTestAutomation(testAutomation);

	}

	@Override
	public void deleteHeatmap(String projectId) {
		LOGGER.info("Delate Heatmap for" , projectId);
		HeatMap heatMap = (HeatMap) heatMaprepository.getByProjectId(projectId);

		if(heatMap !=null) {
			heatMaprepository.delete(heatMap);
		}
		LOGGER.info("Heatmap deleted for", projectId);
	}

}
