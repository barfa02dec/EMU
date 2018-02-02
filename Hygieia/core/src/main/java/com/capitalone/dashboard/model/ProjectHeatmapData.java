package com.capitalone.dashboard.model;


public class ProjectHeatmapData {

	private ProductKnowledge productKnowledge;

	private DomainKnowledge domainKnowledge;

	private CodeCoverage codeCoverage;

	private TestAutomation testAutomation;

	private ManualCodeReview manualCodeReview;

	private StaticCodeAnalysis staticCodeAnalysis;

	private PerformanceAssessment performanceAssessment;

	private Requirements requirements;

	private CustomerWSR customerWSR;

	private AutomatedUnitTesting automatedUnitTesting;

	private TeamSize teamSize;

	private ArchitectureFocus architectureFocus;

	private SecurityAssessment securityAssessment;

	private Metrics metrics;

	private ContinuousIntegration continuousIntegration;

	private DesignFocus designFocus;

	private TestingProcess testingProcess;

	private ReleaseProcess releaseProcess;

	public ProductKnowledge getProductKnowledge ()
	{
		return productKnowledge;
	}

	public void setProductKnowledge (ProductKnowledge productKnowledge)
	{
		this.productKnowledge = productKnowledge;
	}

	public DomainKnowledge getDomainKnowledge ()
	{
		return domainKnowledge;
	}

	public void setDomainKnowledge (DomainKnowledge domainKnowledge)
	{
		this.domainKnowledge = domainKnowledge;
	}

	public CodeCoverage getCodeCoverage ()
	{
		return codeCoverage;
	}

	public void setCodeCoverage (CodeCoverage codeCoverage)
	{
		this.codeCoverage = codeCoverage;
	}

	public TestAutomation getTestAutomation ()
	{
		return testAutomation;
	}

	public void setTestAutomation (TestAutomation testAutomation)
	{
		this.testAutomation = testAutomation;
	}

	public ManualCodeReview getManualCodeReview ()
	{
		return manualCodeReview;
	}

	public void setManualCodeReview (ManualCodeReview manualCodeReview)
	{
		this.manualCodeReview = manualCodeReview;
	}

	public StaticCodeAnalysis getStaticCodeAnalysis ()
	{
		return staticCodeAnalysis;
	}

	public void setStaticCodeAnalysis (StaticCodeAnalysis staticCodeAnalysis)
	{
		this.staticCodeAnalysis = staticCodeAnalysis;
	}

	public PerformanceAssessment getPerformanceAssessment ()
	{
		return performanceAssessment;
	}

	public void setPerformanceAssessment (PerformanceAssessment performanceAssessment)
	{
		this.performanceAssessment = performanceAssessment;
	}

	public Requirements getRequirements ()
	{
		return requirements;
	}

	public void setRequirements (Requirements requirements)
	{
		this.requirements = requirements;
	}

	public CustomerWSR getCustomerWSR ()
	{
		return customerWSR;
	}

	public void setCustomerWSR (CustomerWSR customerWSR)
	{
		this.customerWSR = customerWSR;
	}

	public AutomatedUnitTesting getAutomatedUnitTesting ()
	{
		return automatedUnitTesting;
	}

	public void setAutomatedUnitTesting (AutomatedUnitTesting automatedUnitTesting)
	{
		this.automatedUnitTesting = automatedUnitTesting;
	}

	public TeamSize getTeamSize ()
	{
		return teamSize;
	}

	public void setTeamSize (TeamSize teamSize)
	{
		this.teamSize = teamSize;
	}

	public ArchitectureFocus getArchitectureFocus ()
	{
		return architectureFocus;
	}

	public void setArchitectureFocus (ArchitectureFocus architectureFocus)
	{
		this.architectureFocus = architectureFocus;
	}

	public SecurityAssessment getSecurityAssessment ()
	{
		return securityAssessment;
	}

	public void setSecurityAssessment (SecurityAssessment securityAssessment)
	{
		this.securityAssessment = securityAssessment;
	}

	public Metrics getMetrics ()
	{
		return metrics;
	}

	public void setMetrics (Metrics metrics)
	{
		this.metrics = metrics;
	}

	public ContinuousIntegration getContinuousIntegration ()
	{
		return continuousIntegration;
	}

	public void setContinuousIntegration (ContinuousIntegration continuousIntegration)
	{
		this.continuousIntegration = continuousIntegration;
	}

	public DesignFocus getDesignFocus ()
	{
		return designFocus;
	}

	public void setDesignFocus (DesignFocus designFocus)
	{
		this.designFocus = designFocus;
	}

	public TestingProcess getTestingProcess ()
	{
		return testingProcess;
	}

	public void setTestingProcess (TestingProcess testingProcess)
	{
		this.testingProcess = testingProcess;
	}

	public ReleaseProcess getReleaseProcess ()
	{
		return releaseProcess;
	}

	public void setReleaseProcess (ReleaseProcess releaseProcess)
	{
		this.releaseProcess = releaseProcess;
	}


	public class CustomerWSR
	{
		private String customerWSRStatus;

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
	}
	
	public class Metrics
	{
		private String metricsStatus;

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

	}

	public class ReleaseProcess
	{
		private String releaseProcessStatus;

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

	}

	public class TestAutomation
	{
		private String testAutomationPercentage;

		private String testAutomationStatus;

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


	}

	public class SecurityAssessment
	{
		private String securityAssessmentStatus;

		private String securityAssessmentIndex;

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

	}

	public class PerformanceAssessment
	{
		private String performanceAssessmentStatus;

		private String performanceAssessmentPercentage;

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

	}

	public class TestingProcess
	{
		private String testingProcessStatus;

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
	}

	public class ContinuousIntegration
	{
		private String continuousIntegrationStatus;

		private String continuousIntegrationIndex;

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
	}

	public class CodeCoverage
	{
		private String codeCoveragePercentage;

		private String codeCoverageStatus;

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

	}

	public class AutomatedUnitTesting
	{
		private String automatedUnitTestingStatusPercentage;

		private String automatedUnitTestingStatus;

		/**
		 * @return the automatedUnitTestingStatusPercentage
		 */
		public String getAutomatedUnitTestingStatusPercentage() {
			return automatedUnitTestingStatusPercentage;
		}

		/**
		 * @param automatedUnitTestingStatusPercentage the automatedUnitTestingStatusPercentage to set
		 */
		public void setAutomatedUnitTestingStatusPercentage(
				String automatedUnitTestingStatusPercentage) {
			this.automatedUnitTestingStatusPercentage = automatedUnitTestingStatusPercentage;
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
		
	}

	public class ManualCodeReview
	{
		private String manualCodeReviewStatusCount;

		private String manualCodeReviewStatus;

		/**
		 * @return the manualCodeReviewStatusCount
		 */
		public String getManualCodeReviewStatusCount() {
			return manualCodeReviewStatusCount;
		}

		/**
		 * @param manualCodeReviewStatusCount the manualCodeReviewStatusCount to set
		 */
		public void setManualCodeReviewStatusCount(String manualCodeReviewStatusCount) {
			this.manualCodeReviewStatusCount = manualCodeReviewStatusCount;
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

	}

	public class ProductKnowledge
	{
		private String productKnowledgeIndex;

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

	}

	public class DomainKnowledge
	{
		private String domainKnowledgeStatus;

		/**
		 * @return the domainKnowledgeStatus
		 */
		public String getDomainKnowledgeStatus() {
			return domainKnowledgeStatus;
		}

		/**
		 * @param domainKnowledgeStatus the domainKnowledgeStatus to set
		 */
		public void setDomainKnowledgeStatus(String domainKnowledgeStatus) {
			this.domainKnowledgeStatus = domainKnowledgeStatus;
		}

	}

	public class StaticCodeAnalysis
	{
		private String staticCodeAnalysisIndex;

		private String staticCodeAnalysisStatus;

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

	}

	public class DesignFocus
	{
		private String designFocusStatus;

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

	}

	public class ArchitectureFocus
	{
		private String architectureFocusStatus;

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
		
	}

	public class Requirements
	{
		private String RequirementsStatus;

		/**
		 * @return the requirementsStatus
		 */
		public String getRequirementsStatus() {
			return RequirementsStatus;
		}

		/**
		 * @param requirementsStatus the requirementsStatus to set
		 */
		public void setRequirementsStatus(String requirementsStatus) {
			RequirementsStatus = requirementsStatus;
		}
		
	}

	public class TeamSize
	{
		private String development;

		private String testing;

		public String getDevelopment ()
		{
			return development;
		}

		public void setDevelopment (String development)
		{
			this.development = development;
		}

		public String getTesting ()
		{
			return testing;
		}

		public void setTesting (String testing)
		{
			this.testing = testing;
		}
	}
}
