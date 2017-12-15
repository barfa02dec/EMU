package com.capitalone.dashboard.model;

/**
 * 
 * @author Xoz
 *
 */
public class BurnDownHistory extends BaseModel {

	private Long miliseconds;

	private Double completedIssuesEstimateSum;

	private Double allIssuesEstimateSum;

	private Double remainingIssues;

	public Double getRemainingIssues() {
		return remainingIssues;
	}

	public void setRemainingIssues(Double remainingIssues) {
		this.remainingIssues = remainingIssues;
	}

	public Long getMiliseconds() {
		return miliseconds;
	}

	public void setMiliseconds(Long miliseconds) {
		this.miliseconds = miliseconds;
	}

	public Double getCompletedIssuesEstimateSum() {
		return completedIssuesEstimateSum;
	}

	public void setCompletedIssuesEstimateSum(Double completedIssuesEstimateSum) {
		this.completedIssuesEstimateSum = completedIssuesEstimateSum;
	}

	public Double getAllIssuesEstimateSum() {
		return allIssuesEstimateSum;
	}

	public void setAllIssuesEstimateSum(Double allIssuesEstimateSum) {
		this.allIssuesEstimateSum = allIssuesEstimateSum;
	}

}