package com.capitalone.dashboard.model;

/**
 * 
 * @author Xoz
 *
 */
public class BurnDownHistory extends BaseModel {

	private String date;

	private Double completedIssuesEstimateSum;

	private Double allIssuesEstimateSum;

	private Double remainingIssues;

	
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param string the date to set
	 */
	public void setDate(String string) {
		this.date = string;
	}

	public Double getRemainingIssues() {
		return remainingIssues;
	}

	public void setRemainingIssues(Double remainingIssues) {
		this.remainingIssues = remainingIssues;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((date == null) ? 0 : date.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BurnDownHistory other = (BurnDownHistory) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}

	
}