package com.capitalone.dashboard.model;



public class Burndown {

    private IssueCount initialIssueCount;
    private IssueCount issuesAdded;
    private IssueCount issuesRemoved;

    /**
     * 
     * @return
     *     The initialissuecount
     */
    public IssueCount getInitialIssueCount() {
        return initialIssueCount;
    }

    /**
     * 
     * @param initialissuecount
     *     The initialissuecount
     */
    public void setInitialIssueCount(IssueCount initialIssueCount) {
        this.initialIssueCount = initialIssueCount;
    }

    /**
     * 
     * @return
     *     The issuesadded
     */
    public IssueCount getIssuesAdded() {
        return issuesAdded;
    }

    /**
     * 
     * @param issuesadded
     *     The issuesadded
     */
    public void setIssuesAdded(IssueCount issuesAdded) {
        this.issuesAdded = issuesAdded;
    }

    /**
     * 
     * @return
     *     The issuesremoved
     */
    public IssueCount getIssuesRemoved() {
        return issuesRemoved;
    }

    /**
     * 
     * @param issuesremoved
     *     The issuesremoved
     */
    public void setIssuesRemoved(IssueCount issuesRemoved) {
        this.issuesRemoved = issuesRemoved;
    }
    
    public class IssueCount{
    	
    	Integer count;
    	Float storyPoints;
    	
    	public Integer getCount() {
    		return count;
    	}
    	public void setCount(Integer count) {
    		this.count = count;
    	}
    	public Float getStoryPoints() {
    		return storyPoints;
    	}
    	public void setStoryPoints(Float storyPoints) {
    		this.storyPoints = storyPoints;
    	}	
    }
}

