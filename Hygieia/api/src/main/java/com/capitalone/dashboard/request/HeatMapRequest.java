package com.capitalone.dashboard.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class HeatMapRequest {

	@NotNull
    @Size(min=3,max=40, message="heatmapId should be min=3,max=40 characters")
	private Long heatmapId; 
	
	@NotNull
    @Size(min=3,max=40, message="heatmapId should be min=3,max=40 characters")
	private String projectId;
	
	@NotNull
    @Size(min=3,max=40, message="heatmapId should be min=3,max=40 characters")
	private String submissionDate;

	private String customerWSRStatus;
	
	private String architectureFocusStatus;
	
	private String automatedUnitTestingStatus;
	
	private String automatedUnitTestingPercentage;
	
	private String codeCoverageStatus;
	
	private String codeCoveragePercentage;
	
	private String continuousIntegrationStatus;
	
	private String continuousIntegrationIndex;
	
	private String domainKnowlwdgeStatus;
	
	private String manualCodeReviewStatus;
	
	private String manualCodeReviewCount;
	
	private String metricsStatus;
	
	private String performanceAssessmentStatus;
	
	private String performanceAssessmentPercentage;
	
	private String productKnowledgeIndex;
	
	private String releaseProcessStatus;
	
	private String requirementsStatus;
	
	private String securityAssessmentStatus;
	
	private String securityAssessmentIndex;
	
	private String staticCodeAnalysisStatus;
	
	private String staticCodeAnalysisIndex;
	
	private String teamSizeTesting;
	
	private String testingProcessStatus;
	
	private String testAutomationStatus;
	
	private String testAutomationPercentage;
	
	private String designFocusStatus;

	private String count;
	
	private String testing;
	
	private String development;

	/**
	 * @return the heatmapId
	 */
	public Long getHeatmapId() {
		return heatmapId;
	}

	/**
	 * @param heatmapId the heatmapId to set
	 */
	public void setHeatmapId(Long heatmapId) {
		this.heatmapId = heatmapId;
	}

	/**
	 * @return the projectId
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the submissionDate
	 */
	public String getSubmissionDate() {
		return submissionDate;
	}

	/**
	 * @param submissionDate the submissionDate to set
	 */
	public void setSubmissionDate(String submissionDate) {
		this.submissionDate = submissionDate;
	}

	/**
	 * @return the customerWSRStatus
	 */
	public String getCustomerWSRStatus() {
		return customerWSRStatus;
	}

	/**
	 * @param customerWSRStatus the customerWSRStatus to set
	 */
	public void setCustomerWSRStatus(String customerWSRStatus) {
		this.customerWSRStatus = customerWSRStatus;
	}

	/**
	 * @return the architectureFocusStatus
	 */
	public String getArchitectureFocusStatus() {
		return architectureFocusStatus;
	}

	/**
	 * @param architectureFocusStatus the architectureFocusStatus to set
	 */
	public void setArchitectureFocusStatus(String architectureFocusStatus) {
		this.architectureFocusStatus = architectureFocusStatus;
	}

	/**
	 * @return the automatedUnitTestingStatus
	 */
	public String getAutomatedUnitTestingStatus() {
		return automatedUnitTestingStatus;
	}

	/**
	 * @param automatedUnitTestingStatus the automatedUnitTestingStatus to set
	 */
	public void setAutomatedUnitTestingStatus(String automatedUnitTestingStatus) {
		this.automatedUnitTestingStatus = automatedUnitTestingStatus;
	}

	/**
	 * @return the automatedUnitTestingPercentage
	 */
	public String getAutomatedUnitTestingPercentage() {
		return automatedUnitTestingPercentage;
	}

	/**
	 * @param automatedUnitTestingPercentage the automatedUnitTestingPercentage to set
	 */
	public void setAutomatedUnitTestingPercentage(
			String automatedUnitTestingPercentage) {
		this.automatedUnitTestingPercentage = automatedUnitTestingPercentage;
	}

	/**
	 * @return the codeCoverageStatus
	 */
	public String getCodeCoverageStatus() {
		return codeCoverageStatus;
	}

	/**
	 * @param codeCoverageStatus the codeCoverageStatus to set
	 */
	public void setCodeCoverageStatus(String codeCoverageStatus) {
		this.codeCoverageStatus = codeCoverageStatus;
	}

	/**
	 * @return the codeCoveragePercentage
	 */
	public String getCodeCoveragePercentage() {
		return codeCoveragePercentage;
	}

	/**
	 * @param codeCoveragePercentage the codeCoveragePercentage to set
	 */
	public void setCodeCoveragePercentage(String codeCoveragePercentage) {
		this.codeCoveragePercentage = codeCoveragePercentage;
	}

	/**
	 * @return the continuousIntegrationStatus
	 */
	public String getContinuousIntegrationStatus() {
		return continuousIntegrationStatus;
	}

	/**
	 * @param continuousIntegrationStatus the continuousIntegrationStatus to set
	 */
	public void setContinuousIntegrationStatus(String continuousIntegrationStatus) {
		this.continuousIntegrationStatus = continuousIntegrationStatus;
	}

	/**
	 * @return the continuousIntegrationIndex
	 */
	public String getContinuousIntegrationIndex() {
		return continuousIntegrationIndex;
	}

	/**
	 * @param continuousIntegrationIndex the continuousIntegrationIndex to set
	 */
	public void setContinuousIntegrationIndex(String continuousIntegrationIndex) {
		this.continuousIntegrationIndex = continuousIntegrationIndex;
	}

	/**
	 * @return the domainKnowlwdgeStatus
	 */
	public String getDomainKnowlwdgeStatus() {
		return domainKnowlwdgeStatus;
	}

	/**
	 * @param domainKnowlwdgeStatus the domainKnowlwdgeStatus to set
	 */
	public void setDomainKnowlwdgeStatus(String domainKnowlwdgeStatus) {
		this.domainKnowlwdgeStatus = domainKnowlwdgeStatus;
	}

	/**
	 * @return the manualCodeReviewStatus
	 */
	public String getManualCodeReviewStatus() {
		return manualCodeReviewStatus;
	}

	/**
	 * @param manualCodeReviewStatus the manualCodeReviewStatus to set
	 */
	public void setManualCodeReviewStatus(String manualCodeReviewStatus) {
		this.manualCodeReviewStatus = manualCodeReviewStatus;
	}

	/**
	 * @return the manualCodeReviewCount
	 */
	public String getManualCodeReviewCount() {
		return manualCodeReviewCount;
	}

	/**
	 * @param manualCodeReviewCount the manualCodeReviewCount to set
	 */
	public void setManualCodeReviewCount(String manualCodeReviewCount) {
		this.manualCodeReviewCount = manualCodeReviewCount;
	}

	/**
	 * @return the metricsStatus
	 */
	public String getMetricsStatus() {
		return metricsStatus;
	}

	/**
	 * @param metricsStatus the metricsStatus to set
	 */
	public void setMetricsStatus(String metricsStatus) {
		this.metricsStatus = metricsStatus;
	}

	/**
	 * @return the performanceAssessmentStatus
	 */
	public String getPerformanceAssessmentStatus() {
		return performanceAssessmentStatus;
	}

	/**
	 * @param performanceAssessmentStatus the performanceAssessmentStatus to set
	 */
	public void setPerformanceAssessmentStatus(String performanceAssessmentStatus) {
		this.performanceAssessmentStatus = performanceAssessmentStatus;
	}

	/**
	 * @return the performanceAssessmentPercentage
	 */
	public String getPerformanceAssessmentPercentage() {
		return performanceAssessmentPercentage;
	}

	/**
	 * @param performanceAssessmentPercentage the performanceAssessmentPercentage to set
	 */
	public void setPerformanceAssessmentPercentage(
			String performanceAssessmentPercentage) {
		this.performanceAssessmentPercentage = performanceAssessmentPercentage;
	}

	/**
	 * @return the productKnowledgeIndex
	 */
	public String getProductKnowledgeIndex() {
		return productKnowledgeIndex;
	}

	/**
	 * @param productKnowledgeIndex the productKnowledgeIndex to set
	 */
	public void setProductKnowledgeIndex(String productKnowledgeIndex) {
		this.productKnowledgeIndex = productKnowledgeIndex;
	}

	/**
	 * @return the releaseProcessStatus
	 */
	public String getReleaseProcessStatus() {
		return releaseProcessStatus;
	}

	/**
	 * @param releaseProcessStatus the releaseProcessStatus to set
	 */
	public void setReleaseProcessStatus(String releaseProcessStatus) {
		this.releaseProcessStatus = releaseProcessStatus;
	}

	/**
	 * @return the requirementsStatus
	 */
	public String getRequirementsStatus() {
		return requirementsStatus;
	}

	/**
	 * @param requirementsStatus the requirementsStatus to set
	 */
	public void setRequirementsStatus(String requirementsStatus) {
		this.requirementsStatus = requirementsStatus;
	}

	/**
	 * @return the securityAssessmentStatus
	 */
	public String getSecurityAssessmentStatus() {
		return securityAssessmentStatus;
	}

	/**
	 * @param securityAssessmentStatus the securityAssessmentStatus to set
	 */
	public void setSecurityAssessmentStatus(String securityAssessmentStatus) {
		this.securityAssessmentStatus = securityAssessmentStatus;
	}

	/**
	 * @return the securityAssessmentIndex
	 */
	public String getSecurityAssessmentIndex() {
		return securityAssessmentIndex;
	}

	/**
	 * @param securityAssessmentIndex the securityAssessmentIndex to set
	 */
	public void setSecurityAssessmentIndex(String securityAssessmentIndex) {
		this.securityAssessmentIndex = securityAssessmentIndex;
	}

	/**
	 * @return the staticCodeAnalysisStatus
	 */
	public String getStaticCodeAnalysisStatus() {
		return staticCodeAnalysisStatus;
	}

	/**
	 * @param staticCodeAnalysisStatus the staticCodeAnalysisStatus to set
	 */
	public void setStaticCodeAnalysisStatus(String staticCodeAnalysisStatus) {
		this.staticCodeAnalysisStatus = staticCodeAnalysisStatus;
	}

	/**
	 * @return the staticCodeAnalysisIndex
	 */
	public String getStaticCodeAnalysisIndex() {
		return staticCodeAnalysisIndex;
	}

	/**
	 * @param staticCodeAnalysisIndex the staticCodeAnalysisIndex to set
	 */
	public void setStaticCodeAnalysisIndex(String staticCodeAnalysisIndex) {
		this.staticCodeAnalysisIndex = staticCodeAnalysisIndex;
	}

	/**
	 * @return the teamSizeTesting
	 */
	public String getTeamSizeTesting() {
		return teamSizeTesting;
	}

	/**
	 * @param teamSizeTesting the teamSizeTesting to set
	 */
	public void setTeamSizeTesting(String teamSizeTesting) {
		this.teamSizeTesting = teamSizeTesting;
	}

	/**
	 * @return the testingProcessStatus
	 */
	public String getTestingProcessStatus() {
		return testingProcessStatus;
	}

	/**
	 * @param testingProcessStatus the testingProcessStatus to set
	 */
	public void setTestingProcessStatus(String testingProcessStatus) {
		this.testingProcessStatus = testingProcessStatus;
	}

	/**
	 * @return the testAutomationStatus
	 */
	public String getTestAutomationStatus() {
		return testAutomationStatus;
	}

	/**
	 * @param testAutomationStatus the testAutomationStatus to set
	 */
	public void setTestAutomationStatus(String testAutomationStatus) {
		this.testAutomationStatus = testAutomationStatus;
	}

	/**
	 * @return the testAutomationPercentage
	 */
	public String getTestAutomationPercentage() {
		return testAutomationPercentage;
	}

	/**
	 * @param testAutomationPercentage the testAutomationPercentage to set
	 */
	public void setTestAutomationPercentage(String testAutomationPercentage) {
		this.testAutomationPercentage = testAutomationPercentage;
	}

	/**
	 * @return the designFocusStatus
	 */
	public String getDesignFocusStatus() {
		return designFocusStatus;
	}

	/**
	 * @param designFocusStatus the designFocusStatus to set
	 */
	public void setDesignFocusStatus(String designFocusStatus) {
		this.designFocusStatus = designFocusStatus;
	}

	/**
	 * @return the count
	 */
	public String getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(String count) {
		this.count = count;
	}

	/**
	 * @return the testing
	 */
	public String getTesting() {
		return testing;
	}

	/**
	 * @param testing the testing to set
	 */
	public void setTesting(String testing) {
		this.testing = testing;
	}

	/**
	 * @return the development
	 */
	public String getDevelopment() {
		return development;
	}

	/**
	 * @param development the development to set
	 */
	public void setDevelopment(String development) {
		this.development = development;
	}
	
	

}